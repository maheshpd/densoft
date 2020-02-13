package com.densoftinfotech.densoftpaysmart.model;

import androidx.annotation.NonNull;

public class BranchDetails {

    private String BranchId = "";
    private String Name = "";
    private String Latitude = "";
    private String Longitude = "";
    private String Column1 = "";

    public String getBranchId() {
        return BranchId;
    }

    public void setBranchId(String branchId) {
        BranchId = branchId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getColumn1() {
        return Column1;
    }

    public void setColumn1(String column1) {
        Column1 = column1;
    }

    @NonNull
    @Override
    public String toString() {
        return this.Name;
    }
}
