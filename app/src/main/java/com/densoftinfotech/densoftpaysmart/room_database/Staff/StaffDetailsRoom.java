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
    private String StaffId = "";

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

    public StaffDetailsRoom(String staffId, String PName, String mobile1, String email1, String gender, String joiningDate, String companyName, String branchName,
                            String department, String designation, String StaffPhoto, String DomainUrl) {

        this.StaffId = staffId;
        this.PName = PName;
        this.Mobile1 = mobile1;
        this.Email1 = email1;
        this.Gender = gender;
        this.JoiningDate = joiningDate;
        this.CompanyName = companyName;
        this.BranchName = branchName;
        this.Department = department;
        this.Designation = designation;
        this.StaffPhoto = StaffPhoto;
        this.DomainUrl = DomainUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStaffId() {
        return StaffId;
    }

    public void setStaffId(String staffId) {
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

}
