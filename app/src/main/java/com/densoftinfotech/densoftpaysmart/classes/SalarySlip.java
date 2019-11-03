package com.densoftinfotech.densoftpaysmart.classes;

public class SalarySlip {

    int ApplyForMonth = 0;
    int ApplyForYear = 0;
    String Name = "";
    double Amount = 0;

    public SalarySlip(String Name, double Amount, int ApplyForMonth, int ApplyForYear) {
        this.Name = Name;
        this.Amount = Amount;
        this.ApplyForMonth = ApplyForMonth;
        this.ApplyForYear = ApplyForYear;
    }

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
}
