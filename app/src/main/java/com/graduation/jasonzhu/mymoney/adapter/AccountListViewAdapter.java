package com.graduation.jasonzhu.mymoney.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.model.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by gemha on 2016/2/23.
 */
public class AccountListViewAdapter extends ArrayAdapter {

    private Context context;
    private int resourceId;
    private List<Account> accountList;


    public AccountListViewAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        this.accountList = objects;
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
        viewHolder.tv_categoryName.setText(accountList.get(position).getAccountName());
        BigDecimal bd = new BigDecimal(String.valueOf(accountList.get(position).getAccountMoney()));
        viewHolder.tv_categoryMoney.setText(bd.toPlainString());
        return view;
    }

}
