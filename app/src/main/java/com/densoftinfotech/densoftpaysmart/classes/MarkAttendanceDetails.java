package com.densoftinfotech.densoftpaysmart.classes;

public class MarkAttendanceDetails {
    String staffid = "";
    String ThumbDate = "";
    String ThumbTime = "";

    public MarkAttendanceDetails(String ThumbDate, String ThumbTime){
        this.ThumbDate = ThumbDate;
        this.ThumbTime = ThumbTime;
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public String getThumbDate() {
        return ThumbDate;
    }

    public void setThumbDate(String thumbDate) {
        ThumbDate = thumbDate;
    }

    public String getThumbTime() {
        return ThumbTime;
    }

    public void setThumbTime(String thumbTime) {
        ThumbTime = thumbTime;
    }
}
