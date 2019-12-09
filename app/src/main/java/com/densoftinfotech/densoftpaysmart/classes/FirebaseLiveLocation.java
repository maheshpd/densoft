package com.densoftinfotech.densoftpaysmart.classes;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class FirebaseLiveLocation implements Serializable/* implements ClusterItem*/{
    private String staff_id = "";
    private String staff_name = "";
    private String latitude = "";
    private String longitude = "";
    private String address = "";
    private String workinghours = "";
    private int allow_tracking = 0;
    private String transport_mode = "";

    public FirebaseLiveLocation(){

    }

    public FirebaseLiveLocation(String staff_id, String staff_name, String latitude, String longitude, String address, String workinghours, int allow_tracking, String transport_mode){
        this.staff_id = staff_id;
        this.staff_name = staff_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.workinghours = workinghours;
        this.allow_tracking = allow_tracking;
        this.transport_mode = transport_mode;
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
