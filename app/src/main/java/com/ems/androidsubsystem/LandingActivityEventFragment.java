package com.ems.androidsubsystem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.ems.DataSubsystem.Event;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by josecponce on 4/17/16.
 */
public class LandingActivityEventFragment extends Fragment {
        // Store instance variables
        private Event mEvent;
        private DateFormatter dateFormatter = new DateFormatter();

        // newInstance constructor for creating fragment with arguments
        public static LandingActivityEventFragment newInstance(Event event) {
            LandingActivityEventFragment fragmentFirst = new LandingActivityEventFragment();
            Bundle args = new Bundle();
            args.putSerializable("event", event);
            fragmentFirst.setArguments(args);
            return fragmentFirst;
        }

        // Store instance variables based on arguments passed
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mEvent = (Event) getArguments().getSerializable("event");
        }

        // Inflate the view for the fragment based on layout XML
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_landing_event_fragment, container, false);

            AwesomeTextView eventName = (AwesomeTextView) view.findViewById(R.id.landing_event_name);
            eventName.setText(mEvent.getName());

            AwesomeTextView eventLocation = (AwesomeTextView) view.findViewById(R.id.landing_event_Location);
            eventLocation.setText(mEvent.getLocation());

            try {//no exception should be thrown here
                AwesomeTextView eventStartDate = (AwesomeTextView) view.findViewById(R.id.landing_event_startDate);
                eventStartDate.setText(dateFormatter.fromServerToLocal(mEvent.getStartDate()));

                AwesomeTextView eventEndDate = (AwesomeTextView) view.findViewById(R.id.landing_event_endDate);
                eventEndDate.setText(dateFormatter.fromServerToLocal(mEvent.getEndDate()));

                AwesomeTextView eventTime = (AwesomeTextView) view.findViewById(R.id.landing_event_Time);
                eventTime.setText(dateFormatter.formatTime(mEvent.getTime()));
            }catch (ParseException e){
                e.printStackTrace();
            }
            AwesomeTextView eventDescription = (AwesomeTextView) view.findViewById(R.id.landing_event_Description);
            eventDescription.setText(mEvent.getDescription());

            return view;
        }
}
