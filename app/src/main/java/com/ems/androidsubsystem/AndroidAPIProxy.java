package com.ems.androidsubsystem;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.ems.DataSubsystem.Event;

/**
 * This class is in charge of calling methods on the remote AndroidAPI class.
 * It is the only way to access the system remote API from the android subsystem.
 * @author emsTeam
 *
 */
public class AndroidAPIProxy {

    private static final String LOG = "AndroidAPIProxy";

    private onEventsRefreshed eventObserver;
    private onTicketPurchased ticketObserver;
    interface onEventsRefreshed{
        void onEventsRefreshed(List<Event> events);
    };

    interface onTicketPurchased{
        void onTicketPurchased(boolean success);
    }
	/**
	 * Will send the necessary information to the remote API to record the 
	 * purchase of a ticket through the android application.
	 * @param event Event for which the ticket is being purchased.
	 * @param count Number of tickets purchased.
	 */
	public void purchaseTicket(onTicketPurchased observer,Event event, int count) {
		ticketObserver = observer;
        // TODO purchaseTicket()

        //callback to the ticketObserver to let him know that the purchase is over
        ticketObserver.onTicketPurchased(true);
	 }

	/**
	 * This method retrieves the list of active events from the server.
	 * Since this uses the network, the user should make sure to call this
	 *  on a separate thread to avoid blocking the android UI thread.
	 * @return List of active events in the system.
	 */
	public List<Event> getEvents() {
        Log.d(LOG, "getEvents");
        //this is being called in an external
		//todo getEvents() from the server (HTTPUrlConnection)
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String category;
            if (i%2 ==0)
                category = "Trade Show";
            else
                category = "Conference";
            events.add(new Event("Event "+ i, "Date "+ i, "Location "+ i, category+ i));
        }
		return events;
	 } 

}
