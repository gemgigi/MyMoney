package com.graduation.jasonzhu.mymoney.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by gemha on 2016/2/19.
 */
public class Category implements Serializable {
    private int id;
    private String type;
    private String name;
    private String time;
    private List<Category> categoryList;

    public Category(String type, String name, String time) {
        this.name = name;
        this.time = time;
        this.type = type;
    }

    public Category(String type, String name, String time, List<Category>  categoryList) {
        this.name = name;
        this.time = time;
        this.type = type;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
