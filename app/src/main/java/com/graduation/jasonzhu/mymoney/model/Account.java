package com.graduation.jasonzhu.mymoney.model;

import java.io.Serializable;

/**
 * Created by gemha on 2016/2/17.
 */
public class Account implements Serializable {
    private int id;
    private String accountName;
    private float accountMoney;

    public Account(String accountName, float accountMoney) {
        this.accountName = accountName;
        this.accountMoney = accountMoney;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public float getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(float accountMoney) {
        this.accountMoney = accountMoney;
    }
}
