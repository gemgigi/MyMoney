package com.graduation.jasonzhu.mymoney.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.activity.AddAccountActivity;
import com.graduation.jasonzhu.mymoney.activity.AddCategoryActivity;
import com.graduation.jasonzhu.mymoney.activity.MainActivity;
import com.graduation.jasonzhu.mymoney.adapter.ViewPagerAdapter;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.model.Category;
import com.graduation.jasonzhu.mymoney.model.TestData;
import com.graduation.jasonzhu.mymoney.util.LoadDataCallBackListener;
import com.graduation.jasonzhu.mymoney.util.MyApplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gemha on 2016/2/27.
 */
public class CategoryFragment extends Fragment {

    private View rootView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> tabTitles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;
    private static final String TAG = "TEST";
    private MyMoneyDb myMoneyDb;
    private ExpenseCategoryFragment expenseCategoryFragment;
    private IncomeCategoryFragment incomeCategoryFragment;
    private List<Category> allCateogrys;
    private List<String> allCateogryName;
    private List<Category> incomeCategoryList = new ArrayList<>();
    private List<Category> expenseCategoryList = new ArrayList<>();


    public List<String> getAllCateogryName() {
        myMoneyDb = MyMoneyDb.getInstance(getContext());
        allCateogryName = myMoneyDb.getAllCategoryName();
        return allCateogryName;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "CategoryFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "CategoryFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "CategoryFragment onCreateView");
        initView();
        initData();
        setHasOptionsMenu(true);
        return rootView;
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    private void initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.category_overview, null);
        tabLayout = MainActivity.getTabLayout();
        viewPager = (ViewPager) rootView.findViewById(R.id.mm_main_category_vp);
        expenseCategoryFragment = new ExpenseCategoryFragment();
        incomeCategoryFragment = new IncomeCategoryFragment();
        fragments.add(incomeCategoryFragment);
        fragments.add(expenseCategoryFragment);
        tabTitles.add("收入");
        tabTitles.add("支出");
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), getContext(), fragments, tabTitles);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_category, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_category_add) {
            Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
            String type = (String) tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText();
            intent.putExtra("type", type);
            if ("收入".equals(type)) {
                intent.putStringArrayListExtra("categoryList", (ArrayList<String>) getAllCateogryName());
            }
            if ("支出".equals(type)) {
                intent.putStringArrayListExtra("categoryList", (ArrayList<String>) getAllCateogryName());
            }
            startActivityForResult(intent, 1);
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "CategoryFragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "CategoryFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "CategoryFragment onResume");
//        if(tabLayout!=null){
//            tabLayout.setVisibility(View.VISIBLE);
//            Log.d(TAG, "CategoryFragment tabLayout VISIBLE");
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.d(TAG, "CategoryFragment onPause");
//        if(tabLayout!=null){
//            tabLayout.removeAllTabs();
//            tabLayout.setVisibility(View.GONE);
//            Log.d(TAG, "CategoryFragment tabLayout GONE");
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "CategoryFragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "CategoryFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "CategoryFragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "CategoryFragment onDetach");
    }
}
