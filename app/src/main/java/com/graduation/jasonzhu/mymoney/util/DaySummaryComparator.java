package com.graduation.jasonzhu.mymoney.util;

import com.graduation.jasonzhu.mymoney.model.IncomeAndExpense;

import java.util.Comparator;

/**
 * Created by gemha on 2016/3/11.
 */
public class DaySummaryComparator implements Comparator {

    @Override
    public int compare(Object object1, Object object2) {
        return TimeUtil.formatTime(((IncomeAndExpense) object2).getSaveTime()) -
                TimeUtil.formatTime(((IncomeAndExpense) object1).getSaveTime());
    }

    @Override
    public boolean equals(Object object) {
        return false;
    }
}
