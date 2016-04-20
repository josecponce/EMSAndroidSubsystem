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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.ems.DataSubsystem.Event;
import com.ems.DataSubsystem.Payment;

import org.json.JSONArray;
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
//http://puro.ignorelist.com:8080/last/android_api/pay
    //http://ec2-54-191-253-30.us-west-2.compute.amazonaws.com
    private static final String API_PURCHASE_TICKET = "http://ec2-54-191-253-30.us-west-2.compute.amazonaws.com:8080/demo/android_api/";

    private onTicketPurchased ticketObserver;
    private onEventsRefreshed eventObserver;
    private DateFormatter dateFormatter =new DateFormatter();

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
	public void purchaseTicket(final onTicketPurchased observer, final Event event,
                               final int count, final Payment payment) {
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
                    String requestString =API_PURCHASE_TICKET+"pay/" + event.getId()+"/"+count;
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

                    output.beginObject();
                    output.name("ticket").beginObject();
                    output.name("count").value(count);
                    output.name("price").value(event.getTicketPrice());
                    output.name("number").value(-1);
                    output.name("eventName").value(event.getName());
                    output.name("email").value(payment.getEmail()).endObject();
                    output.name("payment").beginObject();
                    output.name("creditCardNumber").value(payment.getCreditCardNumber());
                    output.name("date").value(new SimpleDateFormat("yyyy-MM-dd").format(payment.date));
                    output.name("email").value(payment.getEmail());
                    output.name("creditCardExpirationDate").value(payment.getCreditCardExpirationDate());
                    output.name("amount").value(payment.getAmount()).endObject().endObject().flush();

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
        /*Log.d(LOG, "getEvents");
        //this is being called in an external
		//todo important getEvents() from the server (HTTPUrlConnection)
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String category;
            if (i%2 ==0)
                category = "Trade Show";
            else
                category = "Conference";
            events.add(new Event("Event "+ i, "Date "+ i, "Location "+ i, category+ i, 25));
        }
		return events;*/

        //getting the events from the server
        Log.d(LOG, "get Events running");
        boolean failed = false;
        HttpURLConnection con = null;
        InputStream input = null;
        StringBuilder builder;
        List<Event> events = null;
        try {
            String requestString =API_PURCHASE_TICKET+"events_list";
            URL url = new URL(requestString);

            con = (HttpURLConnection) url.openConnection();

            con.setReadTimeout(5000);
            con.setConnectTimeout(5000);
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);

            input = new BufferedInputStream(con.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            builder = new StringBuilder();
            Log.d(LOG, String.valueOf(con.getResponseCode()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String jsonResponse = builder.toString();//this should be a simple string
            Log.d(LOG, "Purchase Ticket runnable end: server said: "+ jsonResponse);
            
            events = parseEvents(jsonResponse);
        }catch (JSONException e) {
                    e.printStackTrace();
                    failed =true;
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
                } catch (Exception e) {//todo: catch exceptions appropriately
                    e.printStackTrace();
                }
            if (con != null)
                con.disconnect();
        }
        //return value might be null in case of an exception
        return events;
	 }

    private List<Event> parseEvents(String jsonResponse) throws JSONException{
        JSONTokener tokener = new JSONTokener(jsonResponse);
        JSONArray eventsArray = (JSONArray) tokener.nextValue();
        JSONObject jsonEvent;
        List<Event> events= new ArrayList<>();

        for(int i = 0; i<eventsArray.length();i++){
            jsonEvent= eventsArray.getJSONObject(i);

            Event event = new Event();
            event.setCategory(jsonEvent.getString("category"));
            event.setDescription(jsonEvent.getString("description"));
            event.setEndDate(jsonEvent.getString("endDate"));
            event.setId(jsonEvent.getInt("id"));
            event.setImage(jsonEvent.getString("image"));
            event.setLocation(jsonEvent.getString("location"));
            event.setName(jsonEvent.getString("name"));

            int proposalAllowed = jsonEvent.getInt("proposalAllowed");
            event.setProposalsAllowed((proposalAllowed==0)?false:true);
            event.setStartDate(jsonEvent.getString("startDate"));
            event.setTicketPrice(jsonEvent.getDouble("ticketPrice"));
            event.setTime(jsonEvent.getString("time"));

            events.add(event);
        }

        return events;
    }

}
