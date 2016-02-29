package com.graduation.jasonzhu.mymoney.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.util.MyApplication;

/**
 * Created by gemha on 2016/2/28.
 */
public class IncomeCategoryFragment extends Fragment {
    private View rootView;
    private ExpandableListView expandableListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
        return rootView;
    }

    private void initView() {
        rootView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.category_income_list,null);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.mm_main_category_income_list);
    }
}
