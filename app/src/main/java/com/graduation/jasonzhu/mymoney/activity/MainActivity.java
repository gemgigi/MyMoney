package com.graduation.jasonzhu.mymoney.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.adapter.DayAccountExpandLvAdapter;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.fragment.AccountFragment;
import com.graduation.jasonzhu.mymoney.fragment.CategoryFragment;
import com.graduation.jasonzhu.mymoney.fragment.IncomeAndExpenseFragment;
import com.graduation.jasonzhu.mymoney.fragment.ReportFragment;
import com.graduation.jasonzhu.mymoney.model.TestData;
import com.graduation.jasonzhu.mymoney.util.MyApplication;
import com.graduation.jasonzhu.mymoney.util.TimeUtil;

import java.sql.Time;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView balanceTv;
    private static TabLayout tabLayout;
    private TabLayout reportTabLayout;
    private Fragment currentFragment;
    private IncomeAndExpenseFragment incomeAndExpenseFragment = new IncomeAndExpenseFragment();
    private AccountFragment accountFragment = new AccountFragment();
    private CategoryFragment categoryFragment = new CategoryFragment();
    private ReportFragment reportFragment = new ReportFragment();
    private boolean isFirstLoad = true;
    private MyMoneyDb myMoneyDb;
    private ProgressDialog progressDialog;
    private SharedPreferences sp;
    public static final int INCOME_EXPENSE_RESULT = 1;
    public static final int ACCOUNT_RESULT = 2;
    public static final int INCOME_CATEGORY_RESULT = 3;
    public static final int EXPENSE_CATEGORY_RESULT = 4;
    public static final int CATEGORY_RESULT = 5;
    private boolean isOperateOnIncomeExpense = false;
    private boolean isOperateOnAccount = false;
    private boolean isOperateOnIncomeCategory = false;
    private boolean isOperateOnExpenseCategory = false;
    private boolean isOperateOnCategory = false;


    public boolean isOperateOnCategory() {
        return isOperateOnCategory;
    }

    public void setIsOperateOnCategory(boolean isOperateOnCategory) {
        this.isOperateOnCategory = isOperateOnCategory;
    }

    public interface OperateListener {
        void onOperate(boolean value);
    }


    public void setIsOperateOnIncomeExpense(boolean isOperateOnIncomeExpense) {
        this.isOperateOnIncomeExpense = isOperateOnIncomeExpense;
    }

    public void setIsOperateOnAccount(boolean isOperateOnAccount) {
        this.isOperateOnAccount = isOperateOnAccount;
    }

    public boolean isOperateOnIncomeExpense() {
        return isOperateOnIncomeExpense;
    }

    public boolean isOperateOnAccount() {
        return isOperateOnAccount;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public boolean isOperateOnIncomeCategory() {
        return isOperateOnIncomeCategory;
    }

    public void setIsOperateOnIncomeCategory(boolean isOperateOnIncomeCategory) {
        this.isOperateOnIncomeCategory = isOperateOnIncomeCategory;
    }

    public boolean isOperateOnExpenseCategory() {
        return isOperateOnExpenseCategory;
    }

    public void setIsOperateOnExpenseCategory(boolean isOperateOnExpenseCategory) {
        this.isOperateOnExpenseCategory = isOperateOnExpenseCategory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        sp = getPreferences(Context.MODE_PRIVATE);
        isFirstLoad = sp.getBoolean("isFirstLoad", true);
        Log.d("isFirstLoad", "isFirstLoad = " + isFirstLoad);
        if (isFirstLoad) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("正在加载...");
                progressDialog.setCanceledOnTouchOutside(false);
                new LoadData().execute();

            }

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                //  startActivity(intent);
                startActivityForResult(intent, 1);
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
        reportTabLayout = (TabLayout) findViewById(R.id.mm_main_report_tab);
        balanceTv = (TextView) findViewById(R.id.mm_main_balance_tv);
        setSupportActionBar(toolbar);
    }

    public static TabLayout getTabLayout() {
        return tabLayout;
    }

    public TabLayout getReportTabLayout() {
        return reportTabLayout;
    }

    public TextView getBalanceTv() {
        return balanceTv;
    }

    @Override
    protected void onResume() {
        super.onResume();

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
            reportTabLayout.setVisibility(View.GONE);
//            balanceTv.setVisibility(View.GONE);
            switchFragment(accountFragment);
        } else if (id == R.id.mm_income_expenses) {
            setTitle(item.getTitle());
            tabLayout.setVisibility(View.GONE);
            reportTabLayout.setVisibility(View.GONE);
//            balanceTv.setVisibility(View.VISIBLE);
            switchFragment(incomeAndExpenseFragment);
        } else if (id == R.id.mm_category) {
            setTitle(item.getTitle());
            tabLayout.setVisibility(View.VISIBLE);
            reportTabLayout.setVisibility(View.GONE);
//            balanceTv.setVisibility(View.GONE);
            switchFragment(categoryFragment);
        } else if (id == R.id.mm_setting) {
            setTitle(item.getTitle());
        } else if (id == R.id.mm_report) {
            setTitle(item.getTitle());
            reportTabLayout.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.GONE);
            switchFragment(reportFragment);

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
        switch (resultCode) {
            case INCOME_EXPENSE_RESULT:
                Log.d("TAG", "main from income_expense");
                isOperateOnIncomeExpense = true;
                break;
            case ACCOUNT_RESULT:
                Log.d("TAG", "main account");
                isOperateOnAccount = true;
                break;
            case CATEGORY_RESULT:
                isOperateOnCategory = true;
                isOperateOnExpenseCategory = true;
                isOperateOnIncomeExpense = true;
                break;
        }
    }

    public class LoadData extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFirstLoad", false);
            editor.commit();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Void... params) {
            myMoneyDb = MyMoneyDb.getInstance(MyApplication.getContext());
            myMoneyDb.loadCategory(TestData.getCategoryList());
            return null;
        }
    }
}

