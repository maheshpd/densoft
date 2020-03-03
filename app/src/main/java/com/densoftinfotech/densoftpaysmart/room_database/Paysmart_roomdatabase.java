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

@Database(entities = {StaffDetailsRoom.class}, version = 1, exportSchema = false)
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
            //database.execSQL("ALTER TABLE table_staff_details ADD office_latitude REAL, office_longitude REAL, staff_office_starttime TEXT, staff_office_endtime TEXT" );
            database.execSQL("ALTER TABLE employees" +
                    "  ADD last_name VARCHAR(50)," +
                    "      first_name VARCHAR(40);");
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };*/

    /*static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            *//*database.execSQL("CREATE TABLE table_notification (id INTEGER, "
                    + "`name` TEXT, PRIMARY KEY(`id`))");*//*
        }
    };*/

}
