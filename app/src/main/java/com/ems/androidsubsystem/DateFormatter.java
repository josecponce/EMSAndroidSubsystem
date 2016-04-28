package com.ems.androidsubsystem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by josecponce on 4/20/16.
 */
public class DateFormatter {
    private SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat localFormat = new SimpleDateFormat("MM-dd-yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss aa");

    public String toLocalFormat(Date date){
        return localFormat.format(date);
    }

    public Date fromLocalFormat(String date) throws ParseException{
        return localFormat.parse(date);
    }

    public String toServerFormat(Date date){
        return serverFormat.format(date);
    }

    public Date fromServerFormat(String date) throws ParseException{
        return serverFormat.parse(date);
    }

    public String fromServerToLocal(String date) throws  ParseException{
        Date dateD  = fromServerFormat(date);
        return toLocalFormat(dateD);
    }

    public String formatTime(String time) throws ParseException {
        return time;
    }
}
