package com.graduation.jasonzhu.mymoney.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.adapter.ViewPagerAdapter;
import com.graduation.jasonzhu.mymoney.fragment.AddExchangeFragment;
import com.graduation.jasonzhu.mymoney.fragment.AddExpenseFragment;
import com.graduation.jasonzhu.mymoney.fragment.AddIncomeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gemha on 2016/2/16.
 */
public class AddRecordActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<View> views;
    private List<Fragment> fragments;
    private List<String> tabTitles;
    private Toolbar toolbar;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrecord);
        initViews();


    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.mm_addrecord_toolbar);
        setSupportActionBar(toolbar);
        LayoutInflater inflater = getLayoutInflater().from(this);
        fragments = new ArrayList<Fragment>();
        fragments.add(new AddExpenseFragment());
        fragments.add(new AddIncomeFragment());
        fragments.add(new AddExchangeFragment());
        tabTitles = new ArrayList<String>();
        tabTitles.add("支出");
        tabTitles.add("收入");
        tabTitles.add("转账");

        tabLayout = (TabLayout) findViewById(R.id.mm_addrecord_tab);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager = (ViewPager) findViewById(R.id.mm_addrecord_vp);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this,fragments,tabTitles);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public String getCurrentTab(){
        return tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString();
    }

}
