package com.densoftinfotech.densoftpaysmart.model;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class FirebaseLiveLocation implements Serializable/* implements ClusterItem*/{
    private int staff_id = 0;
    private String staff_name = "";
    private String email = "";
    private String mobile = "";
    private double latitude = 0;
    private double longitude = 0;
    private String address = "";
    private String workinghours = "";
    private int allow_tracking = 0;
    private String transport_mode = "";
    private String photo_url = "";
    private String estimated_distance = "";
    private String estimated_time = "";
    private long timestamp = 0;
    private double angle = 0;

    public FirebaseLiveLocation(){

    }

    public FirebaseLiveLocation(int staff_id, String staff_name, double latitude, double longitude, String address, String workinghours, int allow_tracking, String transport_mode, String photo_url, long timestamp) {
        this.staff_id = staff_id;
        this.staff_name = staff_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.workinghours = workinghours;
        this.allow_tracking = allow_tracking;
        this.transport_mode = transport_mode;
        this.photo_url = photo_url;
        this.timestamp = timestamp;
    }


    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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

    public int getAllow_tracking() {
        return allow_tracking;
    }

    public void setAllow_tracking(int allow_tracking) {
        this.allow_tracking = allow_tracking;
    }

    public String getTransport_mode() {
        return transport_mode;
    }

    public void setTransport_mode(String transport_mode) {
        this.transport_mode = transport_mode;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getEstimated_distance() {
        return estimated_distance;
    }

    public void setEstimated_distance(String estimated_distance) {
        this.estimated_distance = estimated_distance;
    }

    public String getEstimated_time() {
        return estimated_time;
    }

    public void setEstimated_time(String estimated_time) {
        this.estimated_time = estimated_time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    /*String title = "";
    LatLng latLng = null;
    String snippet = "";
    public FirebaseLiveLocation(String title, LatLng latLng, String snippet) {
        this.title = title;
        this.latLng = latLng;
        this.snippet = snippet;
    }
    @Override public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }*/


    /*private LatLng mPosition;
    private String name;
    private String twitterHandle;

    public FirebaseLiveLocation(double lat, double lng, String name, String twitterHandle) {
        this.name = name;
        this.twitterHandle = twitterHandle;
        mPosition = new LatLng(lat, lng);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return twitterHandle;
    }*/

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
