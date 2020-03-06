package com.densoftinfotech.densoftpaysmart.model;

public class LocalTrack{
    private String key;
    private String value;
    private String date;

    public LocalTrack(String key, String value, String date) {
        this.key = key;
        this.value = value;
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
