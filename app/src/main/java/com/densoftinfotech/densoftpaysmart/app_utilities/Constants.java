package com.densoftinfotech.densoftpaysmart.app_utilities;

import android.text.format.DateFormat;

import com.densoftinfotech.densoftpaysmart.model.StaffDetails;

import java.util.Calendar;
import java.util.Date;

public class Constants {
    public static String channel_id = "densoft_2000";
    public static String channel_name = "densoftpayroll_2000";
    public static String channel_description = "densoftinfotech_2000";
    public static String ip = "api.densoftinfotech.in";
    //public static String ip = "demo.paysmart.net.in";
    //public static String ip = "physio.paysmart.net.in";
    public static String db_name = "densoftpaysmart.db";
    public static final String table_staff_details = "table_staff_details";
    public static int staffid = 0;
    public static String current_year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    public static String current_month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
    //public static StaffDetailsRoom staffDetailsRoom = new StaffDetailsRoom();
    public static StaffDetails staffDetails = new StaffDetails();

    public static Date d = new Date();
    public static CharSequence s = DateFormat.format("yyyy-MM-dd", d.getTime());
    public static String today_date = s.toString();
    //public static String today_date = "2019-11-16";
    /*public static CharSequence s1 = DateFormat.format("HH-mm", d.getTime());
    public static String today_time = s1.toString();*/

    public static int count_before_firstpos = 0;

    public static String firebase_database_name = "revo-educare";
    public static long LOCATION_INTERVAL = 10000;
    public static long FASTEST_LOCATION_INTERVAL = 5000;

    //public static final double current_loc_latitude = 19.0175853;
    //public static final double current_loc_longitude = 72.830392;

    public static final double current_loc_latitude = 21.213530;
    public static final double current_loc_longitude = 79.194070;


    public static String delete_constant_staffid = "";

}
