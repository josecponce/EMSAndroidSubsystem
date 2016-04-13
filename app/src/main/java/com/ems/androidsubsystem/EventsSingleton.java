package com.ems.androidsubsystem;

import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;

import com.ems.DataSubsystem.Event;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by josecponce on 4/6/16.
 */
public class EventsSingleton{

    private static final String LOG= "EventsSingleton";

    private static Handler handler;
    private static EventsSingleton eventsSingleton;
    private List<Event> events;
    private AndroidAPIProxy proxy = new AndroidAPIProxy();
    private Date lastMod;

    public static EventsSingleton getInstance(Handler handler) {
        if (eventsSingleton==null){
            EventsSingleton.handler = handler;
            eventsSingleton = new EventsSingleton();
        }
        return eventsSingleton;
    }

    private EventsSingleton() {
        //get the new events from the proxy asynchronously(because it may take time)
        new Thread(new Runnable() {
            @Override
            public void run() {//eventsSingleton will receive a callback when the new events are here
                events = proxy.getEvents();
                lastMod = new Date();
            }
        }).start();
    }

    public List<Event> getEvents(final AndroidAPIProxy.onEventsRefreshed observer) {
        //logic to get new Events from the proxy, if deemed necessary
        Log.d(LOG, "getEvents");
        long elapsed = -1;
        if (lastMod!=null)
            elapsed= new Date().getTime() - lastMod.getTime();
        if (elapsed > 1000*60||elapsed ==-1) {//get new data from the proxy if some time has elapsed
            new Thread(new Runnable() {
                @Override
                public void run() {//eventsSingleton will receive a callback when the new events are here
                    events = proxy.getEvents();
                    lastMod = new Date();
                    handler.post(new Runnable() {//this needs to be posted to the android UI thread
                        @Override
                        public void run() {
                            Log.d(LOG, "getEvents, other thread, before calling onEventsRefreshed");
                            observer.onEventsRefreshed(EventsSingleton.this.events);
                        }
                    });
                }
            }).start();
        }
        //return value is null when we need to get new info from the proxy,caller will receive
        //the information via the AndroidAPIProxy.onEventsRefreshed callback
        return events;
    }
}
