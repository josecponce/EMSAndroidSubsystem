package com.ems.androidsubsystem;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapDropDown;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ems.DataSubsystem.Event;

import java.util.Date;

public class EventsActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, BootstrapDropDown.OnDropDownItemClickListener {

    private static final String LOG = "EventsActivity";
    public static final String EXTRA_EVENT = "com.ems.androidsubsystem.EventsActivity.Extra_event";

    private BootstrapEditText mFromDateEditText;
    private BootstrapEditText mToDateEditText;
    private BootstrapDropDown mCategoryDropDown;
    private BootstrapButton mFilterButton;
    private ListViewCompat mEventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        wireUIElements();
    }

    private void wireUIElements() {
        mFromDateEditText = (BootstrapEditText) findViewById(R.id.events_from_date_editText);
        mToDateEditText = (BootstrapEditText) findViewById(R.id.events_to_date_editText);
        mCategoryDropDown = (BootstrapDropDown) findViewById(R.id.events_category_dropDown);
        mFilterButton = (BootstrapButton) findViewById(R.id.events_filter_button);
        mEventsList = (ListViewCompat) findViewById(R.id.events_listView);

        mCategoryDropDown.setOnDropDownItemClickListener(this);
        mFilterButton.setOnClickListener(this);
        mEventsList.setAdapter(new EventsAdapter(this, new Handler()));
        mEventsList.setOnItemClickListener(this);
        mEventsList.setClickable(true);
    }

    public void filter(Date toDate, String category, Date fromDate) {
        // TODO Auto-generated method
    }

    //onClickListener interface
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.events_filter_button){//the filter button has been clicked
            //todo filter the list of events by using the singleton data
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG, "list item clicked");
        chooseEvent((Event) parent.getAdapter().getItem(position));
    }

    public void chooseEvent(Event event) {
        //todo go to the event details activity for the chosen event
        Intent i = new Intent(this, EventDetailsActivity.class);
        i.putExtra(EXTRA_EVENT, event);
        startActivity(i);
    }

    @Override
    public void onItemClick(ViewGroup parent, View v, int id) {
        mCategoryDropDown.setText(this.getResources().getTextArray(R.array.dropdown)[id]);
    }
}
