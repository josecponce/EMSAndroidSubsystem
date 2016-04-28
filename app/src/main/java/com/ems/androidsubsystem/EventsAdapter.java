package com.ems.androidsubsystem;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ems.DataSubsystem.Event;

import java.util.List;

/**
 * This is an adapter for creating the views to be displayed in the EventsActivity/page.
 * @author emsTeam
 *
 */
public class EventsAdapter extends BaseAdapter implements AndroidAPIProxy.onEventsRefreshed{

    private static final String LOG ="EventsAdatper";

    public EventsSingleton getEventsSingleton() {
        return eventsSingleton;
    }

    public void setEventsSingleton(EventsSingleton eventsSingleton) {
        this.eventsSingleton = eventsSingleton;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
	 * List of events that are being displayed to the user.
	 */
	//private List<Event> events;//do i eliminate this and leave in the event singleton only??
    private EventsSingleton eventsSingleton;
    private Context context;

    @Override
    public void onEventsRefreshed(List<Event> events) {
        Log.d(LOG, "Calling onEventsRefreshed");
        //this has to be called from the UI thread, Singleton should take care of it
        notifyDataSetChanged();
    }

    public EventsAdapter(Context c, Handler handler){
        eventsSingleton = EventsSingleton.getInstance(handler);
        context =c;
    }
	/**
	 * Getter of events
	 */
	public List<Event> getEvents() {
	 	//todo delete getEvents()
        return eventsSingleton.getEvents(this);
	}

	/*
	 * Setter of events
     * @param events list of events to set
     */
	/*public void setEvents(List<Event> events) {

        this.events = events;
	} */
	
	/**
	 * From BaseAdapter. Needed by the ListView in 
	 * android to display a list of views to the user. 
	 * @return Number of elements in the list. 
	 */
    @Override
    public int getCount() {
        List<Event> events = eventsSingleton.getEvents(this);

        int size = 0;
        if (events !=null)//it might be null if it is still not initialized
            size =events.size();
        Log.d(LOG, "getCount: "+size);
        return size;
    }

    /**
	 * From BaseAdapter. Needed by the ListView in 
	 * android to display a list of views to the user.
	 * @param position Index of the view to be returned.
	 * @param convertView A view that the could be provided by ListView, to avoid creating a new one from scratch.
	 * @param parent The parent of the view to be created.
	 * @return A view for the ListView to display to the user. 
	 */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //create the listView element
        View v;
        if (convertView==null)
            v = View.inflate(context, R.layout.activity_events_listview_element, null);
        else
            v = convertView;
        v.setClickable(false);
        v.setFocusable(false);
        Event event = eventsSingleton.getEvents(this).get(position);

        //get handles to ui elements
        AwesomeTextView fromDate = (AwesomeTextView) v.findViewById(R.id.events_listView_eventDate);
        AwesomeTextView location = (AwesomeTextView) v.findViewById(R.id.events_listView_eventLocation);
        AwesomeTextView name = (AwesomeTextView) v.findViewById(R.id.events_listView_eventName);

        //populate fields in the view
        fromDate.setText(event.getStartDate());
        fromDate.setFocusable(false);
        fromDate.setClickable(false);
        location.setText(event.getLocation());
        location.setFocusable(false);
        location.setClickable(false);
        name.setText(event.getName());
        name.setFocusable(false);
        name.setClickable(false);

        return v;
    }

    /**
	 * From BaseAdapter. Needed by the ListView in 
	 * android to display a list of views to the user.
	 * @param position Index of the event in question.
	 * @return The id of the element at that index in the list.
	 */
    @Override
    public long getItemId(int position) {
        return eventsSingleton.getEvents(this).get(position).getId();
    }

    /**
	 * From BaseAdapter. Needed by the ListView in 
	 * android to display a list of views to the user.
	 * @param position Index of the event to be returned.
	 * @return The event at index "position" in the list.
	 */
    @Override
    public Object getItem(int position) {
        return eventsSingleton.getEvents(this).get(position);
    }
}
