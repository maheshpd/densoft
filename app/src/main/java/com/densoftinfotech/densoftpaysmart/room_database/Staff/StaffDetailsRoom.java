package com.densoftinfotech.densoftpaysmart.room_database.Staff;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_staff_details")
public class StaffDetailsRoom {

    public StaffDetailsRoom() {
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "staff_id")
    private int StaffId = 0;

    @ColumnInfo(name = "staff_name")
    private String PName = "";

    @ColumnInfo(name = "staff_mobileno")
    private String Mobile1 = "";

    @ColumnInfo(name = "staff_email")
    private String Email1 = "";

    @ColumnInfo(name = "staff_Gender")
    private String Gender = "";

    @ColumnInfo(name = "staff_JoiningDate")
    private String JoiningDate = "";

    @ColumnInfo(name = "staff_companyname")
    private String CompanyName = "";

    @ColumnInfo(name = "staff_branchname")
    private String BranchName = "";

    @ColumnInfo(name = "staff_department")
    private String Department = "";

    @ColumnInfo(name = "staff_designation")
    private String Designation = "";

    @ColumnInfo(name = "staff_profileimage")
    private String StaffPhoto = "";

    @ColumnInfo(name = "staff_domainurl")
    private String DomainUrl = "";

    @ColumnInfo(name = "office_latitude")
    private double Latitude = 0;

    @ColumnInfo(name = "office_longitude")
    private double Longitude = 0;

    @ColumnInfo(name = "staff_office_starttime")
    private String OfficeStartTime = "9:00"; //default time 9:00 am

    @ColumnInfo(name = "staff_office_endtime")
    private String OfficeEndTime = "18:00"; //default time 6:00 pm


    public StaffDetailsRoom(int staffId, String pName, String mobile1, String email1, String gender, String joiningDate,
                            String companyName, String branchName, String department, String designation, String staffPhoto,
                            String domainUrl, double latitude, double longitude, String officeStartTime, String officeEndTime) {
        this.id = id;
        StaffId = staffId;
        PName = pName;
        Mobile1 = mobile1;
        Email1 = email1;
        Gender = gender;
        JoiningDate = joiningDate;
        CompanyName = companyName;
        BranchName = branchName;
        Department = department;
        Designation = designation;
        StaffPhoto = staffPhoto;
        DomainUrl = domainUrl;
        Latitude = latitude;
        Longitude = longitude;
        OfficeStartTime = officeStartTime;
        OfficeEndTime = officeEndTime;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStaffId() {
        return StaffId;
    }

    public void setStaffId(int staffId) {
        StaffId = staffId;
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

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getStaffPhoto() {
        return StaffPhoto;
    }

    public void setStaffPhoto(String staffPhoto) {
        StaffPhoto = staffPhoto;
    }


    public String getDomainUrl() {
        return DomainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        DomainUrl = domainUrl;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getOfficeStartTime() {
        return OfficeStartTime;
    }

    public void setOfficeStartTime(String officeStartTime) {
        OfficeStartTime = officeStartTime;
    }

    public String getOfficeEndTime() {
        return OfficeEndTime;
    }

    public void setOfficeEndTime(String officeEndTime) {
        OfficeEndTime = officeEndTime;
    }
}
