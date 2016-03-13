package com.graduation.jasonzhu.mymoney.model;

import java.io.Serializable;

/**
 * Created by gemha on 2016/2/19.
 */
public class IncomeAndExpense implements Serializable {
    private int id;
    private float money;
    private String type;
    private String saveTime;
    private String syncTime;
    private String updateTime;
    private String remark;
    private Account account;
    private Category category;
    private Transfer transfer;
    private int recordType;

    @Override
    public String toString() {
        return "IncomeAndExpense{" +
                "id=" + id +
                ", money=" + money +
                ", type='" + type + '\'' +
                ", saveTime='" + saveTime + '\'' +
                ", syncTime='" + syncTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", remark='" + remark + '\'' +
                ", account=" + account +
                ", category=" + category +
                '}';
    }

    public IncomeAndExpense() {
    }

    public IncomeAndExpense(String type, float money, String time, Account account, Category category) {
        this.type = type;
        this.money = money;
        this.saveTime = time;
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

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
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

    public String getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(String syncTime) {
        this.syncTime = syncTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }
}
