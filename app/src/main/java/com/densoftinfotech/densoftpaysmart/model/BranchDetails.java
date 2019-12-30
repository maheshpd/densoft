package com.densoftinfotech.densoftpaysmart.model;

import androidx.annotation.NonNull;

public class BranchDetails {

    private String BranchId = "";
    private String Name = "";
    private String Latitude = "";
    private String Longitude = "";

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

    @NonNull
    @Override
    public String toString() {
        return this.Name;
    }
}
