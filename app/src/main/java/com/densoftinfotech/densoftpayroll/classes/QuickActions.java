package com.densoftinfotech.densoftpayroll.classes;

public class QuickActions {

    int image = 0;
    String name = "";

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
