package com.ems.androidsubsystem;

import android.content.Intent;
import android.os.Handler;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.ListViewCompat;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapDropDown;
import com.beardedhen.androidbootstrap.BootstrapEditText;
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
public class EventsActivityTest {
    @Rule
    public final ActivityTestRule<EventsActivity> activityRule =
            new ActivityTestRule<>(EventsActivity.class,false,false);
    public EventsActivity activity;

    private DateFormatter dateFormatter = new DateFormatter();
    private BootstrapEditText mFromDateEditText;
    private BootstrapEditText mToDateEditText;
    private BootstrapDropDown mCategoryDropDown;
    private BootstrapButton mFilterButton;
    private ListViewCompat mEventsList;
    private EventsAdapter adapter;

    @After
    public void tearDown() throws Exception{

    }

    @Before
    public void setUp() throws Exception{
        activity = activityRule.launchActivity(new Intent());

        EventsActivity activity= activityRule.getActivity();

        mFromDateEditText = (BootstrapEditText) activity.findViewById(R.id.events_from_date_editText);
        mToDateEditText = (BootstrapEditText) activity.findViewById(R.id.events_to_date_editText);
        mCategoryDropDown = (BootstrapDropDown) activity.findViewById(R.id.events_category_dropDown);
        mFilterButton = (BootstrapButton) activity.findViewById(R.id.events_filter_button);
        mEventsList = (ListViewCompat) activity.findViewById(R.id.events_listView);
    }

    @Test
    public void wireUIElements() throws Exception {

        assertTrue(mFilterButton.hasOnClickListeners());
        assertNotNull(activity.getAdapter());
        assertNotNull(activity.getAdapter().getEventsSingleton());
        assertSame(mEventsList.getAdapter(),activity.getAdapter());
        assertSame(mEventsList.getOnItemClickListener(),activity);
        assertTrue(mEventsList.isClickable());
    }
}