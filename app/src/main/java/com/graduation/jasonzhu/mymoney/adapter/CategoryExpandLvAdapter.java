package com.graduation.jasonzhu.mymoney.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.model.Category;
import com.graduation.jasonzhu.mymoney.model.IncomeAndExpense;

import java.util.List;

/**
 * Created by gemha on 2016/2/29.
 */
public class CategoryExpandLvAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Category> expandList;

    public CategoryExpandLvAdapter(Context context,List<Category> expandList){
        this.context = context;
        this.expandList = expandList;
    }

    @Override
    public int getGroupCount() {
        return expandList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expandList.get(groupPosition).getCategoryList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return expandList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandList.get(groupPosition).getCategoryList().get(childPosition);
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
        View view;
        CategoryGroupHolder categoryGroupHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.category_expandlist_group, null);
            categoryGroupHolder = new CategoryGroupHolder();
            categoryGroupHolder.categoryName = (TextView) view.findViewById(R.id.category_overview_group_tv);
            view.setTag(categoryGroupHolder);
        } else{
            view = convertView;
            categoryGroupHolder = (CategoryGroupHolder) view.getTag();
        }
        categoryGroupHolder.categoryName.setText(expandList.get(groupPosition).getName());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        CategoryChildHolder categoryChildHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.category_expandlist_sub, null);
            categoryChildHolder = new CategoryChildHolder();
            categoryChildHolder.categoryName = (TextView) view.findViewById(R.id.category_overview_sub_tv);
            view.setTag(categoryChildHolder);
        } else{
            view = convertView;
            categoryChildHolder = (CategoryChildHolder) view.getTag();
        }
        categoryChildHolder.categoryName.setText("- "+expandList.get(groupPosition).getCategoryList().get(childPosition).getName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }
}

class CategoryGroupHolder {
    TextView categoryName;
}

class CategoryChildHolder {
    TextView categoryName;
}
