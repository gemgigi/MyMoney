package com.graduation.jasonzhu.mymoney.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.adapter.AccountSpinnerAdapter;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.model.Account;
import com.graduation.jasonzhu.mymoney.model.Category;
import com.graduation.jasonzhu.mymoney.model.IncomeAndExpense;
import com.graduation.jasonzhu.mymoney.model.TestData;
import com.graduation.jasonzhu.mymoney.util.CalculatorView;
import com.graduation.jasonzhu.mymoney.util.CategoryListView;
import com.graduation.jasonzhu.mymoney.util.CategorySelectedListener;
import com.graduation.jasonzhu.mymoney.util.TimeUtil;

import java.util.List;

public class EditIncomeActivity extends AppCompatActivity {
    private TextView moneyTv;
    private View categoryLayout;
    private Spinner accountSpinner;
    private EditText remarkEt;
    private TextView mainCategoryTv;
    private TextView subCategoryTv;
    private TextView dateTv;
    private Button saveBtn;
    private Button deleteBtn;
    private Toolbar toolbar;
    private AccountSpinnerAdapter spinnerAdapter;
    private String type;
    private IncomeAndExpense targetIncomeAndExpanse;
    private IncomeAndExpense newIncomeAndExpense;
    private MyMoneyDb myMoneyDb;
    private List<Category> mainCategoryList;
    private List<Category> subCategoryList;
    private List<Account> accountList;
    private String selectedTime;
    private Account selectedAccount;
    private Category seledtedCategory;
    private AlertDialog alertDialog;
    private CategoryListView categoryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_income);
        Intent intent = getIntent();
        targetIncomeAndExpanse = (IncomeAndExpense) intent.getSerializableExtra("data");
        type = targetIncomeAndExpanse.getType();
        initData();
        initView();
        categoryListView = new CategoryListView(this, type, mainCategoryTv, subCategoryTv);
        moneyTv.setText(String.valueOf(targetIncomeAndExpanse.getMoney()));
        setSpinnerItemSelectedByValue(targetIncomeAndExpanse.getAccount().getAccountName());
        dateTv.setText(targetIncomeAndExpanse.getSaveTime());
        mainCategoryTv.setText(targetIncomeAndExpanse.getCategory().getName());
        subCategoryTv.setText(targetIncomeAndExpanse.getCategory().getCategoryList().get(0).getName());
        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAccount = accountList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditIncomeActivity.this) {
                    @Override
                    public AlertDialog show() {
                        setView(categoryListView.getCategoryView(new CategorySelectedListener() {
                            @Override
                            public void onFinish() {
                                alertDialog.dismiss();
                            }
                        }));
                        return super.show();
                    }
                };
                alertDialog = builder.show();

            }

        });
        moneyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CalculatorView calculatorView = new CalculatorView();
                AlertDialog.Builder builder = new AlertDialog.Builder(EditIncomeActivity.this) {
                    @Override
                    public AlertDialog show() {
                        setView(calculatorView.getCalculatoView(getContext()));
                        return super.show();
                    }
                };
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moneyTv.setText(calculatorView.getResultTv().getText());
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditIncomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (0 <= monthOfYear && monthOfYear < 9) {
                            if (0 < dayOfMonth && dayOfMonth < 10) {
                                selectedTime = year + "-" + 0 + (monthOfYear + 1) + "-" + "0" + dayOfMonth + " " + TimeUtil.getCurrentTime("H:m");
                            } else {
                                selectedTime = year + "-" + 0 + (monthOfYear + 1) + "-" + dayOfMonth + " " + TimeUtil.getCurrentTime("H:m");
                            }
                        } else {
                            if (0 < dayOfMonth && dayOfMonth < 10) {
                                selectedTime = year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth + " " + TimeUtil.getCurrentTime("H:m");
                            } else {
                                selectedTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " " + TimeUtil.getCurrentTime("H:m");
                            }
                        }
                        dateTv.setText(selectedTime);
                    }
                }, Integer.valueOf(TimeUtil.getCurrentTime("yyyy"))
                        , Integer.valueOf(TimeUtil.getCurrentTime("M")) - 1, Integer.valueOf(TimeUtil.getCurrentTime("dd"))).show();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newIncomeAndExpense = new IncomeAndExpense();
                if (categoryListView.getSelectedCategory() != null) {
                    newIncomeAndExpense.setCategory(categoryListView.getSelectedCategory());
                } else {
                    newIncomeAndExpense.setCategory(targetIncomeAndExpanse.getCategory().getCategoryList().get(0));
                }
                if (selectedAccount != null) {
                    newIncomeAndExpense.setAccount(selectedAccount);
                } else {
                    newIncomeAndExpense.setAccount(targetIncomeAndExpanse.getAccount());
                }
                if (selectedTime != null) {
                    newIncomeAndExpense.setSaveTime(dateTv.getText().toString());
                } else {
                    newIncomeAndExpense.setSaveTime(targetIncomeAndExpanse.getSaveTime());
                }
                String money = moneyTv.getText().toString();
                String remark = remarkEt.getText().toString();
                newIncomeAndExpense.setId(targetIncomeAndExpanse.getId());
                newIncomeAndExpense.setMoney(Float.valueOf(money));
                newIncomeAndExpense.setRemark(remark);
                newIncomeAndExpense.setUpdateTime(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
                myMoneyDb = MyMoneyDb.getInstance(EditIncomeActivity.this);
                myMoneyDb.updateIncomeAndExpense(targetIncomeAndExpanse, newIncomeAndExpense);
                setResult(1, getIntent());
                finish();
            }
        });
    }

    private void initData() {
        myMoneyDb = MyMoneyDb.getInstance(this);
        mainCategoryList = myMoneyDb.getMainCategory(type);
        accountList = myMoneyDb.getAllAccount();
    }

    private void initView() {
        moneyTv = (TextView) findViewById(R.id.mm_edit_income_money_et);
        categoryLayout = findViewById(R.id.mm_edit_income_category_layout);
        mainCategoryTv = (TextView) findViewById(R.id.mm_edit_income_maincategory_tv);
        subCategoryTv = (TextView) findViewById(R.id.mm_edit_income_subcategory_tv);
        accountSpinner = (Spinner) findViewById(R.id.mm_edit_income_account_spinner);
        dateTv = (TextView) findViewById(R.id.mm_edit_income_date_tv);
        remarkEt = (EditText) findViewById(R.id.mm_edit_income_remark);
        saveBtn = (Button) findViewById(R.id.mm_edit_income_save_btn);
   // deleteBtn = (Button) findViewById(R.id.mm_edit_income_delete_btn);
        toolbar = (Toolbar) findViewById(R.id.edit_income_toolbar);
        setSupportActionBar(toolbar);
        setTitle("编辑" + type);
        spinnerAdapter = new AccountSpinnerAdapter(this, R.layout.item_select_account, accountList);
        accountSpinner.setAdapter(spinnerAdapter);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
    }

    public void setSpinnerItemSelectedByValue(String value) {
        for (int i = 0; i < spinnerAdapter.getCount(); i++) {
            Account account = (Account) spinnerAdapter.getItem(i);
            if (value.equals(account.getAccountName())) {
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
                finish();
                break;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditIncomeActivity.this)
                        .setTitle("删除提示")
                        .setMessage("是否删除该记录")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myMoneyDb = MyMoneyDb.getInstance(EditIncomeActivity.this);
                                myMoneyDb.deleteIncomeAndExpense(targetIncomeAndExpanse);
                                Toast.makeText(EditIncomeActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                setResult(1, getIntent());
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
