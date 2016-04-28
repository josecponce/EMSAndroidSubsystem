package com.ems.androidsubsystem;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.ButtonMode;
import com.ems.DataSubsystem.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(AndroidJUnit4.class)
public class PurchaseTicketActivityTest {
    @Rule
    public final ActivityTestRule<PurchaseTicketActivity> activityRule =
            new ActivityTestRule<>(PurchaseTicketActivity.class,false,false);
    public PurchaseTicketActivity activity;
    private DateFormatter dateFormatter = new DateFormatter();
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
    Event mEvent;

    @After
    public void tearDown() throws Exception{

    }

    @Before
    public void setUp() throws Exception{

        mEvent = new Event("testName", "2017-11-11 11:11:11","testLocation", "testCategory", 100.00);
        mEvent.setTime("2017-11-11 14:20:00");
        mEvent.setEndDate("2017-11-11 11:11:11");

        Intent i= new Intent();
        i.putExtra(EventsActivity.EXTRA_EVENT, mEvent);
        activity = activityRule.launchActivity(i);


        mEventNameTextView = (AwesomeTextView) activity.findViewById(R.id.purchase_ticket_eventName);
        mTicketCountEditText = (BootstrapEditText) activity.findViewById(R.id.purchase_ticket_ticketCount);
        mTicketTotalAmountTextView = (AwesomeTextView) activity.findViewById(R.id.purchase_ticket_ticketAmountTotal);
        mPurchaseTicketButton = (BootstrapButton) activity.findViewById(R.id.purchase_ticket_purchaseTicket_button);
        mTicketRate = (AwesomeTextView) activity.findViewById(R.id.purchase_ticket_ticketRate);
        mCardNumberEditText = (BootstrapEditText) activity.findViewById(R.id.make_payment_cardNumber);
        mSecurityCodeEditText = (BootstrapEditText) activity.findViewById(R.id.make_payment_securityCode);
        mExpirationMonthEditText = (BootstrapEditText) activity.findViewById(R.id.make_payment_expirationMonth);
        mExpirationYearEditText = (BootstrapEditText) activity.findViewById(R.id.make_payment_expirationYear);
        //eliminate repeated ids from make payment layout

        mEmail = (BootstrapEditText) activity.findViewById(R.id.purchase_ticket_email);
    }

    @Test
    public void wireUIElements() throws Exception {
        //wire UI elements
        assertEquals(mEvent.getName(), mEventNameTextView.getText().toString());
        assertEquals("1",mTicketCountEditText.getText().toString());
        assertEquals(mEvent.getTicketPrice(),Double.parseDouble(mTicketRate.getText().toString()),0.01);
        assertEquals(mEvent.getTicketPrice(),Double.parseDouble(mTicketTotalAmountTextView.getText().toString()),0.01);
        assertTrue(mPurchaseTicketButton.hasOnClickListeners());
        assertEquals(ButtonMode.REGULAR, mPurchaseTicketButton.getButtonMode());
        assertEquals("",mCardNumberEditText.getText().toString());
        assertEquals("",mSecurityCodeEditText.getText().toString());
        assertEquals("",mExpirationMonthEditText.getText().toString());
        assertEquals("",mExpirationYearEditText.getText().toString());
    }
}