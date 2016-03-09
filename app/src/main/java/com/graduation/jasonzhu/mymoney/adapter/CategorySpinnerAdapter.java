package com.graduation.jasonzhu.mymoney.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.model.Category;

import java.util.List;

/**
 * Created by gemha on 2016/3/3.
 */
public class CategorySpinnerAdapter extends BaseAdapter {

    private Context context;
    private int resourceId;
    private List<Category> categoryList;

    public CategorySpinnerAdapter(Context context, int resource, List<Category> objects) {
        this.context = context;
        this.resourceId = resource;
        this.categoryList = objects;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        CategoryHolder categoryHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId,null);
            categoryHolder = new CategoryHolder();
            categoryHolder.textView = (TextView) view.findViewById(R.id.mm_operate_category_maincategory_tv);
            view.setTag(categoryHolder);
        }else{
            view = convertView;
            categoryHolder = (CategoryHolder) view.getTag();
        }
        categoryHolder.textView.setText(categoryList.get(position).getName());
        return view;
    }
}
class CategoryHolder{
    TextView textView;
}
