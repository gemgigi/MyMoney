package com.graduation.jasonzhu.mymoney.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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


    private Summary getYearIncomeAndExpense(String year) {
        Summary summary = new Summary();
        String sql = "select type,SUM(money) as total from income_expense where YEAR(saveTime) = ? GROUP BY type ;";
        Cursor cursor = db.rawQuery(sql, new String[]{year});
        float income = 0;
        float expense = 0;
        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndex("type"));
            float money = cursor.getFloat(cursor.getColumnIndex("total"));
            if ("收入".equals(type)) {
                summary.setIncome(String.valueOf(money));
                income = money;
            } else if ("支出".equals(type)) {
                summary.setExpense(String.valueOf(money));
                expense = money;
            }
        }
        summary.setBalance(String.valueOf(Math.abs(expense - income)));
        return summary;
    }

    private List<Summary> getMonthIncomeAndExpense(String year) {
        List<Summary> summaries = new ArrayList<>();
        String sql = "select type,SUM(money) as total,MONTH(saveTime) as month from income_expense " +
                "where YEAR(saveTime) = ? GROUP BY MONTH(saveTime),type ORDER BY saveTime desc;";
        Cursor cursor = db.rawQuery(sql, new String[]{year});
        float income = 0;
        float expense = 0;
        int lastMonth = 0;
        Summary summary = null;
        Summary lastSummary = null;
        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndex("type"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            float money = cursor.getFloat(cursor.getColumnIndex("total"));
            if (lastMonth != month) {
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
            lastMonth = month;
        }
        return summaries;
    }

    private List<List<IncomeAndExpense>> getDayIncomeAndExpense(String month) {
        List<List<IncomeAndExpense>> lists = new ArrayList<>();
        String sql = "select i.id,i.type,i.money,i.saveTime,i.remark," +
                "a.id as accountId,a.accountName,a.balance," +
                "c.id as categoryId,c.categoryName,c.parentId as categoryParentId" +
                "from income_expense i " +
                "join category c on i.categoryId = c.id " +
                "join account a on i.accountId = a.id " +
                "where MONTH(i.saveTime) = ?" +
                "ORDER BY saveTime desc;";
        Cursor cursor = db.rawQuery(sql, new String[]{month});
        return lists;
    }

}
