package com.ems.androidsubsystem;

import android.content.Intent;
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
import com.ems.DataSubsystem.Payment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PurchaseTicketActivity extends AppCompatActivity implements View.OnClickListener, AndroidAPIProxy.onTicketPurchased {

    public static final String LOG = "PurchaseTicketActivity";
    public static final String EXTRA_TICKET_COUNT = "ticketCount";

    private AwesomeTextView mEventNameTextView;
    private BootstrapEditText mTicketCountEditText;
    private AwesomeTextView mTicketRate;
    private AwesomeTextView mTicketTotalAmountTextView;
    private BootstrapButton mPurchaseTicketButton;

    private BootstrapEditText mCardNumberEditText;
    private BootstrapEditText mSecurityCodeEditText;
    private BootstrapEditText mExpirationMonthEditText;
    private BootstrapEditText mExpirationYearEditText;
    private BootstrapEditText mEmail;

    public static String getLOG() {
        return LOG;
    }

    public static String getExtraTicketCount() {
        return EXTRA_TICKET_COUNT;
    }

    public AwesomeTextView getmEventNameTextView() {
        return mEventNameTextView;
    }

    public void setmEventNameTextView(AwesomeTextView mEventNameTextView) {
        this.mEventNameTextView = mEventNameTextView;
    }

    public BootstrapEditText getmTicketCountEditText() {
        return mTicketCountEditText;
    }

    public void setmTicketCountEditText(BootstrapEditText mTicketCountEditText) {
        this.mTicketCountEditText = mTicketCountEditText;
    }

    public AwesomeTextView getmTicketRate() {
        return mTicketRate;
    }

    public void setmTicketRate(AwesomeTextView mTicketRate) {
        this.mTicketRate = mTicketRate;
    }

    public AwesomeTextView getmTicketTotalAmountTextView() {
        return mTicketTotalAmountTextView;
    }

    public void setmTicketTotalAmountTextView(AwesomeTextView mTicketTotalAmountTextView) {
        this.mTicketTotalAmountTextView = mTicketTotalAmountTextView;
    }

    public BootstrapButton getmPurchaseTicketButton() {
        return mPurchaseTicketButton;
    }

    public void setmPurchaseTicketButton(BootstrapButton mPurchaseTicketButton) {
        this.mPurchaseTicketButton = mPurchaseTicketButton;
    }

    public BootstrapEditText getmCardNumberEditText() {
        return mCardNumberEditText;
    }

    public void setmCardNumberEditText(BootstrapEditText mCardNumberEditText) {
        this.mCardNumberEditText = mCardNumberEditText;
    }

    public BootstrapEditText getmSecurityCodeEditText() {
        return mSecurityCodeEditText;
    }

    public void setmSecurityCodeEditText(BootstrapEditText mSecurityCodeEditText) {
        this.mSecurityCodeEditText = mSecurityCodeEditText;
    }

    public BootstrapEditText getmExpirationMonthEditText() {
        return mExpirationMonthEditText;
    }

    public void setmExpirationMonthEditText(BootstrapEditText mExpirationMonthEditText) {
        this.mExpirationMonthEditText = mExpirationMonthEditText;
    }

    public BootstrapEditText getmExpirationYearEditText() {
        return mExpirationYearEditText;
    }

    public void setmExpirationYearEditText(BootstrapEditText mExpirationYearEditText) {
        this.mExpirationYearEditText = mExpirationYearEditText;
    }

    public BootstrapEditText getmEmail() {
        return mEmail;
    }

    public void setmEmail(BootstrapEditText mEmail) {
        this.mEmail = mEmail;
    }

    public Event getmEvent() {
        return mEvent;
    }

    public void setmEvent(Event mEvent) {
        this.mEvent = mEvent;
    }

    public AndroidAPIProxy getmProxy() {
        return mProxy;
    }

    public void setmProxy(AndroidAPIProxy mProxy) {
        this.mProxy = mProxy;
    }

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

        int count;
        try{//ensure the ticket count field actually contains a number
            count = Integer.parseInt(mTicketCountEditText.getText().toString());
        }catch (NumberFormatException e){//if there is not a number in the count field
            final String text = "Please enter a valid integer in the count field";
            Toast message = Toast.makeText(this, text, Toast.LENGTH_LONG);
            message.show();
            return;
        }
        Payment payment = new Payment();
        payment.setAmount(Double.parseDouble(mTicketTotalAmountTextView.getText().toString()));
        payment.setCreditCardExpirationDate(mExpirationYearEditText.getText()+"-"+mExpirationMonthEditText.getText()+"-00");
        payment.setCreditCardNumber(mCardNumberEditText.getText().toString());
        payment.setDate(new Date());
        payment.setEmail(mEmail.getText().toString());

        Log.d(LOG, "Before input validated");
        if(validateInput()) {
            Log.d(LOG, "Input validated");
            //go to the proxy to pay(invoke remote api)
            mProxy.purchaseTicket(PurchaseTicketActivity.this,mEvent, count, payment);
            //proxy will trigger a callback when the ticket purchase has been processed
        }
    }

    private boolean validateInput(){
        String cardNumber;
        String securityCode;
        int expirationMonth;
        int expirationYear;
        cardNumber = mCardNumberEditText.getText().toString();
        if (cardNumber.length()!=16) {
            showError("Credit card number should be 16 digits long.");
            return false;
        }

        securityCode = mSecurityCodeEditText.getText().toString();
        if (securityCode.length()<3||securityCode.length()>4){
            showError("The security code should be either 3 or 4 digits long.");
            return false;
        }


        final String month = mExpirationMonthEditText.getText().toString();
        expirationMonth = Integer.parseInt(month);
        if (expirationMonth<1||expirationMonth>12){
            showError("Month has to be between 1-12");
            return false;
        }

        final String year =mExpirationYearEditText.getText().toString();
        final Calendar now = Calendar.getInstance();
        expirationYear = Integer.parseInt(year);
        if (expirationYear< now.get(Calendar.YEAR)||
                (expirationYear== now.get(Calendar.YEAR))&&
                        expirationMonth<now.get(Calendar.MONTH)){
            showError("The card seems to be expired. Please check the expiration date and try again");
            return false;
        }
        return true;
    }

    private void showError(String message){
        Toast toast = Toast.makeText(PurchaseTicketActivity.this,message,Toast.LENGTH_LONG);
        toast.show();
    }

    private void wireUIElements() {

        //get handle of UI elements
        mEventNameTextView = (AwesomeTextView) findViewById(R.id.purchase_ticket_eventName);
        mTicketCountEditText = (BootstrapEditText) findViewById(R.id.purchase_ticket_ticketCount);
        mTicketTotalAmountTextView = (AwesomeTextView) findViewById(R.id.purchase_ticket_ticketAmountTotal);
        mPurchaseTicketButton = (BootstrapButton) findViewById(R.id.purchase_ticket_purchaseTicket_button);
        mTicketRate = (AwesomeTextView) findViewById(R.id.purchase_ticket_ticketRate);
        mCardNumberEditText = (BootstrapEditText) findViewById(R.id.make_payment_cardNumber);
        mSecurityCodeEditText = (BootstrapEditText) findViewById(R.id.make_payment_securityCode);
        mExpirationMonthEditText = (BootstrapEditText) findViewById(R.id.make_payment_expirationMonth);
        mExpirationYearEditText = (BootstrapEditText) findViewById(R.id.make_payment_expirationYear);
        //eliminate repeated ids from make payment layout

        mEmail = (BootstrapEditText) findViewById(R.id.purchase_ticket_email);

        //wire UI elements
        mEventNameTextView.setText(mEvent.getName());
        mTicketCountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int ticketCount;
                try{
                    ticketCount = Integer.parseInt(s.toString());
                    Log.d(LOG, ticketCount + " tickets at " + mEvent.getTicketPrice());
                    final double totalPrice = mEvent.getTicketPrice() * ticketCount;
                    mTicketTotalAmountTextView.setText(String.valueOf(totalPrice));
                }catch (NumberFormatException e){
                    final String text = "Please enter a number in the count field.";
                    Toast message = Toast.makeText(PurchaseTicketActivity.this, text, Toast.LENGTH_LONG);
                    message.show();
                    mTicketTotalAmountTextView.setText("????");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTicketRate.setText(String.valueOf(mEvent.getTicketPrice()));
        mTicketCountEditText.setText("1");
        mTicketTotalAmountTextView.setText(String.valueOf(mEvent.getTicketPrice()));
        mPurchaseTicketButton.setOnClickListener(this);
        mPurchaseTicketButton.setButtonMode(ButtonMode.REGULAR);

        mCardNumberEditText.setText("");
        mSecurityCodeEditText.setText("");
        mExpirationMonthEditText.setText("");
        mExpirationYearEditText.setText("");
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
