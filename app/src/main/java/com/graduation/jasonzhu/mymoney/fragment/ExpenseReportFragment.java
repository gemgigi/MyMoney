package com.graduation.jasonzhu.mymoney.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.graduation.jasonzhu.mymoney.R;

/**
 * Created by jason on 3/29/16.
 */
public class ExpenseReportFragment extends Fragment {

    private View rootView;
    private PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
        return rootView;
    }

    private void initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.report_main, null);
        pieChart = (PieChart) rootView.findViewById(R.id.report_piechart);

       // pieChart.setTouchEnabled(false);
    }
}
