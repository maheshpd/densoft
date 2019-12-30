package com.densoftinfotech.densoftpaysmart.model;

public class SalarySlip {

    private int ApplyForMonth = 0;
    private int ApplyForYear = 0;
    private String Name = "";
    private double Amount = 0;
    private boolean isSelected = false;


    public int getApplyForMonth() {
        return ApplyForMonth;
    }

    public void setApplyForMonth(int applyForMonth) {
        ApplyForMonth = applyForMonth;
    }

    public int getApplyForYear() {
        return ApplyForYear;
    }

    public void setApplyForYear(int applyForYear) {
        ApplyForYear = applyForYear;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
