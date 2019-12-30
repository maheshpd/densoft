package com.densoftinfotech.densoftpaysmart.model;

public class TeamList {
    private String StaffId = "";
    private String EmpName = "";
    private String LeaveName = "";
    private String FromDate = "";
    private String ToDate = "";
    private String NoOfDays = "";
    private String totaldays = "";
    private String RemainingLeave = "";
    private String P_Id = "";
    private String LeaveType = "";
    private String Status = "";

    public String getStaffId() {
        return StaffId;
    }

    public void setStaffId(String staffId) {
        StaffId = staffId;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getLeaveName() {
        return LeaveName;
    }

    public void setLeaveName(String leaveName) {
        LeaveName = leaveName;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getNoOfDays() {
        return NoOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        NoOfDays = noOfDays;
    }

    public String getTotaldays() {
        return totaldays;
    }

    public void setTotaldays(String totaldays) {
        this.totaldays = totaldays;
    }

    public String getRemainingLeave() {
        return RemainingLeave;
    }

    public void setRemainingLeave(String remainingLeave) {
        RemainingLeave = remainingLeave;
    }

    public String getP_Id() {
        return P_Id;
    }

    public void setP_Id(String p_Id) {
        P_Id = p_Id;
    }

    public String getLeaveType() {
        return LeaveType;
    }

    public void setLeaveType(String leaveType) {
        LeaveType = leaveType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
