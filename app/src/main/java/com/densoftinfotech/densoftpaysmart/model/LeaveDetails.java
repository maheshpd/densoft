package com.densoftinfotech.densoftpaysmart.model;

import java.io.Serializable;

public class LeaveDetails implements Serializable {

    private String StaffId = "";
    private String PName = "";
    private String Name = "";
    private String Description = "";
    private String LeaveId = "";
    private String LeaveAssign = "";
    private String LeaveTaken = "";
    private String BalanceLeave = "";

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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLeaveId() {
        return LeaveId;
    }

    public void setLeaveId(String leaveId) {
        LeaveId = leaveId;
    }

    public String getLeaveAssign() {
        return LeaveAssign;
    }

    public void setLeaveAssign(String leaveAssign) {
        LeaveAssign = leaveAssign;
    }

    public String getLeaveTaken() {
        return LeaveTaken;
    }

    public void setLeaveTaken(String leaveTaken) {
        LeaveTaken = leaveTaken;
    }

    public String getBalanceLeave() {
        return BalanceLeave;
    }

    public void setBalanceLeave(String balanceLeave) {
        BalanceLeave = balanceLeave;
    }
}
