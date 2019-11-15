package com.densoftinfotech.densoftpaysmart.app_utilities;

import android.text.format.DateFormat;

import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;

import java.util.Calendar;
import java.util.Date;

public class Constants {
    public static String channel_id = "densoft_2000";
    public static String channel_name = "densoftpayroll_2000";
    public static String channel_description = "densoftinfotech_2000";
    public static String ip = "api.densoftinfotech.in";
    //public static String ip = "demo.paysmart.net.in";
    public static String db_name = "densoftpaysmart";
    public static final String table_staff_details = "table_staff_details";
    public static String staffid = "";
    public static String current_year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    public static String current_month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
    public static StaffDetailsRoom staffDetailsRoom = new StaffDetailsRoom();

    public static Date d = new Date();
    public static CharSequence s = DateFormat.format("yyyy-MM-dd", d.getTime());
    public static String today_date = s.toString();
    //public static String today_date = "2019-11-16";
    /*public static CharSequence s1 = DateFormat.format("HH-mm", d.getTime());
    public static String today_time = s1.toString();*/

    public static int count_before_firstpos = 0;

    public static String firebase_database_name = "LiveTracking";

}
