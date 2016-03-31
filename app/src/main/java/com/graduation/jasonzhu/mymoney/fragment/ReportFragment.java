package com.graduation.jasonzhu.mymoney.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.activity.MainActivity;
import com.graduation.jasonzhu.mymoney.adapter.ViewPagerAdapter;
import com.graduation.jasonzhu.mymoney.util.ReportViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 3/29/16.
 */
public class ReportFragment extends Fragment {


    private View rootView;
    private ViewPager viewPager;
    private ReportViewPager reportViewPager;
    private TabLayout tabLayout;
    private List<String> tabTitles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;
    private IncomeReportFragment incomeReportFragment;
    private ExpenseReportFragment expenseReportFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
        return rootView;
    }

    private void initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.report_overview, null);
        viewPager = (ViewPager) rootView.findViewById(R.id.mm_main_report_vp);
        reportViewPager = (ReportViewPager) rootView.findViewById(R.id.mm_main_report_vp);
        tabLayout = ((MainActivity)getActivity()).getReportTabLayout();
        tabTitles.add("收入");
        tabTitles.add("支出");
        incomeReportFragment = new IncomeReportFragment();
        expenseReportFragment = new ExpenseReportFragment();
        fragments.add(incomeReportFragment);
        fragments.add(expenseReportFragment);
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), getContext(), fragments, tabTitles);

//        viewPager.setAdapter(viewPagerAdapter);
        reportViewPager.setChildId(R.id.report_piechart);
        reportViewPager.setAdapter(viewPagerAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(reportViewPager);

    }
}
