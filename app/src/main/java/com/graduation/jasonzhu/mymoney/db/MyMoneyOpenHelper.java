package com.graduation.jasonzhu.mymoney.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gemha on 2016/3/1.
 */
public class MyMoneyOpenHelper extends SQLiteOpenHelper {


    private static final String CREATE_TABLE_ACCOUNT = "create table if not exists account(" +
            "id integer primary key autoincrement," +
            "accountName text not null unique," +
            "balance real not null default 0," +
            "syncTime text," +
            "updateTime text);";
    private static final String CREATE_TABLE_CATEGORY = "create table if not exists category(" +
            "id integer primary key autoincrement," +
            "type text not null," +
            "categoryName text not null unique," +
            "syncTime text," +
            "updateTime text," +
            "parentId integer," +
            "constraint fk_category foreign key(parentId) references category(id) on delete cascade on update cascade);";
    private static final String CREATE_TABLE_MONEY = "create table if not exists income_expense(" +
            "id integer primary key autoincrement," +
            "type text not null," +
            "money real," +
            "saveTime text," +
            "remark text," +
            "syncTime text," +
            "updateTime text," +
            "accountId integer," +
            "categoryId integer," +
            "constraint fk_money_account foreign key(accountId) references account(id) on delete cascade on update cascade," +
            "constraint fk_money_category foreign key(categoryId) references category(id) on delete cascade on update cascade) ;";
    private static final String CREATE_TABLE_TRANSFER = "create table if not exists transfer(" +
            "id integer primary key autoincrement," +
            "fromAccount integer," +
            "toAccount integer," +
            "money real," +
            "transferTime text," +
            "updateTime text," +
            "syncTime text," +
            "constraint fk_transfer_account1 foreign key(fromAccount) references account(id) on delete cascade on update cascade," +
            "constraint fk_transfer_account2 foreign key(toAccount) references account(id) on delete cascade on update cascade);";
    private static final String CREATE_TRIGGER_MONEY_INSERT = "create trigger if not exists income_expense_insert " +
            "after insert on income_expense" +
            "for each row" +
            "begin" +
            "UPDATE account set balance = balance + new.money where id= new.accountId;" +
            "end;";
    private static final String CREATE_TRIGGER_MONEY_UPDATE = "create trigger if not exists income_expense_update " +
            "after update on income_expense" +
            "for each row" +
            "begin" +
            "update account set balance = balance - old.money + new.money where id = new.accountId;" +
            "end;";
    private static final String CREATE_TRIGGER_MONEY_DELETE = "create trigger if not exists income_expense_delete " +
            "after delete on income_expense" +
            "for each row" +
            "begin" +
            "update account set balance = balance - old.money where id = old.accountId;" +
            "end;";

    public MyMoneyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNT);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_MONEY);
        db.execSQL(CREATE_TABLE_TRANSFER);

//        db.execSQL(CREATE_TRIGGER_MONEY_INSERT);
//        db.execSQL(CREATE_TRIGGER_MONEY_UPDATE);
//        db.execSQL(CREATE_TRIGGER_MONEY_DELETE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
