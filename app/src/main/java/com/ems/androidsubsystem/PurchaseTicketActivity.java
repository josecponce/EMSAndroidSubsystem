package com.ems.androidsubsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.ButtonMode;
import com.ems.DataSubsystem.Event;

public class PurchaseTicketActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, AndroidAPIProxy.onTicketPurchased {

    public static final String LOG = "PurchaseTicketActivity";

    private AwesomeTextView mEventNameTextView;
    private AwesomeTextView mEventLocationTextView;
    private AwesomeTextView mEventDateTextView;
    private AwesomeTextView mEventTimeTextView;
    private BootstrapEditText mTicketCountEditText;
    private AwesomeTextView mTicketTotalAmountTextView;
    private BootstrapButton mPurchaseTicketButton;

    private Event mEvent;
    private AndroidAPIProxy mProxy = new AndroidAPIProxy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_ticket);

        mEvent = (Event) getIntent().getSerializableExtra(EventsActivity.EXTRA_EVENT);
        wireUIElements();
    }
    
    public void purchaseTicket() {
        // TODO get the proxy to do this
        int count;
        try{
            count = Integer.parseInt(mTicketCountEditText.getText().toString());
        }catch (NumberFormatException e){//if there is not a number in the count field
            final String text = "Please enter a valid integer in the count field";
            Toast message = Toast.makeText(this, text, Toast.LENGTH_LONG);
            message.show();
            return;
        }

        mProxy.purchaseTicket(this,mEvent,count);
    }

    private void wireUIElements() {

        //get handle of UI elements
        mEventNameTextView = (AwesomeTextView) findViewById(R.id.purchase_ticket_eventName);
        mEventLocationTextView = (AwesomeTextView) findViewById(R.id.purchase_ticket_eventLocation);
        mEventDateTextView = (AwesomeTextView) findViewById(R.id.purchase_ticket_eventDate);
        mEventTimeTextView = (AwesomeTextView) findViewById(R.id.purchase_ticket_eventTime);
        mTicketCountEditText = (BootstrapEditText) findViewById(R.id.purchase_ticket_ticketCount);
        mTicketTotalAmountTextView = (AwesomeTextView) findViewById(R.id.purchase_ticket_ticketAmountTotal);
        mPurchaseTicketButton = (BootstrapButton) findViewById(R.id.purchase_ticket_purchaseTicket_button);

        //wire UI elements
        mEventNameTextView.setText(mEvent.getName());
        mEventLocationTextView.setText(mEvent.getLocation());
        mEventDateTextView.setText(mEvent.getStartDate());
        final String time = mEvent.getTime();
        final String defaults = "Some made up time";
        mEventTimeTextView.setText((time==null)? defaults:time);
        mTicketCountEditText.addTextChangedListener(this);
        mTicketCountEditText.setText("1");
        mTicketTotalAmountTextView.setText(String.valueOf(mEvent.getTicketPrice()));
        mPurchaseTicketButton.setOnClickListener(this);
        mPurchaseTicketButton.setButtonMode(ButtonMode.REGULAR);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //not needed
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int ticketCount;
        try{
            ticketCount = Integer.parseInt(s.toString());
            final double totalPrice = mEvent.getTicketPrice() * ticketCount;
            mTicketTotalAmountTextView.setText(String.valueOf(totalPrice));
        }catch (NumberFormatException e){
            final String text = "Please enter a number in the count field.";
            Toast message = Toast.makeText(this, text, Toast.LENGTH_LONG);
            message.show();
            mTicketTotalAmountTextView.setText("????");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        //not needed
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.purchase_ticket_purchaseTicket_button){
            Log.d(LOG, "clicked purchase ticket");
            purchaseTicket();
        }
    }

    @Override
    public void onTicketPurchased(boolean success) {
        //todo should this lead to an activity giving info on the ticket or just a success message
        final String text;
        if (success)
            text = "Your ticket(s) has been purchased successfully";
        else
            text = "Your ticket(s) could not be purchased successfully";
        Toast message = Toast.makeText(this, text, Toast.LENGTH_LONG );
        message.show();
    }
}
