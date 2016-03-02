package com.graduation.jasonzhu.mymoney.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gemha on 2016/2/19.
 */
public class Category implements Serializable {
    private int id;
    private String type;
    private String name;
    private String updateTime;
    private String syncTime;
    private List<Category> categoryList;
    private int parentId;

    public Category() {
    }

    public Category(int id, String type, String name, String time) {
        this.id = id;
        this.name = name;
        this.updateTime = time;
        this.type = type;
    }

    public Category(String type, String name, String time) {
        this.name = name;
        this.updateTime = time;
        this.type = type;
    }

    public Category(String type, String name, String time, List<Category> categoryList) {
        this.type = type;
        this.name = name;
        this.updateTime = time;
        this.categoryList = categoryList;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(String syncTime) {
        this.syncTime = syncTime;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
