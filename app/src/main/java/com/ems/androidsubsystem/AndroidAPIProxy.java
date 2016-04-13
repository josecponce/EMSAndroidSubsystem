package com.ems.androidsubsystem;

import android.os.Handler;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.ems.DataSubsystem.Event;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

/**
 * This class is in charge of calling methods on the remote AndroidAPI class.
 * It is the only way to access the system remote API from the android subsystem.
 * @author emsTeam
 *
 */
public class AndroidAPIProxy {

    private static final String LOG = "AndroidAPIProxy";

    private static final String API_PURCHASE_TICKET = "http://puro.ignorelist.com:8080/ems_github/android_api/pay";

    private onTicketPurchased ticketObserver;
    
    interface onEventsRefreshed{
        void onEventsRefreshed(List<Event> events);
    }

    interface onTicketPurchased{
        void onTicketPurchased(boolean success);
    }
	/**
	 * Will send the necessary information to the remote API to record the 
	 * purchase of a ticket through the android application. This is an asynchronous call.
     * A callback will be made to the observer object.
	 * @param event Event for which the ticket is being purchased.
	 * @param count Number of tickets purchased.
	 */
	public void purchaseTicket(final onTicketPurchased observer, final Event event, final int count) {
        //this needs to be called from the UI thread to get the Handler object
		ticketObserver = observer;
        final Handler handler = new Handler();

        // TODO purchaseTicket()
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG, "Purchase Ticket runnable start");
                boolean failed = false;
                HttpURLConnection con = null;
                InputStream input = null;
                JsonWriter output = null;
                StringBuilder builder;
                boolean successful =false;
                try {
                    String requestString =API_PURCHASE_TICKET+"/" + event.getId()+"/"+count;
                    URL url = new URL(requestString);

                    con = (HttpURLConnection) url.openConnection();


                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "application/json");
                    con.setDoInput(true);

                    con.setDoOutput(true);
                    output  = new JsonWriter(new PrintWriter(con.getOutputStream()));
                    //{"count":null,"price":0.0,"number":null,"eventName":null}

                    output.beginObject().name("count").value(count);
                    output.name("price").value(event.getTicketPrice());
                    output.name("number").value(-1);
                    output.name("eventName").value(event.getName()).endObject().flush();





                    Log.d(LOG, String.valueOf(con.getResponseCode()));
                    input = new BufferedInputStream(con.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String line;
                    builder = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    String jsonResponse = builder.toString();//this should be a simple string
                    Log.d(LOG, "Purchase Ticket runnable end: server said: "+ jsonResponse);
                    successful = Boolean.parseBoolean(jsonResponse);

                    /*JSONTokener tokener = new JSONTokener(jsonString);
                    JSONObject jsonObject = (JSONObject) tokener.nextValue();
                }catch (JSONException e) {
                    e.printStackTrace();
                    failed =true;*/
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    failed = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    failed = true;
                    Log.d(LOG, "IOException, possibly no connection to server");
                } finally {
                    if (input != null)
                        try {
                            input.close();
                            output.close();
                        } catch (Exception e) {//todo: catch exceptions appropriately
                            e.printStackTrace();
                        }
                    if (con != null)
                        con.disconnect();
                }

                Log.d(LOG, "Download " + ((failed)?"failed":"succeded"));
                final boolean finalSuccess = (!failed)&&successful;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        observer.onTicketPurchased(finalSuccess);
                    }
                });
            }
        }).start();
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
            events.add(new Event("Event "+ i, "Date "+ i, "Location "+ i, category+ i, 25));
        }
		return events;
	 } 

}
