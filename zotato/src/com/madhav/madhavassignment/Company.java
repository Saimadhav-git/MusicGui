package com.madhav.madhavassignment;

public class Company {
    private String name;
    private double company_money;
    Company(String name)
    {
        this.name=name;
        this.company_money=0;
    }
    public void add_company_money(double company_money)
    {
        this.company_money+=company_money;
    }
    public void company_details()
    {
        System.out.println("company money = "+this.company_money);
    }
}
