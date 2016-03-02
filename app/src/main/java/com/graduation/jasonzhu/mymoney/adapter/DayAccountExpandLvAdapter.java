package com.graduation.jasonzhu.mymoney.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.model.IncomeAndExpense;
import com.graduation.jasonzhu.mymoney.model.Summary;
import com.graduation.jasonzhu.mymoney.util.MyApplication;
import com.graduation.jasonzhu.mymoney.util.TimeUtil;

import java.util.List;


/**
 * Created by gemha on 2016/2/19.
 */
public class DayAccountExpandLvAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Summary> expandListGroup;
    private List<List<IncomeAndExpense>> expandListChild;

    public DayAccountExpandLvAdapter(Context context, List<Summary> expandListGroup, List<List<IncomeAndExpense>> expandListChild) {
        this.context = context;
        this.expandListGroup = expandListGroup;
        this.expandListChild = expandListChild;
    }

    @Override
    public int getGroupCount() {
        return expandListGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expandListChild.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return expandListGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandListChild.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View groupView;
        GroupHolder groupHolder;
        if (convertView == null) {
            groupView = LayoutInflater.from(context).inflate(R.layout.main_expandlsit_group, null);
            groupHolder = new GroupHolder();
            groupHolder.month_tv = (TextView) groupView.findViewById(R.id.mm_main_list_month_tv);
            groupHolder.income_tv = (TextView) groupView.findViewById(R.id.mm_main_list_income_tv);
            groupHolder.expense_tv = (TextView) groupView.findViewById(R.id.mm_main_list_expense_tv);
            groupHolder.balance_tv = (TextView) groupView.findViewById(R.id.mm_main_list_balance_tv);
            groupView.setTag(groupHolder);
        } else {
            groupView = convertView;
            groupHolder = (GroupHolder) groupView.getTag();
        }
        groupHolder.month_tv.setText(expandListGroup.get(groupPosition).getMonth());
        groupHolder.income_tv.setText(expandListGroup.get(groupPosition).getIncome());
        groupHolder.expense_tv.setText(expandListGroup.get(groupPosition).getExpense());
        groupHolder.balance_tv.setText(expandListGroup.get(groupPosition).getBalance());
        return groupView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View childView;
        ItemHolder itemHolder;
        if (convertView == null) {
            childView = LayoutInflater.from(context).inflate(R.layout.main_expandlsit_item, null);
            itemHolder = new ItemHolder();
            itemHolder.day_tv = (TextView) childView.findViewById(R.id.mm_main_sublist_day_tv);
            itemHolder.category = (TextView) childView.findViewById(R.id.mm_main_sublist_category_tv);
            itemHolder.money = (TextView) childView.findViewById(R.id.mm_main_sublist_money_tv);
            childView.setTag(itemHolder);
        } else {
            childView = convertView;
            itemHolder = (ItemHolder) childView.getTag();
        }

        String currDay = TimeUtil.getDay(expandListChild.get(groupPosition).get(childPosition).getSaveTime());
        if (childPosition == 0) {
            itemHolder.day_tv.setText(currDay);
        } else {
            String beforeDay = TimeUtil.getDay(expandListChild.get(groupPosition).get(childPosition - 1).getSaveTime());


            if (beforeDay != null && !"".equals(beforeDay)) {
                if (!currDay.equals(beforeDay)) {
                    Log.d("TAG","currDay = "+ currDay+"-----"+"beforeDay = " + beforeDay);
                    itemHolder.day_tv.setText(currDay);
                } else {
                    itemHolder.day_tv.setText("");
                }
            }
        }
        itemHolder.category.setText(expandListChild.get(groupPosition).get(childPosition).getCategory().getCategoryList().get(0).getName());
        itemHolder.money.setText(String.valueOf(expandListChild.get(groupPosition).get(childPosition).getMoney()));
        switch (expandListChild.get(groupPosition).get(childPosition).getType()){
            case "支出":
                itemHolder.money.setTextColor(MyApplication.getContext().getResources().getColor(R.color.expense));
                break;
            case "收入":
                itemHolder.money.setTextColor(MyApplication.getContext().getResources().getColor(R.color.income));
                break;
        }

        return childView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

class GroupHolder {
    TextView month_tv;
    TextView income_tv;
    TextView expense_tv;
    TextView balance_tv;
}

class ItemHolder {
    TextView day_tv;
    TextView category;
    TextView money;
}

