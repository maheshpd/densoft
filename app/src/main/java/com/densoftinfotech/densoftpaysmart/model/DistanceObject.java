package com.densoftinfotech.densoftpaysmart.model;

import org.json.JSONObject;

public class DistanceObject {
    private String text = "";
    private int value = 0;

    public DistanceObject(JSONObject obj){
        if(obj.has("text")){
            this.text = obj.optString("text");
        }if(obj.has("value")){
            this.value = obj.optInt("value");
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
