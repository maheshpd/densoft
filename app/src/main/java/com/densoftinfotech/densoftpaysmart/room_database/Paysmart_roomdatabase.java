package com.densoftinfotech.densoftpaysmart.room_database;

import android.content.Context;

import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetails_Dao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {StaffDetailsRoom.class}, version = 1)
public abstract class Paysmart_roomdatabase extends RoomDatabase {

    private static Paysmart_roomdatabase INSTANCE;
    public abstract StaffDetails_Dao staffDetails_dao();

    public static Paysmart_roomdatabase get_PaysmartDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Paysmart_roomdatabase.class, Constants.db_name).build();
            //INSTANCE =Room.databaseBuilder(context.getApplicationContext(), Paysmart_roomdatabase.class, MapConstants.db_name).addMigrations(MIGRATION_1_2).build();
        }
        return INSTANCE;
    }

    public static void destroy_instance(){
        INSTANCE = null;
    }

    /*static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            *//*database.execSQL("CREATE TABLE table_notification (id INTEGER, "
                    + "`name` TEXT, PRIMARY KEY(`id`))");*//*
        }
    };*/

}
