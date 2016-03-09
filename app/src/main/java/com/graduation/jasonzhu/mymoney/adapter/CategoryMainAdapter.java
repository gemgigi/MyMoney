package com.graduation.jasonzhu.mymoney.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.model.Category;

import java.util.List;
import java.util.Map;

/**
 * Created by gemha on 2016/2/17.
 */
public class CategoryMainAdapter extends BaseAdapter {
    private Context context;
    private List<Category> list;
    private int mPosition = 0;
    private boolean islodinging = true;


    public CategoryMainAdapter(Context context, List<Category> list) {
        this.context = context;
        this.list = list;
    }

    public CategoryMainAdapter(Context context, List<Category> list,
                               boolean islodinging) {
        this.context = context;
        this.list = list;
        this.islodinging = islodinging;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;
        Holder hold;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_category_mainlist, null);
            hold = new Holder(view);
            view.setTag(hold);
        } else {
            view = convertView;
            hold = (Holder) view.getTag();
        }
//        if (islodingimg == true) {
//            hold.img.setImageResource(Integer.parseInt(list.get(arg0)
//                    .get("img").toString()));
//        }
        hold.txt.setText(list.get(position).getName());
//        hold.layout.setBackgroundColor(0xFFEBEBEB);
//        if (position == mPosition) {
//            hold.layout.setBackgroundColor(0xFFFFFFFF);
//        }
        return view;
    }

    public void setSelectItem(int position) {
        this.mPosition = position;
    }

    public int getSelectItem() {
        return mPosition;
    }

    private static class Holder {
        LinearLayout layout;
        ImageView img;
        TextView txt;

        public Holder(View view) {
              txt = (TextView) view.findViewById(R.id.category_mainitem_tv);
//            img = (ImageView) view.findViewById(R.id.mainitem_img);
//            layout = (LinearLayout) view.findViewById(R.id.mainitem_layout);
        }
    }
}
