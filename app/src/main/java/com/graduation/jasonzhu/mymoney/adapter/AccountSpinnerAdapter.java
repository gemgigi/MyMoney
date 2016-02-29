package com.graduation.jasonzhu.mymoney.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.model.Account;

import java.util.List;

/**
 * Created by gemha on 2016/2/17.
 */
public class AccountSpinnerAdapter extends BaseAdapter {

    private List<Account> values;
    private Context context;
    private int resourceId;

    public AccountSpinnerAdapter(Context context, int resource, List<Account> objects) {
        this.context = context;
        this.resourceId = resource;
        this.values = objects;
    }


    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        AccountViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, null);
            viewHolder = new AccountViewHolder();
            viewHolder.tv_categoryName = (TextView) view.findViewById(R.id.category_name_tv);
            viewHolder.tv_categoryMoney = (TextView) view.findViewById(R.id.category_money_tv);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (AccountViewHolder) view.getTag();
        }
        viewHolder.tv_categoryName.setText(values.get(position).getAccountName());
        viewHolder.tv_categoryMoney.setText(String.valueOf(values.get(position).getAccountMoney()));
        return view;
    }
}

