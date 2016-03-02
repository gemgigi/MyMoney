package com.graduation.jasonzhu.mymoney.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.graduation.jasonzhu.mymoney.model.Account;
import com.graduation.jasonzhu.mymoney.model.Category;
import com.graduation.jasonzhu.mymoney.model.IncomeAndExpense;
import com.graduation.jasonzhu.mymoney.model.Summary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gemha on 2016/3/1.
 */
public class MyMoneyDb {

    private static final String DB_NAME = "mymoney.db";
    private static final int VERSION = 1;
    private SQLiteDatabase db;
    private MyMoneyOpenHelper openHelper;
    private static MyMoneyDb myMoneyDb;

    public MyMoneyDb(Context context) {
        openHelper = new MyMoneyOpenHelper(context, DB_NAME, null, VERSION);
        db = openHelper.getWritableDatabase();
    }

    public synchronized static MyMoneyDb getInstance(Context context) {
        if (myMoneyDb == null) {
            myMoneyDb = new MyMoneyDb(context);
        }
        return myMoneyDb;
    }


    public Summary getYearIncomeAndExpense(String year) {
        Summary summary = new Summary();
        String sql = "select type,SUM(money) as total from income_expense where strftime('%Y',saveTime) = ? GROUP BY type ;";
        Cursor cursor = db.rawQuery(sql, new String[]{year});
        float income = 0;
        float expense = 0;
        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndex("type"));
            float money = cursor.getFloat(cursor.getColumnIndex("total"));
            if (type != null && "收入".equals(type)) {
                summary.setIncome(String.valueOf(money));
                income = money;
            } else if (type != null && "支出".equals(type)) {
                summary.setExpense(String.valueOf(money));
                expense = money;
            }
        }
        if (summary.getIncome() == null || "".equals(summary.getIncome())) {
            summary.setIncome(String.valueOf(income));
        }
        if (summary.getExpense() == null || "".equals(summary.getExpense())) {
            summary.setExpense(String.valueOf(expense));
        }
        summary.setBalance(String.valueOf(Math.abs(expense - income)));
        Log.d("DATA", "income = " + summary.getIncome()
                + ", expense = " + summary.getExpense() + ",balance = " + summary.getBalance());
        cursor.close();
        return summary;
    }

    public List<Summary> getMonthIncomeAndExpense(String year) {
        List<Summary> summaries = new ArrayList<>();
        String sql = "select type,SUM(money) as total,strftime('%m',saveTime) as month from income_expense " +
                "where strftime('%Y',saveTime) = ? GROUP BY strftime('%m',saveTime),type ORDER BY saveTime desc;";
        Cursor cursor = db.rawQuery(sql, new String[]{year});
        float income = 0;
        float expense = 0;
        int lastMonth = 0;
        Summary summary = null;
        Summary lastSummary = null;
        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndex("type"));
            int tempMonth = cursor.getInt(cursor.getColumnIndex("month"));
            float money = cursor.getFloat(cursor.getColumnIndex("total"));
            if (lastMonth != tempMonth) {
                summary = new Summary();
                if (lastSummary != null) {
                    lastSummary.setBalance(String.valueOf(Math.abs(expense - income)));
                    summaries.add(lastSummary);
                }
            }
            if ("收入".equals(type)) {
                summary.setIncome(String.valueOf(money));
                income = money;
            } else if ("支出".equals(type)) {
                summary.setExpense(String.valueOf(money));
                expense = money;
            }
            lastSummary = summary;
            lastMonth = tempMonth;
        }
        cursor.close();
        return summaries;
    }

    public List<IncomeAndExpense> getDayIncomeAndExpense(String year, String month) {
        List<IncomeAndExpense> lists = new ArrayList<>();
        String sql = "select i.id,i.type,i.money,i.saveTime,i.remark," +
                "a.id as accountId,a.accountName,a.balance," +
                "c.id as categoryId,c.categoryName,c.parentId as categoryParentId " +
                "from income_expense i " +
                "join category c on i.categoryId = c.id " +
                "join account a on i.accountId = a.id " +
                "where strftime('%m',i.saveTime) = ? and strftime('%Y',i.saveTime) = ? " +
                "ORDER BY saveTime desc;";

        Cursor cursor = db.rawQuery(sql, new String[]{month, year});
        List<Category> categoryChildList = new ArrayList<>();
        while (cursor.moveToNext()) {
            categoryChildList.clear();
            IncomeAndExpense incomeAndExpense = new IncomeAndExpense();
            Category categoryChild = new Category();
            Category categoryParent = null;
            Account account = new Account();
            incomeAndExpense.setId(cursor.getInt(cursor.getColumnIndex("id")));
            incomeAndExpense.setType(cursor.getString(cursor.getColumnIndex("type")));
            incomeAndExpense.setMoney(cursor.getFloat(cursor.getColumnIndex("money")));
            incomeAndExpense.setSaveTime(cursor.getString(cursor.getColumnIndex("saveTime")));
            //如果该分类是子分类
            int parentId = cursor.getInt(cursor.getColumnIndex("categoryParentId"));
            if (parentId > 0) {
                categoryChild.setId(cursor.getInt(cursor.getColumnIndex("categoryId")));
                categoryChild.setType(cursor.getString(cursor.getColumnIndex("type")));
                categoryChild.setName(cursor.getString(cursor.getColumnIndex("categoryName")));
                categoryChildList.add(categoryChild);
                categoryParent = getCategoryById(parentId);
                categoryParent.setCategoryList(categoryChildList);
                incomeAndExpense.setCategory(categoryParent);
                //如果该分类是主分类
            } else {
                categoryParent = new Category();
                categoryParent.setId(cursor.getInt(cursor.getColumnIndex("categoryId")));
                categoryParent.setType(cursor.getString(cursor.getColumnIndex("type")));
                categoryParent.setName(cursor.getString(cursor.getColumnIndex("categoryName")));
                incomeAndExpense.setCategory(categoryParent);
            }
            account.setId(cursor.getInt(cursor.getColumnIndex("accountId")));
            account.setAccountMoney(cursor.getFloat(cursor.getColumnIndex("balance")));
            account.setAccountName(cursor.getString(cursor.getColumnIndex("accountName")));
            incomeAndExpense.setAccount(account);

        }
        cursor.close();
        return lists;
    }


    public long saveIncomeAndExpense(IncomeAndExpense incomeAndExpense) {
        long newRow = 0;
        if (incomeAndExpense != null) {
            ContentValues values = new ContentValues();
            values.put("type", incomeAndExpense.getType());
            values.put("money", incomeAndExpense.getMoney());
            values.put("saveTime", incomeAndExpense.getSaveTime());
            values.put("remark", incomeAndExpense.getRemark());
            values.put("accountId", incomeAndExpense.getAccount().getId());
            values.put("categoryId", incomeAndExpense.getCategory().getId());
            newRow = db.insert("income_expense", null, values);
        }
        return newRow;
    }

    private void updateIncomeAndExpense(IncomeAndExpense oldIncomeAndExpense, IncomeAndExpense newIncomeAndExpense) {

        if (oldIncomeAndExpense.toString().equals(newIncomeAndExpense.toString())) {
            return;
        }
        if (oldIncomeAndExpense != null && newIncomeAndExpense != null) {
            ContentValues values = new ContentValues();
            values.put("money", newIncomeAndExpense.getMoney());
            values.put("saveTime", newIncomeAndExpense.getSaveTime());
            values.put("updateTime", newIncomeAndExpense.getSaveTime());
            values.put("accountId", newIncomeAndExpense.getAccount().getId());
            values.put("categoryId", newIncomeAndExpense.getCategory().getId());
            db.update("income_expense", values, "id = ?", new String[]{String.valueOf(newIncomeAndExpense.getId())});
        }
    }

    //根据parentId查找parentCategory
    public Category getCategoryById(int id) {
        Category category = new Category();
        String sql = "select * from category where id = " + id;
        Cursor cursor = db.query("category", null, "where id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while (cursor.moveToNext()) {
            category.setId(cursor.getInt(cursor.getColumnIndex("id")));
            category.setType(cursor.getString(cursor.getColumnIndex("type")));
            category.setName(cursor.getString(cursor.getColumnIndex("categoryName")));
        }
        cursor.close();
        return category;
    }

    public List<Category> getMainCategory() {
        List<Category> categoryList = new ArrayList<>();
        Cursor cursor = db.query("category", null, "parentId is ?", new String[]{}, null, null, null);
        while (cursor.moveToNext()) {
            Category category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndex("id")));
            category.setName(cursor.getString(cursor.getColumnIndex("categoryName")));
            category.setType(cursor.getString(cursor.getColumnIndex("type")));
            category.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
            categoryList.add(category);
        }
        cursor.close();
        return categoryList;
    }

    public List<Category> getSubCategory(int id){
        List<Category> categoryList = new ArrayList<>();
        Cursor cursor = db.query("category", null, "parentId = ?",new String[]{String.valueOf(id)}, null, null, null);
        while (cursor.moveToNext()){
            Category category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndex("id")));
            category.setName(cursor.getString(cursor.getColumnIndex("categoryName")));
            category.setType(cursor.getString(cursor.getColumnIndex("type")));
            category.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
            categoryList.add(category);
        }
        cursor.close();
        return categoryList;
    }
    public List<Category> getAllCategory() {
        List<Category> categoryMainList = getMainCategory();
        for (Category c:categoryMainList){
            c.setCategoryList(getSubCategory(c.getId()));
        }
        return categoryMainList;
    }


    public void insertCategory(Category category) {
        long newRow;
        if (category != null) {
            ContentValues values = new ContentValues();
            values.put("type", category.getType());
            values.put("categoryName", category.getName());
            values.put("updateTime", category.getUpdateTime());
            //插入子类别时 parentId 不等于0
            if (category.getParentId() != 0) {
                values.put("parentId", category.getParentId());
            }
            newRow = db.insert("category", null, values);
            Log.d("DATA", "newRow = " + String.valueOf(newRow));
            if (category.getCategoryList() != null && category.getCategoryList().size() > 0) {
                for (Category c : category.getCategoryList()) {
                    values.clear();
                    values.put("type", c.getType());
                    values.put("categoryName", c.getName());
                    values.put("updateTime", c.getUpdateTime());
                    values.put("parentId", newRow);
                    long row = db.insert("category", null, values);
                    Log.d("DATA", "newRow2 = " + String.valueOf(row));
                }
            }
        }
    }

}
