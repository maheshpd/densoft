package com.densoftinfotech.densoftpaysmart.classes;

public class FirebaseLiveLocation {
    private String staff_id = "";
    private String staff_name = "";
    private String latitude = "";
    private String longitude = "";
    private String address = "";
    private String workinghours = "";

    public FirebaseLiveLocation(String staff_id,String staff_name, String latitude, String longitude, String address, String workinghours){
        this.staff_id = staff_id;
        this.staff_name = staff_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.workinghours = workinghours;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkinghours() {
        return workinghours;
    }

    public void setWorkinghours(String workinghours) {
        this.workinghours = workinghours;
    }
}
