package com.densoftinfotech.densoftpaysmart.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.DateUtils;
import com.densoftinfotech.densoftpaysmart.classes.NotificationReceived;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mInstance = null;
    public static final String DATABASE_NAME = Constants.db_name;
    static Context mcontext;

    public static final String TABLE_NOTIFICATION = "table_notification";
    public static final String TABLE_ATTENDANCE = "table_attendance";
    public static final String TABLE_FIREBASE_LIVE_LOCATION = "table_firebase_live_location";

    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String BIG_PICTURE = "big_picture";
    public static final String DELETED = "deleted";
    public static final String ID = "id";

    public static final String STAFF_ID = "staff_id";
    public static final String CHECK_IN_TIME = "check_in_time";
    public static final String CHECK_OUT_TIME = "check_out_time";
    public static final String TODAY_DATE = "today_date";
    public static final String SAVEDTIME = "SAVEDTIME";

    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ADDRESS = "address";
    public static final String STAFF_NAME = "staff_name";
    public static final String WORKING_HOUR_FROM = "working_hour_from";
    public static final String WORKING_HOUR_TO = "working_hour_to";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        mcontext = context;
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        mcontext = context;
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_notification = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATION + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE + " TEXT," + DESCRIPTION + " TEXT," + BIG_PICTURE + " TEXT," + DELETED + " INTEGER," + SAVEDTIME + " TEXT)";
        db.execSQL(query_notification);

        String query_attendance = "CREATE TABLE IF NOT EXISTS " + TABLE_ATTENDANCE + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + STAFF_ID + " TEXT," + CHECK_IN_TIME + " TEXT," + CHECK_OUT_TIME + " TEXT," + TODAY_DATE + " TEXT," + SAVEDTIME + " TEXT)";
        db.execSQL(query_attendance);

        String query_liveupdates = "CREATE TABLE IF NOT EXISTS " + TABLE_FIREBASE_LIVE_LOCATION + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + STAFF_ID + " TEXT," + STAFF_NAME + " TEXT," + LATITUDE + " TEXT," + LONGITUDE + " TEXT,"
                + ADDRESS + " TEXT," + WORKING_HOUR_FROM + " TEXT," + WORKING_HOUR_TO + " TEXT," + SAVEDTIME + " TEXT)";
        db.execSQL(query_liveupdates);
    }


    public void createtablenotification() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String query_notification = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATION + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE + " TEXT," + DESCRIPTION + " TEXT," + BIG_PICTURE + " TEXT," + DELETED + " INTEGER," + SAVEDTIME + " TEXT)";
            db.execSQL(query_notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable_forAttendance() {
        try {
            SQLiteDatabase db = getWritableDatabase();

            String query_button = "CREATE TABLE IF NOT EXISTS " + TABLE_ATTENDANCE + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + STAFF_ID + " TEXT," + CHECK_IN_TIME + " TEXT," + CHECK_OUT_TIME + " TEXT," + TODAY_DATE + " TEXT," + SAVEDTIME + " TEXT)";
            db.execSQL(query_button);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createtable_location() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String query_liveupdates = "CREATE TABLE IF NOT EXISTS " + TABLE_FIREBASE_LIVE_LOCATION + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + STAFF_ID + " TEXT," + STAFF_NAME + " TEXT," + LATITUDE + " TEXT," + LONGITUDE + " TEXT,"
                    + ADDRESS + " TEXT," + WORKING_HOUR_FROM + " TEXT," + WORKING_HOUR_TO + " TEXT," + SAVEDTIME + " TEXT)";
            db.execSQL(query_liveupdates);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long savenotificationData(String title, String description, String big_picture) {
        long index = 0;
        try {
            createtablenotification();
            SQLiteDatabase db = getWritableDatabase();
            ContentValues c = new ContentValues();
            c.put(TITLE, title);
            c.put(DESCRIPTION, description);
            c.put(BIG_PICTURE, big_picture);
            c.put(DELETED, 0);

            index = db.insertWithOnConflict(TABLE_NOTIFICATION, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return index;
    }

    public long save_location(ContentValues c) {
        long index = 0;
        try {
            createtable_location();
            SQLiteDatabase db = getWritableDatabase();
            index = db.insertWithOnConflict(TABLE_FIREBASE_LIVE_LOCATION, null, c, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return index;
    }



    public long save_time(String staffid, String checkin, String checkout, String today_date) {
        long index = 0;
        createTable_forAttendance();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c1 = db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCE, null);
        c1.moveToFirst();
        //Log.d("Number of Records", " :: " + c1.getCount());

        if (c1.getCount() == 0) {
            try {
                createTable_forAttendance();

                ContentValues c = new ContentValues();
                c.put(STAFF_ID, staffid);

                if (!checkin.equalsIgnoreCase("0")) {
                    c.put(CHECK_IN_TIME, checkin);
                }
                if (!checkout.equalsIgnoreCase("0")) {
                    c.put(CHECK_OUT_TIME, checkout);
                }
                c.put(TODAY_DATE, today_date);
                c.put(SAVEDTIME, DateUtils.getSqliteTime());

                index = db.insertWithOnConflict(TABLE_ATTENDANCE, null, c, SQLiteDatabase.CONFLICT_REPLACE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            update_time(db, staffid, checkin, checkout, today_date);
        }

        return index;
    }

    public void update_time(SQLiteDatabase db, String staffid, String checkin, String checkout, String today_date) {

        try {

            ContentValues c = new ContentValues();
            c.put(STAFF_ID, staffid);

            if (!checkin.equalsIgnoreCase("0")) {
                c.put(CHECK_IN_TIME, checkin);
            }
            if (!checkout.equalsIgnoreCase("0")) {
                c.put(CHECK_OUT_TIME, checkout);
            }
            c.put(TODAY_DATE, today_date);
            c.put(SAVEDTIME, DateUtils.getSqliteTime());

            db.update(TABLE_ATTENDANCE, c, "STAFF_ID=" + staffid, null);

            /*if (!checkin.equalsIgnoreCase("0")) {
                String query ="UPDATE " + TABLE_BUTTON + " SET " + CHECK_IN_TIME + " = " + checkin + ", "
                        + TODAY_DATE + " = " + today_date + ", " + SAVEDTIME + " = " + DateUtils.getSqliteTime() + " WHERE " + STAFF_ID + " = " + staffid;
                        db.execSQL(query);
            }
            if (!checkout.equalsIgnoreCase("0")) {
                String query = "UPDATE " + TABLE_BUTTON + " SET " + CHECK_OUT_TIME + " = " + checkout + ", "
                        + TODAY_DATE + " = " + today_date + ", " + SAVEDTIME + " = " + DateUtils.getSqliteTime() + " WHERE " + STAFF_ID + " = " + staffid;
                db.execSQL(query);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean allow_check(int status) {
        createTable_forAttendance();
        String today_date = "";
        try {
            SQLiteDatabase db = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_ATTENDANCE;
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();

            if(status == 0){
                if (c.getCount() == 0) {
                    return true;
                }else{
                    return c.getString(2) == null || (c.getString(2).equalsIgnoreCase("0"));
                }
            }else {
                if (c.getCount() == 0) {
                    return true;
                }else{
                    return c.getString(3) == null || (c.getString(3).equalsIgnoreCase("0"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public String check_sqliteDate(){
        createTable_forAttendance();
        String today_date = Constants.today_date;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_ATTENDANCE;
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();

            if(c.getCount()>0)
                today_date = c.getString(4);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return today_date;
    }

    public ArrayList<NotificationReceived> get_NotificationData() {
        createtablenotification();
        ArrayList<NotificationReceived> list = new ArrayList<NotificationReceived>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NOTIFICATION + " WHERE " + DELETED + " =0";
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("id", c.getLong(0));
                        obj.put("title", c.getString(1));
                        obj.put("description", c.getString(2));
                        obj.put("big_picture", c.getString(3));
                        obj.put("deleted", c.getString(4));
                        //Log.d("obj_notification", "obj " + obj.toString());
                        list.add(new NotificationReceived(obj));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        //Log.e("error1", "error1" + e);
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deletebyid(long id) {
        try {
            createtablenotification();
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_NOTIFICATION + " WHERE ID = " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEntry(String table, String staffid)
    {
        String where="id=?";
        SQLiteDatabase db = getWritableDatabase();
        db.delete(table, where, new String[]{staffid}) ;
    }

    public void update_notification() {
        try {
            createtablenotification();
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE " + TABLE_NOTIFICATION +
                    " SET " + DELETED + " =1 WHERE " + DELETED + " =0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
