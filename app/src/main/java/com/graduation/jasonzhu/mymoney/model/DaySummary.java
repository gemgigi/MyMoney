package com.graduation.jasonzhu.mymoney.model;

import java.util.List;

/**
 * Created by gemha on 2016/3/11.
 */
public class DaySummary {

    private List<IncomeAndExpense> incomeAndExpenseList ;
    private List<Transfer> transferList;

    public List<IncomeAndExpense> getIncomeAndExpenseList() {
        return incomeAndExpenseList;
    }

    public void setIncomeAndExpenseList(List<IncomeAndExpense> incomeAndExpenseList) {
        this.incomeAndExpenseList = incomeAndExpenseList;
    }

    public List<Transfer> getTransferList() {
        return transferList;
    }

    public void setTransferList(List<Transfer> transferList) {
        this.transferList = transferList;
    }
}
