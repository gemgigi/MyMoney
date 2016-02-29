package com.graduation.jasonzhu.mymoney.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.adapter.AccountSpinnerAdapter;
import com.graduation.jasonzhu.mymoney.model.Account;
import com.graduation.jasonzhu.mymoney.model.IncomeAndExpense;
import com.graduation.jasonzhu.mymoney.model.TestData;

public class EditIncomeActivity extends AppCompatActivity {
    private EditText moneyEt;
    private Spinner accountSpinner;
    private EditText remarkEt;
    private TextView mainCategoryTv;
    private TextView subCategoryTv;
    private TextView timeTv;
    private Button confirmBtn;
    private Button deleteBtn;
    private Toolbar toolbar;
    private AccountSpinnerAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_income);
        initView();
        Intent intent = getIntent();
        IncomeAndExpense incomeAndExpense = (IncomeAndExpense) intent.getSerializableExtra("income_data");
        //Log.d("edit_expense","money = "+incomeAndExpense.getMoney());
        moneyEt.setText(String.valueOf(incomeAndExpense.getMoney()));
        setSpinnerItemSelectedByValue(incomeAndExpense.getAccount().getAccountName());
        timeTv.setText(incomeAndExpense.getTime());
        mainCategoryTv.setText(incomeAndExpense.getCategory().getName());
        subCategoryTv.setText(incomeAndExpense.getCategory().getCategoryList().get(0).getName());
    }

    private void initView() {
        moneyEt = (EditText) findViewById(R.id.mm_edit_income_money_et);
        mainCategoryTv = (TextView) findViewById(R.id.mm_edit_income_maincategory_tv);
        subCategoryTv = (TextView) findViewById(R.id.mm_edit_income_subcategory_tv);
        accountSpinner = (Spinner) findViewById(R.id.mm_edit_income_account_spinner);
        timeTv = (TextView) findViewById(R.id.mm_edit_income_date_tv);
        remarkEt = (EditText) findViewById(R.id.mm_edit_income_remark);
        confirmBtn = (Button) findViewById(R.id.mm_edit_income_save_btn);
        deleteBtn = (Button) findViewById(R.id.mm_edit_income_delete_btn);
        toolbar = (Toolbar) findViewById(R.id.edit_income_toolbar);
        setSupportActionBar(toolbar);
        setTitle("编辑收入");
        spinnerAdapter = new AccountSpinnerAdapter(this,R.layout.item_select_account, TestData.getAccounts());
        accountSpinner.setAdapter(spinnerAdapter);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
    }

    public void setSpinnerItemSelectedByValue(String value){
        int k= spinnerAdapter.getCount();
        for(int i=0;i<k;i++){
            Account account = (Account) spinnerAdapter.getItem(i);
            Log.d("edit_expense", account.getAccountName());
            if(value.equals(account.getAccountName())){
                accountSpinner.setSelection(i, true);// 默认选中项
                break;
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_activity_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(EditIncomeActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.action_delete:
                Toast.makeText(this,"EditIncome",Toast.LENGTH_SHORT).show();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
