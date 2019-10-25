package com.densoftinfotech.densoftpaysmart.room_database.Staff;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface StaffDetails_Dao {

        @Query("SELECT * FROM Constants.table_staff_details")
        ArrayList<StaffDetails> getAll();

        @Query("SELECT * FROM Constants.table_staff_details where staff_id LIKE :staff_id AND staff_name LIKE :staff_name")
        StaffDetails findByName(String staff_id, String staff_name);

        @Query("SELECT COUNT(*) from Constants.table_staff_details")
        int countUsers();

        @Insert
        void insertAll(StaffDetails users);

        @Delete
        void delete(StaffDetails user);
}
