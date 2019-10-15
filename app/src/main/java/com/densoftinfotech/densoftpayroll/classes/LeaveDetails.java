package com.densoftinfotech.densoftpayroll.classes;

import android.os.Parcelable;

import java.io.Serializable;

public class LeaveDetails implements Serializable {
    int days = 0;
    String name_of_leave = "";

    public LeaveDetails(int days, String name_of_leave){
        this.days = days;
        this.name_of_leave = name_of_leave;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getName_of_leave() {
        return name_of_leave;
    }

    public void setName_of_leave(String name_of_leave) {
        this.name_of_leave = name_of_leave;
    }
}
