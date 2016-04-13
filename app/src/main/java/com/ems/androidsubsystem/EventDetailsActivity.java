package com.ems.androidsubsystem;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.ButtonMode;
import com.ems.DataSubsystem.Event;

public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG ="EventDetailsActivity";

    private AwesomeTextView mEventNameTextView;
    private AwesomeTextView mEventLocationTextView;
    private AwesomeTextView mEventDateTextView;
    private AwesomeTextView mEventTimeTextView;
    private AwesomeTextView mEventDescriptionTextView;
    private BootstrapButton mPurchaseTicketButton;
    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        mEvent = (Event) getIntent().getSerializableExtra(EventsActivity.EXTRA_EVENT);
        wireUIElements();
    }

    public void purchaseTicket() {
        Intent i = new Intent(this, PurchaseTicketActivity.class);
        i.putExtra(EventsActivity.EXTRA_EVENT, mEvent);
        startActivity(i);
    }

    private void wireUIElements() {
        //get handles to UI elements
        mEventNameTextView =(AwesomeTextView) findViewById(R.id.eventDetails_eventName);
        mEventLocationTextView= (AwesomeTextView) findViewById(R.id.eventDetails_eventLocation);
        mEventDateTextView = (AwesomeTextView) findViewById(R.id.eventDetails_eventDate);
        mEventTimeTextView = (AwesomeTextView) findViewById(R.id.eventDetails_eventTime);
        mEventDescriptionTextView = (AwesomeTextView) findViewById(R.id.eventDetails_eventDescription);
        mPurchaseTicketButton = (BootstrapButton) findViewById(R.id.eventDetails_purchaseTicket_button);

        //configure UI elements
        mEventNameTextView.setText(mEvent.getName());
        mEventLocationTextView.setText(mEvent.getLocation());
        mEventDateTextView.setText(mEvent.getStartDate());
        mEventTimeTextView.setText("Some made up time");
        final String defaults = "There is no description";
        final String description = mEvent.getDescription();
        mEventDescriptionTextView.setText((description ==null)? defaults:description);
        mPurchaseTicketButton.setButtonMode(ButtonMode.REGULAR);
        mPurchaseTicketButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.eventDetails_purchaseTicket_button){//if purchase button clicked
            Log.d(LOG, "clicked purchase ticket");
            purchaseTicket();
        }
    }
}
