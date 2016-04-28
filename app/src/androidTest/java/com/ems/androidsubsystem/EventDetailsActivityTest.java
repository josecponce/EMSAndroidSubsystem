package com.ems.androidsubsystem;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.ems.DataSubsystem.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(AndroidJUnit4.class)
public class EventDetailsActivityTest {
    @Rule
    public final ActivityTestRule<EventDetailsActivity> activityRule =
            new ActivityTestRule<>(EventDetailsActivity.class,false,false);
    public EventDetailsActivity activity;

    private DateFormatter dateFormatter = new DateFormatter();
    private AwesomeTextView mEventNameTextView;
    private AwesomeTextView mEventLocationTextView;
    private AwesomeTextView mEventStartDateTextView;
    private AwesomeTextView mEventEndDateTextView;
    private AwesomeTextView mEventTimeTextView;
    private AwesomeTextView mEventDescriptionTextView;
    private BootstrapButton mPurchaseTicketButton;
    Event event;
    @After
    public void tearDown() throws Exception{

    }

    @Before
    public void setUp() throws Exception{

        event = new Event("testName", "2017-11-11 11:11:11","testLocation", "testCategory", 100.00);
        event.setTime("2017-11-11 14:20:00");
        event.setEndDate("2017-11-11 11:11:11");
        Intent i = new Intent();
        i.putExtra(EventsActivity.EXTRA_EVENT, event);
        activity = activityRule.launchActivity(i);

        mEventNameTextView =(AwesomeTextView) activity.findViewById(R.id.eventDetails_eventName);
        mEventLocationTextView= (AwesomeTextView) activity.findViewById(R.id.eventDetails_eventLocation);
        mEventStartDateTextView = (AwesomeTextView) activity.findViewById(R.id.eventDetails_eventStartDate);
        mEventEndDateTextView = (AwesomeTextView) activity.findViewById(R.id.eventDetails_eventEndDate);
        mEventTimeTextView = (AwesomeTextView) activity.findViewById(R.id.eventDetails_eventTime);
        mEventDescriptionTextView = (AwesomeTextView) activity.findViewById(R.id.eventDetails_eventDescription);
        mPurchaseTicketButton = (BootstrapButton) activity.findViewById(R.id.eventDetails_purchaseTicket_button);
    }

    @Test
    public void wireUIElements() throws Exception {
        String startDate = event.getStartDate();
        startDate = dateFormatter.fromServerToLocal(startDate);

        String endDate = event.getEndDate();
        endDate = dateFormatter.fromServerToLocal(endDate);

        String time = event.getTime();
        time = dateFormatter.formatTime(time);

        final String defaults = "There is no description";

        assertEquals(mEventNameTextView.getText().toString(),event.getName());
        assertEquals(mEventLocationTextView.getText().toString(),event.getLocation());

        assertEquals(mEventStartDateTextView.getText().toString(),startDate);
        assertEquals(mEventEndDateTextView.getText().toString(),endDate);

        assertEquals(mEventTimeTextView.getText().toString(),time);
        assertEquals(mEventDescriptionTextView.getText().toString(),defaults);

        assertTrue(mPurchaseTicketButton.isClickable());
    }
}