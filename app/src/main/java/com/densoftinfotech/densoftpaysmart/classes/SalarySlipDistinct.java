package com.densoftinfotech.densoftpaysmart.classes;

import java.util.ArrayList;

public class SalarySlipDistinct {

    int ApplyForMonth = 0;
    int ApplyForYear = 0;
    String Name = "";
    double Amount = 0;

    ArrayList<SalarySlip> salarySlips = new ArrayList<>();

    /*public SalarySlipDistinct(int ApplyForMonth, int ApplyForYear, String Name, double Amount) {
        this.ApplyForMonth = ApplyForMonth;
        this.ApplyForYear = ApplyForYear;
        this.Name = Name;
        this.Amount = Amount;
    }*/

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

    public ArrayList<SalarySlip> getSalarySlips() {
        return salarySlips;
    }

    public void setSalarySlips(ArrayList<SalarySlip> salarySlips) {
        this.salarySlips = salarySlips;
    }
}
