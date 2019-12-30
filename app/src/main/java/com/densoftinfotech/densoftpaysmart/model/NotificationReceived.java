package com.densoftinfotech.densoftpaysmart.model;

import org.json.JSONObject;

import androidx.annotation.NonNull;

public class NotificationReceived {

    private long id = 0;
    private String title = "";
    private String description = "";
    private String big_picture = "";
    private int deleted = 0;

    public NotificationReceived(JSONObject obj){
        if(obj.has("id")){
            this.id = obj.optInt("id");
        }
        if(obj.has("title")){
            this.title = obj.optString("title");
        }
        if(obj.has("description")){
            this.description = obj.optString("description");
        }
        if(obj.has("big_picture")){
            this.big_picture = obj.optString("big_picture");
        }
        if(obj.has("deleted")){
            this.deleted = obj.optInt("deleted");
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBig_picture() {
        return big_picture;
    }

    public void setBig_picture(String big_picture) {
        this.big_picture = big_picture;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
