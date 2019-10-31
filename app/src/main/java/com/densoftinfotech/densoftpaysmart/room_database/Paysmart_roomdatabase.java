package com.densoftinfotech.densoftpaysmart.room_database;

import android.content.Context;

import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetails_Dao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {StaffDetailsRoom.class}, version = 1)
public abstract class Paysmart_roomdatabase extends RoomDatabase {

    private static Paysmart_roomdatabase INSTANCE;
    public abstract StaffDetails_Dao staffDetails_dao();


    public static Paysmart_roomdatabase get_PaysmartDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Paysmart_roomdatabase.class, Constants.db_name).build();
        }
        return INSTANCE;
    }

    public static void destroy_instance(){
        INSTANCE = null;
    }

}
