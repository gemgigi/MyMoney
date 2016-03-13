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
import com.graduation.jasonzhu.mymoney.model.TestData;
import com.graduation.jasonzhu.mymoney.model.Transfer;
import com.graduation.jasonzhu.mymoney.util.LoadDataCallBackListener;
import com.graduation.jasonzhu.mymoney.util.LogUtil;
import com.graduation.jasonzhu.mymoney.util.TimeUtil;

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
    private List<Category> categoryIncomeList = new ArrayList<>();
    private List<Category> categoryExpenseList = new ArrayList<>();
    private List<Account> accountList = new ArrayList<>();
    private List<Summary> monthSummaryList = new ArrayList<>();
    private List<Transfer> transferList = new ArrayList<>();


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

        summary.setBalance(String.valueOf(income - expense));


        cursor.close();
        return summary;
    }

    public List<Summary> getMonthIncomeAndExpense(String year) {
        monthSummaryList.clear();
        String sql = "select type,SUM(money) as total,strftime('%m',saveTime) as month from income_expense " +
                "where strftime('%Y',saveTime) = ? GROUP BY strftime('%m',saveTime),type ORDER BY saveTime desc;";
        Cursor cursor = db.rawQuery(sql, new String[]{year});
        float income = 0;
        float expense = 0;
        int lastMonth = 0;
        float money;
        Summary summary = new Summary();
        Summary lastSummary = null;
        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndex("type"));
            int tempMonth = cursor.getInt(cursor.getColumnIndex("month"));
            money = cursor.getFloat(cursor.getColumnIndex("total"));
            if (lastMonth != tempMonth) {
                summary = new Summary();
                if (lastSummary != null) {
                    if (lastSummary.getIncome() == null) {
                        lastSummary.setIncome(String.valueOf(0.0f));
                    }
                    if (lastSummary.getExpense() == null) {
                        lastSummary.setExpense(String.valueOf(0.0f));
                    }
                    lastSummary.setMonth(String.valueOf(lastMonth));
                    lastSummary.setBalance(String.valueOf(Math.abs(expense - income)));
                    monthSummaryList.add(lastSummary);
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
            if (cursor.isLast()) {
                if (lastSummary.getIncome() == null) {
                    lastSummary.setIncome(String.valueOf(0.0f));
                }
                if (lastSummary.getExpense() == null) {
                    lastSummary.setExpense(String.valueOf(0.0f));
                }
                lastSummary.setMonth(String.valueOf(lastMonth));
                lastSummary.setBalance(String.valueOf(Math.abs(expense - income)));
                monthSummaryList.add(lastSummary);
            }
        }
        cursor.close();
        return monthSummaryList;
    }

    public List<IncomeAndExpense> getDayIncomeAndExpense(String year, String month) {
        List<IncomeAndExpense> daySummaryList = new ArrayList<>();
        String sql = "select i.id,i.type,i.money,i.saveTime,i.remark,i.updateTime," +
                "a.id as accountId,a.accountName,a.balance," +
                "c.id as categoryId,c.categoryName,c.parentId as categoryParentId " +
                "from income_expense i " +
                "join category c on i.categoryId = c.id " +
                "join account a on i.accountId = a.id " +
                "where strftime('%m',i.saveTime) = ? and strftime('%Y',i.saveTime) = ? " +
                "ORDER BY saveTime desc;";
        Cursor cursor = db.rawQuery(sql, new String[]{month, year});
        while (cursor.moveToNext()) {
            List<Category> categoryChildList = new ArrayList<>();
            IncomeAndExpense incomeAndExpense = new IncomeAndExpense();
            Category categoryChild = new Category();
            Category categoryParent;
            Account account = new Account();
            incomeAndExpense.setId(cursor.getInt(cursor.getColumnIndex("id")));
            incomeAndExpense.setType(cursor.getString(cursor.getColumnIndex("type")));
            incomeAndExpense.setMoney(cursor.getFloat(cursor.getColumnIndex("money")));
            incomeAndExpense.setSaveTime(cursor.getString(cursor.getColumnIndex("saveTime")));
            incomeAndExpense.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
            incomeAndExpense.setRecordType(0);
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
            daySummaryList.add(incomeAndExpense);
        }
        cursor.close();
        return daySummaryList;
    }


    public long saveIncomeAndExpense(IncomeAndExpense incomeAndExpense) {
        long newRow = 0;
        if (incomeAndExpense != null) {
            ContentValues valuesInsert = new ContentValues();
            valuesInsert.put("type", incomeAndExpense.getType());
            valuesInsert.put("money", incomeAndExpense.getMoney());
            valuesInsert.put("saveTime", incomeAndExpense.getSaveTime());
            valuesInsert.put("remark", incomeAndExpense.getRemark());
            valuesInsert.put("updateTime", incomeAndExpense.getUpdateTime());
            valuesInsert.put("accountId", incomeAndExpense.getAccount().getId());
            valuesInsert.put("categoryId", incomeAndExpense.getCategory().getId());
            ContentValues valuesUpdate = new ContentValues();
            db.beginTransaction();
            try {
                if ("支出".equals(incomeAndExpense.getType())) {
                    valuesUpdate.put("balance", incomeAndExpense.getAccount().getAccountMoney() - incomeAndExpense.getMoney());
                    newRow = db.insert("income_expense", null, valuesInsert);
                    db.update("account", valuesUpdate, "id = ?", new String[]{String.valueOf(incomeAndExpense.getAccount().getId())});
                    db.setTransactionSuccessful();
                } else if ("收入".equals(incomeAndExpense.getType())) {
                    valuesUpdate.put("balance", incomeAndExpense.getAccount().getAccountMoney() + incomeAndExpense.getMoney());
                    newRow = db.insert("income_expense", null, valuesInsert);
                    db.update("account", valuesUpdate, "id = ?", new String[]{String.valueOf(incomeAndExpense.getAccount().getId())});
                    db.setTransactionSuccessful();
                }
            } catch (Exception e) {
            } finally {
                db.endTransaction();
            }
        }
        return newRow;
    }


    public void updateIncomeAndExpense(IncomeAndExpense oldIncomeAndExpense, IncomeAndExpense newIncomeAndExpense) {
        if (newIncomeAndExpense != null && oldIncomeAndExpense != null) {
            ContentValues values = new ContentValues();
            values.put("money", newIncomeAndExpense.getMoney());
            values.put("saveTime", newIncomeAndExpense.getSaveTime());
            values.put("updateTime", newIncomeAndExpense.getUpdateTime());
            values.put("accountId", newIncomeAndExpense.getAccount().getId());
            values.put("categoryId", newIncomeAndExpense.getCategory().getId());
            values.put("remark", newIncomeAndExpense.getRemark());
            db.beginTransaction();
            try {
                db.update("income_expense", values, "id = ?", new String[]{String.valueOf(newIncomeAndExpense.getId())});
                ContentValues valuesOld = new ContentValues();
                ContentValues valuesNew = new ContentValues();
                if (!oldIncomeAndExpense.getAccount().getAccountName().equals(newIncomeAndExpense.getAccount().getAccountName())) {
                    if ("支出".equals(oldIncomeAndExpense.getType())) {
                        valuesOld.put("balance", oldIncomeAndExpense.getAccount().getAccountMoney() + oldIncomeAndExpense.getMoney());
                        valuesOld.put("updateTime", newIncomeAndExpense.getUpdateTime());
                        valuesNew.put("balance", newIncomeAndExpense.getAccount().getAccountMoney() - newIncomeAndExpense.getMoney());
                        valuesNew.put("updateTime", newIncomeAndExpense.getUpdateTime());
                    } else if ("收入".equals(oldIncomeAndExpense.getType())) {
                        valuesOld.put("balance", oldIncomeAndExpense.getAccount().getAccountMoney() - oldIncomeAndExpense.getMoney());
                        valuesOld.put("updateTime", newIncomeAndExpense.getUpdateTime());
                        valuesNew.put("balance", newIncomeAndExpense.getAccount().getAccountMoney() + newIncomeAndExpense.getMoney());
                        valuesNew.put("updateTime", newIncomeAndExpense.getUpdateTime());
                    }
                    db.update("account", valuesOld, "id = ?", new String[]{String.valueOf(oldIncomeAndExpense.getAccount().getId())});
                    db.update("account", valuesNew, "id = ?", new String[]{String.valueOf(newIncomeAndExpense.getAccount().getId())});
                    valuesOld.clear();
                    valuesNew.clear();
                } else {
                    if ("支出".equals(oldIncomeAndExpense.getType())) {
                        valuesNew.put("balance", oldIncomeAndExpense.getAccount().getAccountMoney() + oldIncomeAndExpense.getMoney() - newIncomeAndExpense.getMoney());
                        valuesNew.put("updateTime", newIncomeAndExpense.getUpdateTime());
                    } else if ("收入".equals(oldIncomeAndExpense.getType())) {
                        valuesNew.put("balance", oldIncomeAndExpense.getAccount().getAccountMoney() - oldIncomeAndExpense.getMoney() + newIncomeAndExpense.getMoney());
                        valuesNew.put("updateTime", newIncomeAndExpense.getUpdateTime());
                    }
                    db.update("account", valuesNew, "id = ?", new String[]{String.valueOf(oldIncomeAndExpense.getAccount().getId())});
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {

            } finally {
                db.endTransaction();
            }
        }
    }

    public void deleteIncomeAndExpense(IncomeAndExpense incomeAndExpense) {
        if (incomeAndExpense != null) {
            db.beginTransaction();
            try {
                db.delete("income_expense", "id = ?", new String[]{String.valueOf(incomeAndExpense.getId())});
                ContentValues values = new ContentValues();
                if ("支出".equals(incomeAndExpense.getType())) {
                    values.put("balance", incomeAndExpense.getAccount().getAccountMoney() + incomeAndExpense.getMoney());
                } else if ("收入".equals(incomeAndExpense.getType())) {
                    values.put("balance", incomeAndExpense.getAccount().getAccountMoney() - incomeAndExpense.getMoney());
                }
                values.put("updateTime", TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
                db.update("account", values, "id = ?", new String[]{String.valueOf(incomeAndExpense.getAccount().getId())});
                db.setTransactionSuccessful();
            } catch (Exception e) {
                LogUtil.e("Error", "delete fail");
            } finally {
                db.endTransaction();
            }

        }
    }

    //根据parentId查找parentCategory
    public Category getCategoryById(int id) {
        Category category = new Category();
        Cursor cursor = db.query("category", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while (cursor.moveToNext()) {
            category.setId(cursor.getInt(cursor.getColumnIndex("id")));
            category.setType(cursor.getString(cursor.getColumnIndex("type")));
            category.setName(cursor.getString(cursor.getColumnIndex("categoryName")));
        }
        cursor.close();
        return category;
    }

    public List<Category> getMainCategory(String type) {

        List<Category> categoryList;
        Cursor cursor;
        if (!"".equals(type)) {
            cursor = db.query("category", null, "type = ? and parentId is ?", new String[]{type}, null, null, null);
            if ("收入".equals(type)) {
                categoryIncomeList.clear();
                categoryList = categoryIncomeList;
            } else {
                categoryExpenseList.clear();
                categoryList = categoryExpenseList;
            }
        } else {
            cursor = db.query("category", null, "parentId is ?", new String[]{}, null, null, null);
            categoryList = new ArrayList<>();
        }

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

    public List<Category> getSubCategory(int id) {
        List<Category> categoryList = new ArrayList<>();
        Cursor cursor = db.query("category", null, "parentId = ?", new String[]{String.valueOf(id)}, null, null, null);
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

    public List<Category> getAllCategory(String type) {
        List<Category> categoryMainList = getMainCategory(type);
        for (Category c : categoryMainList) {
            c.setCategoryList(getSubCategory(c.getId()));
        }
        return categoryMainList;
    }

    public List<String> getAllCategoryName() {
        List<String> names = new ArrayList<>();
        Cursor cursor = db.query("category", new String[]{"categoryName"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            names.add(cursor.getString(cursor.getColumnIndex("categoryName")));
        }
        cursor.close();
        return names;
    }


    /**
     * 1.只插入父分类
     * 2.只插入子分类
     * 3.父分类和子分类一起插入
     *
     * @param category
     */
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
            if (newRow > 0) {
                if (category.getCategoryList() != null && category.getCategoryList().size() > 0) {
                    for (Category c : category.getCategoryList()) {
                        values.clear();
                        values.put("type", c.getType());
                        values.put("categoryName", c.getName());
                        values.put("updateTime", c.getUpdateTime());
                        values.put("parentId", newRow);
                        db.insert("category", null, values);
                    }
                }
            }
        }
    }

    public void updateCategory(Category category) {
        if (category != null) {
            ContentValues values = new ContentValues();
            values.put("type", category.getType());
            values.put("categoryName", category.getName());
            values.put("updateTime", category.getUpdateTime());
            //修改子类别时 parentId 不等于0
            if (category.getParentId() != 0) {
                values.put("parentId", category.getParentId());
            }
            db.update("category", values, "id = ?", new String[]{String.valueOf(category.getId())});
        }
    }

    public void deleteCategory(Category category) {
        if (category != null) {
            db.delete("category", "id = ?", new String[]{String.valueOf(category.getId())});
        }
    }

//    public void loadCategory(final List<Category> categoryList, final LoadDataCallBackListener loadDataCallBackListener) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int count = 0;
//                //  db.beginTransaction();
//                for (Category category : categoryList) {
//                    try {
//                        insertCategory(category);
//                        count++;
//                    } catch (Exception e) {
////                        db.endTransaction();
////                        loadDataCallBackListener.onError(e);
//                        return;
//                    }
//                }
////                if (count == categoryList.size()) {
////                    db.setTransactionSuccessful();
////                    db.endTransaction();
////                    loadDataCallBackListener.onFinish();
////                }
//            }
//        }).start();
//    }

    public void loadCategory(List<Category> categoryList) {
        for (Category category : categoryList) {
            try {
                insertCategory(category);
            } catch (Exception e) {
            }
        }
    }


    public Account getAccountById(int id) {
        Cursor cursor = db.query("account", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        Account account = new Account();
        while (cursor.moveToNext()) {
            account.setId(cursor.getInt(cursor.getColumnIndex("id")));
            account.setAccountName(cursor.getString(cursor.getColumnIndex("accountName")));
            account.setAccountMoney(cursor.getFloat(cursor.getColumnIndex("balance")));
            account.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
            account.setSyncTime(cursor.getString(cursor.getColumnIndex("syncTime")));
        }
        return account;
    }

    public List<Account> getAllAccount() {
        accountList.clear();
        Cursor cursor = db.query("account", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Account account = new Account();
            account.setId(cursor.getInt(cursor.getColumnIndex("id")));
            account.setAccountName(cursor.getString(cursor.getColumnIndex("accountName")));
            account.setAccountMoney(cursor.getFloat(cursor.getColumnIndex("balance")));
            account.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
            account.setSyncTime(cursor.getString(cursor.getColumnIndex("syncTime")));
            accountList.add(account);
        }
        cursor.close();
        return accountList;
    }


    public void insertAccount(Account account) {
        if (account != null) {
            ContentValues values = new ContentValues();
            values.put("accountName", account.getAccountName());
            values.put("balance", account.getAccountMoney());
            values.put("updateTime", account.getUpdateTime());
            db.insert("account", null, values);
        }
    }

    public void updateAccount(Account account) {
        if (account != null) {
            ContentValues values = new ContentValues();
            values.put("accountName", account.getAccountName());
            values.put("balance", account.getAccountMoney());
            values.put("updateTime", account.getUpdateTime());
            db.update("account", values, "id = ?", new String[]{String.valueOf(account.getId())});
        }
    }

    public void deleteAccount(Account account) {
        if (account != null) {
            db.delete("account", "id = ?", new String[]{String.valueOf(account.getId())});
        }
    }


    public List<Transfer> getTransfer(String year, String month) {
        transferList.clear();
        String sql = "select * from transfer where strftime('%m',transferTime) = ? and strftime('%Y',transferTime) = ? " +
                "order by transferTime";
        Cursor cursor = db.rawQuery(sql, new String[]{month, year});
        while (cursor.moveToNext()) {
            Transfer transfer = new Transfer();
            transfer.setId(cursor.getInt(cursor.getColumnIndex("id")));
            transfer.setMoney(cursor.getFloat(cursor.getColumnIndex("money")));
            transfer.setTransferTime(cursor.getString(cursor.getColumnIndex("transferTime")));
            transfer.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
            transfer.setSyncTime(cursor.getString(cursor.getColumnIndex("syncTime")));
            transfer.setFromAccount(getAccountById(cursor.getInt(cursor.getColumnIndex("fromAccount"))));
            transfer.setToAccount(getAccountById(cursor.getInt(cursor.getColumnIndex("toAccount"))));
            transferList.add(transfer);
        }
        cursor.close();
        return transferList;
    }

    public void insertTransfer(Transfer transfer) {
        if (transfer != null) {
            ContentValues values = new ContentValues();
            values.put("money", transfer.getMoney());
            values.put("fromAccount", transfer.getFromAccount().getId());
            values.put("toAccount", transfer.getToAccount().getId());
            values.put("transferTime", transfer.getTransferTime());
            values.put("updateTime", transfer.getUpdateTime());

            ContentValues valuesFrom = new ContentValues();
            valuesFrom.put("balance", transfer.getFromAccount().getAccountMoney() - transfer.getMoney());
            valuesFrom.put("updateTime", transfer.getUpdateTime());

            ContentValues valuesTo = new ContentValues();
            valuesTo.put("balance", transfer.getToAccount().getAccountMoney() + transfer.getMoney());
            valuesTo.put("updateTime", transfer.getUpdateTime());
            db.beginTransaction();
            try {
                db.insert("transfer", null, values);
                db.update("account", valuesFrom, "id = ?", new String[]{String.valueOf(transfer.getFromAccount().getId())});
                db.update("account", valuesTo, "id = ?", new String[]{String.valueOf(transfer.getToAccount().getId())});
                db.setTransactionSuccessful();
            } catch (Exception e) {

            } finally {
                db.endTransaction();
            }

        }
    }

    public void updateTransfer(Transfer oldTransfer, Transfer newTransfer) {
        if (newTransfer != null && oldTransfer != null) {
            ContentValues values = new ContentValues();
            values.put("money", newTransfer.getMoney());
            values.put("fromAccount", newTransfer.getFromAccount().getId());
            values.put("toAccount", newTransfer.getToAccount().getId());
            values.put("transferTime", newTransfer.getTransferTime());
            values.put("updateTime", newTransfer.getUpdateTime());
            db.beginTransaction();
            try {
                db.update("transfer", values, "id = ?", new String[]{String.valueOf(newTransfer.getId())});
                ContentValues valuesOld = new ContentValues();
                ContentValues valuesNew = new ContentValues();
                ContentValues valuesChange = new ContentValues();
                if (!oldTransfer.getFromAccount().getAccountName().equals(newTransfer.getFromAccount().getAccountName()) ||
                        !oldTransfer.getToAccount().getAccountName().equals(newTransfer.getToAccount().getAccountName())) {
                    //更改转出账户
                    if (!oldTransfer.getFromAccount().getAccountName().equals(newTransfer.getFromAccount().getAccountName())) {
                        valuesOld.put("balance", oldTransfer.getFromAccount().getAccountMoney() + oldTransfer.getMoney());
                        valuesOld.put("updateTime", newTransfer.getUpdateTime());
                        valuesNew.put("balance", newTransfer.getFromAccount().getAccountMoney() - newTransfer.getMoney());
                        valuesNew.put("updateTime", newTransfer.getUpdateTime());
                        valuesChange.put("balance", oldTransfer.getToAccount().getAccountMoney() - oldTransfer.getMoney() + newTransfer.getMoney());
                        valuesChange.put("updateTime", newTransfer.getUpdateTime());
                        db.update("account", valuesOld, "id = ?", new String[]{String.valueOf(oldTransfer.getFromAccount().getId())});
                        db.update("account", valuesNew, "id = ?", new String[]{String.valueOf(newTransfer.getFromAccount().getId())});
                        db.update("account", valuesChange, "id = ?", new String[]{String.valueOf(oldTransfer.getToAccount().getId())});
                        valuesOld.clear();
                        valuesNew.clear();
                    } else {
                        valuesNew.put("balance", oldTransfer.getFromAccount().getAccountMoney() + oldTransfer.getMoney() - newTransfer.getMoney());
                        valuesNew.put("updateTime", newTransfer.getUpdateTime());
                        db.update("account", valuesNew, "id = ?", new String[]{String.valueOf(newTransfer.getFromAccount().getId())});
                        valuesNew.clear();
                    }
                    //更改转入账户
                    if (!oldTransfer.getToAccount().getAccountName().equals(newTransfer.getToAccount().getAccountName())) {
                        valuesOld.put("balance", oldTransfer.getToAccount().getAccountMoney() - oldTransfer.getMoney());
                        valuesOld.put("updateTime", newTransfer.getUpdateTime());
                        valuesNew.put("balance", newTransfer.getToAccount().getAccountMoney() + newTransfer.getMoney());
                        valuesNew.put("updateTime", newTransfer.getUpdateTime());
                        valuesChange.put("balance", oldTransfer.getFromAccount().getAccountMoney() + oldTransfer.getMoney() - newTransfer.getMoney());
                        valuesChange.put("updateTime", newTransfer.getUpdateTime());
                        db.update("account", valuesOld, "id = ?", new String[]{String.valueOf(oldTransfer.getToAccount().getId())});
                        db.update("account", valuesNew, "id = ?", new String[]{String.valueOf(newTransfer.getToAccount().getId())});
                        db.update("account", valuesChange, "id = ?", new String[]{String.valueOf(oldTransfer.getFromAccount().getId())});
                        valuesOld.clear();
                        valuesNew.clear();
                    } else {
                        valuesNew.put("balance", oldTransfer.getToAccount().getAccountMoney() - oldTransfer.getMoney() + newTransfer.getMoney());
                        valuesNew.put("updateTime", newTransfer.getUpdateTime());
                        db.update("account", valuesNew, "id = ?", new String[]{String.valueOf(newTransfer.getToAccount().getId())});
                        valuesNew.clear();
                    }
                } else {
                    valuesNew.put("balance", oldTransfer.getFromAccount().getAccountMoney() + oldTransfer.getMoney() - newTransfer.getMoney());
                    valuesNew.put("updateTime", newTransfer.getUpdateTime());
                    db.update("account", valuesNew, "id = ?", new String[]{String.valueOf(oldTransfer.getFromAccount().getId())});
                    valuesNew.clear();
                    valuesNew.put("balance", oldTransfer.getToAccount().getAccountMoney() - oldTransfer.getMoney() + newTransfer.getMoney());
                    valuesNew.put("updateTime", newTransfer.getUpdateTime());
                    db.update("account", valuesNew, "id = ?", new String[]{String.valueOf(oldTransfer.getToAccount().getId())});
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
            } finally {
                db.endTransaction();
            }

        }
    }

    public void deleteTransfer(Transfer transfer) {
        if (transfer != null) {
            ContentValues valuesFrom = new ContentValues();
            valuesFrom.put("balance", transfer.getFromAccount().getAccountMoney() + transfer.getMoney());
            valuesFrom.put("updateTime", transfer.getUpdateTime());
            ContentValues valuesTo = new ContentValues();
            valuesTo.put("balance", transfer.getFromAccount().getAccountMoney() - transfer.getMoney());
            valuesTo.put("updateTime", transfer.getUpdateTime());
            db.beginTransaction();
            try {
                db.delete("transfer", "id = ?", new String[]{String.valueOf(transfer.getId())});
                db.update("account", valuesFrom, "id = ?", new String[]{String.valueOf(transfer.getFromAccount().getId())});
                db.update("account", valuesTo, "id = ?", new String[]{String.valueOf(transfer.getToAccount().getId())});
                db.setTransactionSuccessful();
            } catch (Exception e) {

            } finally {
                db.endTransaction();
            }
        }
    }
}
