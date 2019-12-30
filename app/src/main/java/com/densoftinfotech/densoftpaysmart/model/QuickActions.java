package com.densoftinfotech.densoftpaysmart.model;

public class QuickActions {

    private int image = 0;
    private String name = "";

    public QuickActions(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
