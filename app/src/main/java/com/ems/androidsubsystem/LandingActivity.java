package com.ems.androidsubsystem;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.ButtonMode;
import com.ems.DataSubsystem.Event;

import java.util.List;

public class LandingActivity extends AppCompatActivity{

    FragmentPagerAdapter adapterViewPager;
    BootstrapButton eventsListButton;
    ViewPager vpPager;

    public FragmentPagerAdapter getAdapterViewPager() {
        return adapterViewPager;
    }

    public void setAdapterViewPager(FragmentPagerAdapter adapterViewPager) {
        this.adapterViewPager = adapterViewPager;
    }

    public BootstrapButton getEventsListButton() {
        return eventsListButton;
    }

    public void setEventsListButton(BootstrapButton eventsListButton) {
        this.eventsListButton = eventsListButton;
    }

    public ViewPager getVpPager() {
        return vpPager;
    }

    public void setVpPager(ViewPager vpPager) {
        this.vpPager = vpPager;
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter implements AndroidAPIProxy.onEventsRefreshed{
        EventsSingleton eventsSingleton;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            eventsSingleton = EventsSingleton.getInstance(new Handler());
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            List<Event> events = eventsSingleton.getEvents(this);

            int size = 0;
            if (events !=null)//it might be null if it is still not initialized
                size =events.size();
            return size;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            //use the position to get from the singleton
            Event event = eventsSingleton.getEvents(this).get(position);
            LandingActivityEventFragment eventFragment = LandingActivityEventFragment.newInstance(event);
            return eventFragment;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

        @Override
        public void onEventsRefreshed(List<Event> events) {
            notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        vpPager = (ViewPager) findViewById(R.id.landing_event_fragment_container);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        eventsListButton = (BootstrapButton) findViewById(R.id.landing_goToEvents_button);
        eventsListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingActivity.this, EventsActivity.class);
                LandingActivity.this.startActivity(i);
            }
        });
        eventsListButton.setButtonMode(ButtonMode.REGULAR);


    }
}
