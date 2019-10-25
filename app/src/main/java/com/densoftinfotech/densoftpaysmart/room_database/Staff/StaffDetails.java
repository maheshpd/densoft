package com.densoftinfotech.densoftpaysmart.room_database.Staff;

import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = Constants.table_staff_details)
public class StaffDetails {

    @PrimaryKey(autoGenerate =  true)
    private int id;

    @ColumnInfo(name = "staff_id")
    private String Patientid;

    @ColumnInfo(name = "staff_name")
    private String PName;

    @ColumnInfo(name = "staff_mobileno")
    private String Mobile1;

    @ColumnInfo(name = "staff_email")
    private String Email1;

    private String DOB;

    @ColumnInfo(name = "staff_Gender")
    private String Gender;

    @ColumnInfo(name = "staff_JoiningDate")
    private String JoiningDate;


    public String getPatientid() {
        return Patientid;
    }

    public void setPatientid(String patientid) {
        Patientid = patientid;
    }

    public String getPName() {
        return PName;
    }

    public void setPName(String PName) {
        this.PName = PName;
    }

    public String getMobile1() {
        return Mobile1;
    }

    public void setMobile1(String mobile1) {
        Mobile1 = mobile1;
    }

    public String getEmail1() {
        return Email1;
    }

    public void setEmail1(String email1) {
        Email1 = email1;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getJoiningDate() {
        return JoiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        JoiningDate = joiningDate;
    }
}
