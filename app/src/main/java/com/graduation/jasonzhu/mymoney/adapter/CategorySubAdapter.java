package com.graduation.jasonzhu.mymoney.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.model.Category;

import java.util.List;

/**
 * Created by gemha on 2016/2/17.
 */
public class CategorySubAdapter extends BaseAdapter{
    private Context context;
    private List<Category> subList;
    private int mPosition = 0;


    public CategorySubAdapter(Context context, List<Category> subList) {
        this.context = context;
        this.subList = subList;
    }

    public int getCount() {
        return subList.size();
    }

    public Object getItem(int position) {
        return subList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Holder hold;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_category_sublist, null);
            hold = new Holder(view);
            view.setTag(hold);
        } else {
            view = convertView;
            hold = (Holder) view.getTag();
        }
        hold.txt.setText(subList.get(position).getName());
//        hold.txt.setTextColor(0xFF666666);
//        if (mPosition == position) {
//            hold.txt.setTextColor(0xFFFF8C00);
//        }
        return view;
    }

    public void setSelectItem(int position) {
        this.mPosition = position;
    }

    private static class Holder {
        TextView txt;

        public Holder(View view) {
            txt = (TextView) view.findViewById(R.id.category_subitem_tv);
        }
    }
}
