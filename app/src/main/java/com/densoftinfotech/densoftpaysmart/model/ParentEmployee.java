package com.densoftinfotech.densoftpaysmart.model;

import androidx.annotation.NonNull;

public class ParentEmployee {
    private String patientId = "";
    private String Pname = "";

    public ParentEmployee(String patientId, String Pname){
        this.patientId = patientId;
        this.Pname = Pname;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    @NonNull
    @Override
    public String toString() {
        return this.Pname;
    }
}
