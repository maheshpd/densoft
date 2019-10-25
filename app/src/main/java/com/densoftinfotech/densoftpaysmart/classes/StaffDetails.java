package com.densoftinfotech.densoftpaysmart.classes;

import com.google.gson.annotations.SerializedName;

public class StaffDetails {
    @SerializedName("StaffId")
    private String StaffId = "";

    @SerializedName("PName")
    private String PName = "";

    @SerializedName("Mobile1")
    private String Mobile1 = "";

    @SerializedName("Email1")
    private String Email1 = "";

    @SerializedName("DOB")
    private String DOB = "";

    @SerializedName("Gender")
    private String Gender = "";

    @SerializedName("JoiningDate")
    private String JoiningDate = "";

    @SerializedName("CompanyName")
    private String CompanyName = "";

    @SerializedName("BranchName")
    private String BranchName = "";

    @SerializedName("Department")
    private String Department = "";

    @SerializedName("Designation")
    private String Designation = "";

    @SerializedName("JobCategory")
    private String JobCategory = "";

    @SerializedName("RuleTemplate")
    private String RuleTemplate = "";

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

    public String getJobCategory() {
        return JobCategory;
    }

    public void setJobCategory(String jobCategory) {
        JobCategory = jobCategory;
    }

    public String getRuleTemplate() {
        return RuleTemplate;
    }

    public void setRuleTemplate(String ruleTemplate) {
        RuleTemplate = ruleTemplate;
    }
}
