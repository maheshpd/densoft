package com.densoftinfotech.densoftpaysmart.classes;

import androidx.annotation.NonNull;

public class CalendarDetails {

    String description = "";
    int date = 0;

    public CalendarDetails(String status, String description) {

        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    String StaffId = "";
    String CDate = "";
    String InTime = "";
    String OutTime = "";
    String WorkingHour = "";
    String LateBy = "";
    String HalfDay = "";
    String OverTime = "";
    String Presenty = "";
    String WeekOff = "";
    String HoliDayName = "";
    String LeaveName = "";
    String ShiftName = "";
    String Status = "";


    public String getStaffId() {
        return StaffId;
    }

    public void setStaffId(String staffId) {
        StaffId = staffId;
    }

    public String getCDate() {
        return CDate;
    }

    public void setCDate(String CDate) {
        this.CDate = CDate;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public String getWorkingHour() {
        return WorkingHour;
    }

    public void setWorkingHour(String workingHour) {
        WorkingHour = workingHour;
    }

    public String getLateBy() {
        return LateBy;
    }

    public void setLateBy(String lateBy) {
        LateBy = lateBy;
    }

    public String getHalfDay() {
        return HalfDay;
    }

    public void setHalfDay(String halfDay) {
        HalfDay = halfDay;
    }

    public String getOverTime() {
        return OverTime;
    }

    public void setOverTime(String overTime) {
        OverTime = overTime;
    }

    public String getPresenty() {
        return Presenty;
    }

    public void setPresenty(String presenty) {
        Presenty = presenty;
    }

    public String getWeekOff() {
        return WeekOff;
    }

    public void setWeekOff(String weekOff) {
        WeekOff = weekOff;
    }

    public String getHoliDayName() {
        return HoliDayName;
    }

    public void setHoliDayName(String holiDayName) {
        HoliDayName = holiDayName;
    }

    public String getLeaveName() {
        return LeaveName;
    }

    public void setLeaveName(String leaveName) {
        LeaveName = leaveName;
    }

    public String getShiftName() {
        return ShiftName;
    }

    public void setShiftName(String shiftName) {
        ShiftName = shiftName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
