package com.ems.androidsubsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.ButtonMode;
import com.ems.DataSubsystem.Event;

import java.util.Calendar;

public class MakePaymentActivity extends AppCompatActivity implements AndroidAPIProxy.onTicketPurchased{

    public static final String LOG = "MakePaymentActivity";

    private AwesomeTextView mEventNameTextView;
    private AwesomeTextView mTicketCountTextView;
    private AwesomeTextView mTicketAmountTotalTextView;
    private BootstrapEditText mCardNumberEditText;
    private BootstrapEditText mSecurityCodeEditText;
    private BootstrapEditText mExpirationMonthEditText;
    private BootstrapEditText mExpirationYearEditText;
    private BootstrapButton mPayNowButton;

    public static String getLOG() {
        return LOG;
    }

    public AwesomeTextView getmEventNameTextView() {
        return mEventNameTextView;
    }

    public void setmEventNameTextView(AwesomeTextView mEventNameTextView) {
        this.mEventNameTextView = mEventNameTextView;
    }

    public AwesomeTextView getmTicketCountTextView() {
        return mTicketCountTextView;
    }

    public void setmTicketCountTextView(AwesomeTextView mTicketCountTextView) {
        this.mTicketCountTextView = mTicketCountTextView;
    }

    public AwesomeTextView getmTicketAmountTotalTextView() {
        return mTicketAmountTotalTextView;
    }

    public void setmTicketAmountTotalTextView(AwesomeTextView mTicketAmountTotalTextView) {
        this.mTicketAmountTotalTextView = mTicketAmountTotalTextView;
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

    public BootstrapButton getmPayNowButton() {
        return mPayNowButton;
    }

    public void setmPayNowButton(BootstrapButton mPayNowButton) {
        this.mPayNowButton = mPayNowButton;
    }

    public Event getmEvent() {
        return mEvent;
    }

    public void setmEvent(Event mEvent) {
        this.mEvent = mEvent;
    }

    public int getmTicketCount() {
        return mTicketCount;
    }

    public void setmTicketCount(int mTicketCount) {
        this.mTicketCount = mTicketCount;
    }

    public AndroidAPIProxy getProxy() {
        return proxy;
    }

    public void setProxy(AndroidAPIProxy proxy) {
        this.proxy = proxy;
    }

    private Event mEvent;
    private int mTicketCount;
    private AndroidAPIProxy proxy = new AndroidAPIProxy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        final Intent intent = getIntent();
        mEvent = (Event) intent.getSerializableExtra(EventsActivity.EXTRA_EVENT);
        mTicketCount = intent.getIntExtra(PurchaseTicketActivity.EXTRA_TICKET_COUNT,0);
        wireUI();
    }

    private void wireUI(){
    //    mEventNameTextView = (AwesomeTextView) findViewById(R.id.make_payment_eventName);
    //    mTicketCountTextView = (AwesomeTextView) findViewById(R.id.make_payment_ticketCount);
    //    mTicketAmountTotalTextView = (AwesomeTextView) findViewById(R.id.make_payment_ticketTotal);
     //   mCardNumberEditText = (BootstrapEditText) findViewById(R.id.make_payment_cardNumber);
    //    mSecurityCodeEditText = (BootstrapEditText) findViewById(R.id.make_payment_securityCode);
     //   mExpirationMonthEditText = (BootstrapEditText) findViewById(R.id.make_payment_expirationMonth);
    //    mExpirationYearEditText = (BootstrapEditText) findViewById(R.id.make_payment_expirationYear);
     //   mPayNowButton = (BootstrapButton) findViewById(R.id.make_payment_makePayment_button);

        mEventNameTextView.setText(mEvent.getName());
        mTicketCountTextView.setText(String.valueOf(mTicketCount));
        final double totalAmount = mTicketCount * mEvent.getTicketPrice();
        mTicketAmountTotalTextView.setText(String.valueOf(totalAmount));
        mCardNumberEditText.setText("");
        mSecurityCodeEditText.setText("");
        mExpirationMonthEditText.setText("");
        mExpirationYearEditText.setText("");
        mPayNowButton.setButtonMode(ButtonMode.REGULAR);
        mPayNowButton.setOnClickListener(new View.OnClickListener() {
            private String cardNumber;
            private String securityCode;
            private int expirationMonth;
            private int expirationYear;

            @Override
            public void onClick(View v) {
                //validate the input
                Log.d(LOG, "Before input validated");
                if(validateInput()) {
                    Log.d(LOG, "Input validated");
                    //go to the proxy to pay(invoke remote api)
                   // proxy.purchaseTicket(MakePaymentActivity.this,mEvent, mTicketCount);
                    //proxy will trigger a callback when the ticket purchase has been processed
                }
            }

            private boolean validateInput(){
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
                Toast toast = Toast.makeText(MakePaymentActivity.this,message,Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    @Override
    public void onTicketPurchased(boolean success) {
        String message = "Purchase of " +mTicketCount + " for event: \""+ mEvent.getName()+ "\" was ";
        message+= success ? "successful" : "not successful.";

        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
