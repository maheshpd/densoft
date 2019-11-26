package com.densoftinfotech.densoftpaysmart.classes;

import androidx.annotation.NonNull;

public class CareOfStaff_Employee {
    private String patientid = "";
    private String pname = "";

    public CareOfStaff_Employee(String patientid, String pname){
        this.patientid = patientid;
        this.pname = pname;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    @NonNull
    @Override
    public String toString() {
        return this.pname;
    }
}
