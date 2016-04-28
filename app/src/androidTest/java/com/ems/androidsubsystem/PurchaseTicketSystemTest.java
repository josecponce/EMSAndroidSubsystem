package com.ems.androidsubsystem;

import android.support.v7.widget.ListViewCompat;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.robotium.solo.Solo;

import java.util.Date;


/**
 * Created by josecponce on 4/27/16.
 */
public class PurchaseTicketSystemTest extends ActivityInstrumentationTestCase2<LandingActivity>{

    public static final String NOT_SUCCESSFUL = "Your ticket(s) could not be purchased successfully";
    public static final String SUCCESSFUL = "Your ticket(s) has been purchased successfully";
    public static final String EXPIRED_CARD = "The card seems to be expired. Please check the expiration date and try again";
    public static final String INVALID_SEC_CODE = "The security code should be either 3 or 4 digits long.";
    private Solo solo;

    public PurchaseTicketSystemTest() {
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
     * This test goes to the purchase ticket page for the first event in the list,
     * sets some valid input and then proceeds to click purchase ticket button,
     * this will result in a successful operation Toast being displayed
     * @throws Exception
     */
    public void testPurchaseTicketSuccess() throws Exception{
        //scroll the events carousel in the landing page
        solo.scrollToSide(Solo.RIGHT);

        //go to eventsList page
        solo.clickOnText("Events List");

        solo.clickInList(0);

        //go to purchase ticket page
        solo.clickOnText("Purchase Ticket");

        //enter valid information on the purchase ticket page
        solo.clearEditText(0);
        solo.enterText(0,"1");

        solo.enterText(1,"pepote5391@gmail.com");
        solo.enterText(2, "1111222233334444");
        solo.enterText(3, "111");
        solo.enterText(4, "11");
        solo.enterText(5, "2018");

        //submit ticket purchase
        solo.clickOnText("Purchase Ticket");

        //wait up to a second for the toast
        Date date = new Date();
        TextView toast;
        long elapsed;
        do{
            elapsed = new Date().getTime() - date.getTime();
            toast = (TextView)solo.getView(android.R.id.message);
        }while(elapsed<1000&&toast!=null);

        //check that the toast shown has the Success message in it
        assertEquals(toast.getText().toString(), SUCCESSFUL);
    }

    /**
     * This test goes to the purchase ticket page for the first event in the list,
     * sets some valid input and then proceeds to click purchase ticket button,
     * this will result in a successful operation Toast being displayed
     * @throws Exception
     */
    public void testPurchaseTicketPastDate() throws Exception{
        //go to eventsList page
        solo.clickOnText("Events List");

        solo.clickInList(1);

        //go to purchase ticket page
        solo.clickOnText("Purchase Ticket");

        //enter valid information on the purchase ticket page
        solo.clearEditText(0);
        solo.enterText(0,"1");

        solo.enterText(1,"pepote5391@gmail.com");
        solo.enterText(2, "1111222233334444");
        solo.enterText(3, "111");
        solo.enterText(4, "11");
        solo.enterText(5, "2011");

        //submit ticket purchase
        solo.clickOnText("Purchase Ticket");

        //wait up to a second for the toast
        Date date = new Date();
        TextView toast;
        long elapsed;
        do{
            elapsed = new Date().getTime() - date.getTime();
            toast = (TextView)solo.getView(android.R.id.message);
        }while(elapsed<1000&&toast!=null);

        //check that the toast shown has the Success message in it
        assertEquals(toast.getText().toString(), EXPIRED_CARD);
    }

    /**
     * This test goes to the purchase ticket page for the first event in the list,
     * sets some valid input and then proceeds to click purchase ticket button,
     * this will result in a successful operation Toast being displayed
     * @throws Exception
     */
    public void testPurchaseTicketInvalidSecCode() throws Exception{
        //go to eventsList page
        solo.clickOnText("Events List");

        solo.clickInList(1);

        //go to purchase ticket page
        solo.clickOnText("Purchase Ticket");

        //enter valid information on the purchase ticket page
        solo.clearEditText(0);
        solo.enterText(0,"1");

        solo.enterText(1,"pepote5391@gmail.com");
        solo.enterText(2, "1111222233334444");
        solo.enterText(3, "11221");
        solo.enterText(4, "11");
        solo.enterText(5, "2018");

        //submit ticket purchase
        solo.clickOnText("Purchase Ticket");

        //wait up to a second for the toast
        Date date = new Date();
        TextView toast;
        long elapsed;
        do{
            elapsed = new Date().getTime() - date.getTime();
            toast = (TextView)solo.getView(android.R.id.message);
        }while(elapsed<1000&&toast!=null);

        //check that the toast shown has the Success message in it
        assertEquals(toast.getText().toString(), INVALID_SEC_CODE);
    }
}
