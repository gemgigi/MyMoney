package com.graduation.jasonzhu.mymoney.model;

import java.io.Serializable;

/**
 * Created by gemha on 2016/2/19.
 */
public class IncomeAndExpense implements Serializable{
    private int id ;
    private float money;
    private String type;
    private String time;
    private Account account;
    private Category category;

    public IncomeAndExpense(String type,float money, String time, Account account, Category category){
        this.type = type;
        this.money = money;
        this.time = time;
        this.account = account;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
