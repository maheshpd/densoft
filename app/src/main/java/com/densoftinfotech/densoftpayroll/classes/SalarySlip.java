package com.densoftinfotech.densoftpayroll.classes;

public class SalarySlip {
    String month = "";
    int days_of_month = 0;
    double take_home = 0;
    double deduction = 0;

    public SalarySlip(String month, int days_of_month, double take_home, double deduction) {
        this.month = month;
        this.days_of_month = days_of_month;
        this.take_home = take_home;
        this.deduction = deduction;
    }

    public SalarySlip(double take_home, double deduction) {
        this.take_home = take_home;
        this.deduction = deduction;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getDays_of_month() {
        return days_of_month;
    }

    public void setDays_of_month(int days_of_month) {
        this.days_of_month = days_of_month;
    }

    public double getTake_home() {
        return take_home;
    }

    public void setTake_home(double take_home) {
        this.take_home = take_home;
    }

    public double getDeduction() {
        return deduction;
    }

    public void setDeduction(double deduction) {
        this.deduction = deduction;
    }
}
