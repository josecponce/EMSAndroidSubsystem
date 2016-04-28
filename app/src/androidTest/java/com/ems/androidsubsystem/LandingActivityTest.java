package com.ems.androidsubsystem;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.ButtonMode;
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
public class LandingActivityTest {
    @Rule
    public final ActivityTestRule<LandingActivity> activityRule =
            new ActivityTestRule<>(LandingActivity.class,false,false);
    public LandingActivity activity;

    FragmentPagerAdapter adapterViewPager;
    BootstrapButton eventsListButton;
    ViewPager vpPager;

    @After
    public void tearDown() throws Exception{

    }

    @Before
    public void setUp() throws Exception{

        activity = activityRule.launchActivity(new Intent());

        vpPager = (ViewPager) activity.findViewById(R.id.landing_event_fragment_container);
        eventsListButton = (BootstrapButton) activity.findViewById(R.id.landing_goToEvents_button);
        adapterViewPager = activity.getAdapterViewPager();
    }

    @Test
    public void wireUIElements() throws Exception {
        assertNotNull(adapterViewPager);

        assertNotNull(vpPager.getAdapter());

        assertTrue(eventsListButton.hasOnClickListeners());
        assertEquals(ButtonMode.REGULAR, eventsListButton.getButtonMode());
    }
}