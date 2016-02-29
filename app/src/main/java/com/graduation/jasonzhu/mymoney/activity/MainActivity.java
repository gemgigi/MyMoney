package com.graduation.jasonzhu.mymoney.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.adapter.DayAccountExpandLvAdapter;
import com.graduation.jasonzhu.mymoney.fragment.AccountFragment;
import com.graduation.jasonzhu.mymoney.fragment.CategoryFragment;
import com.graduation.jasonzhu.mymoney.fragment.IncomeAndExpenseFragment;
import com.graduation.jasonzhu.mymoney.model.TestData;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ExpandableListView expandableListView;
    private DayAccountExpandLvAdapter dayAccountExpandLvAdapter;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private static TabLayout tabLayout;
    private Fragment currentFragment;
    private IncomeAndExpenseFragment incomeAndExpenseFragment = new IncomeAndExpenseFragment();
    private AccountFragment accountFragment = new AccountFragment();
    private CategoryFragment categoryFragment = new CategoryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                startActivity(intent);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //监听侧滑菜单中的item点
        navigationView.setNavigationItemSelectedListener(this);
        currentFragment = incomeAndExpenseFragment;
        getSupportFragmentManager().beginTransaction().add(R.id.main_content, currentFragment).commit();

    }
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tabLayout = (TabLayout) findViewById(R.id.mm_main_category_tab);
    }

    public static TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//      //  menu.clear();
//        Log.d("TAG","type = "+type);
//        switch (type){
//            case "我的账户":
//                getMenuInflater().inflate(R.menu.main_account, menu);
//                break;
//            case "我的收支":
//                getMenuInflater().inflate(R.menu.main, menu);
//                break;
//            case "收支分类":
//                break;
//        }
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.mm_account) {
            setTitle(item.getTitle());
            tabLayout.setVisibility(View.GONE);
            switchFragment(accountFragment);
        } else if (id == R.id.mm_income_expenses) {
            setTitle(item.getTitle());
            tabLayout.setVisibility(View.GONE);
            switchFragment(incomeAndExpenseFragment);
        } else if (id == R.id.mm_category) {
            setTitle(item.getTitle());
            tabLayout.setVisibility(View.VISIBLE);
            switchFragment(categoryFragment);
        } else if (id == R.id.mm_setting) {
            setTitle(item.getTitle());
        }
        navigationView.setCheckedItem(id);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void switchFragment(Fragment otherFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != otherFragment) {
            if (!otherFragment.isAdded()) {
                transaction.hide(currentFragment).add(R.id.main_content, otherFragment).commit();
            } else {
                transaction.hide(currentFragment).show(otherFragment).commit();
            }
            currentFragment = otherFragment;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    Log.d("TAG", "返回主Activity");
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
