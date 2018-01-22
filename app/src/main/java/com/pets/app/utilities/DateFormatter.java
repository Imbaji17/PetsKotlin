package com.pets.app.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public class DateFormatter {

    public static final SimpleDateFormat h_mm_a = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    public static final SimpleDateFormat EE_MMM_dd_hh_mm_a = new SimpleDateFormat("EE, MMM dd, hh:mm a", Locale.ENGLISH);
    public static final SimpleDateFormat MMMM_dd_yyyy = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat MMMM_dd = new SimpleDateFormat("MMMM d", Locale.ENGLISH);
    public static final SimpleDateFormat dd_MM_yyyy = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat yyyy_MM_dd_hh_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    public static final SimpleDateFormat dd_MMM_yyyy = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static final String yyyy_mm_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String REVIEW_DATE_FORMAT = "dd/MM/yyyy";
    public static final String dd_MMM_yyyy_str = "dd MMM, yyyy";
    public static final String dd_MM_yyyy_str = "dd-MM-yyyy";
    public static final String yyyy_MM_dd_str = "yyyy-MM-dd";

    private static final String TAG = DateFormatter.class.getName();

    public static String getFormattedDate(String inFormat, String strDate, String outFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(inFormat, Locale.ENGLISH);
        try {
            Date date = sdf.parse(strDate);
            sdf = new SimpleDateFormat(outFormat, Locale.ENGLISH);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return " ";
        }
    }

    public static String getUTCtoLocalTime(String strDate, String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String strFormattedDate = "";
        try {
            Date myDate = simpleDateFormat.parse(strDate);
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            strFormattedDate = simpleDateFormat.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strFormattedDate;
    }

    public static Date getDate(String format, String strClosingDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat sdf1 = new SimpleDateFormat(format, Locale.ENGLISH);
        sdf1.setTimeZone(TimeZone.getDefault());

        try {
            Date d1 = sdf.parse(strClosingDate);
            String s1 = sdf1.format(d1);
            Date d2 = sdf1.parse(s1);
//            Date date = sdf1.parse(sdf1.format(sdf.parse(strClosingDate)));
            return d2;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getDaysDifference(Date lateDate, Date earlyDate) {
        Calendar thatDay = Calendar.getInstance();
        thatDay.setTime(lateDate);
        Calendar calCreatedDate = Calendar.getInstance();
        calCreatedDate.setTime(earlyDate);
        long diff = thatDay.getTimeInMillis() - calCreatedDate.getTimeInMillis(); //result in millis
        return diff;
    }
}
