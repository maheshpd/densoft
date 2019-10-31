package com.densoftinfotech.densoftpaysmart.room_database.Staff;



import java.util.ArrayList;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface StaffDetails_Dao {

        @Query("SELECT * FROM table_staff_details")
        StaffDetailsRoom getAll();

        @Query("SELECT * FROM table_staff_details where staff_id LIKE :staff_id AND staff_name LIKE :staff_name")
        StaffDetailsRoom findByName(String staff_id, String staff_name);

        @Query("SELECT COUNT(*) from table_staff_details")
        int countUsers();

        @Insert
        void insertAll(StaffDetailsRoom users);

        @Delete
        void delete(StaffDetailsRoom user);

}
