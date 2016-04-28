package com.ems.androidsubsystem;

import android.support.v7.widget.ListViewCompat;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;


/**
 * Created by josecponce on 4/27/16.
 */
public class ViewEventDetailsSystemTest extends ActivityInstrumentationTestCase2<LandingActivity>{

    private Solo solo;

    public ViewEventDetailsSystemTest() {
        super(LandingActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(),getActivity());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * This scrolls a few times on the carousel, then goes to eventsList,
     * and then goes straight to eventDetails for event 1 in the list.
     * @throws Exception
     */
    public void testViewEventDetails() throws Exception{
        //scroll the events carousel in the landing page
        solo.scrollToSide(Solo.RIGHT);
        solo.scrollToSide(Solo.RIGHT);
        solo.scrollToSide(Solo.RIGHT);
        solo.scrollToSide(Solo.LEFT);

        //go to eventsList page
        solo.clickOnText("Events List");

        solo.clickInList(0);
        solo.sleep(1000);
    }

    /**
     * This goes to the eventsList, then scrolls on the list of events
     * and clicks on the last event to see its details.
     * @throws Exception
     */
    public void testViewEventDetails2() throws Exception{

        solo.clickOnText("Events List");

        solo.scrollListToBottom(0);
        int count = ((ListViewCompat)solo.getCurrentActivity().findViewById(R.id.events_listView)).getChildCount();
        solo.clickInList(count);
        solo.sleep(1000);
    }
}
