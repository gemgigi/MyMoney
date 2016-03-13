package com.graduation.jasonzhu.mymoney.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.adapter.AccountSpinnerAdapter;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.model.Account;
import com.graduation.jasonzhu.mymoney.model.Transfer;
import com.graduation.jasonzhu.mymoney.util.CalculatorView;
import com.graduation.jasonzhu.mymoney.util.MyApplication;
import com.graduation.jasonzhu.mymoney.util.TimeUtil;

import java.util.List;

public class EditExchangeActivity extends AppCompatActivity {
    private TextView moneyTv;
    private Spinner toSpinner;
    private Spinner fromSpinner;
    private AccountSpinnerAdapter spinnerAdapter;
    private TextView date_tv;
    private Button saveBtn;
    private String selectedTime;
    private List<Account> accountList;
    private Account fromAccount;
    private Account toAccount;
    private MyMoneyDb myMoneyDb;
    private Transfer oldTransfer;
    private Transfer newTransfer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.addexpense,null);
//        toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar,null);
//        linearLayout.addView(toolbar);
        setContentView(R.layout.edit_exchange);
        initData();
        initView();
        Intent intent = getIntent();
        oldTransfer = (Transfer) intent.getSerializableExtra("data");
        moneyTv.setText(String.valueOf(oldTransfer.getMoney()));
        fromAccount = oldTransfer.getFromAccount();
        toAccount = oldTransfer.getToAccount();
        setFromSpinnerSelected(fromAccount);
        setToSpinnerSelected(toAccount);
        date_tv.setText(oldTransfer.getTransferTime());
        moneyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CalculatorView calculatorView = new CalculatorView();
                AlertDialog.Builder builder = new AlertDialog.Builder(EditExchangeActivity.this) {
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
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toAccount = accountList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromAccount = accountList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditExchangeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (0 <= monthOfYear && monthOfYear < 9) {
                            if (0 < dayOfMonth && dayOfMonth < 10) {
                                selectedTime = year + "-" + 0 + (monthOfYear + 1) + "-" + "0" + dayOfMonth + " " + TimeUtil.getCurrentTime("HH:mm");
                            } else {
                                selectedTime = year + "-" + 0 + (monthOfYear + 1) + "-" + dayOfMonth + " " + TimeUtil.getCurrentTime("HH:m");
                            }
                        } else {
                            if (0 < dayOfMonth && dayOfMonth < 10) {
                                selectedTime = year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth + " " + TimeUtil.getCurrentTime("HH:mm");
                            } else {
                                selectedTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " " + TimeUtil.getCurrentTime("HH:mm");
                            }

                        }
                        date_tv.setText(selectedTime);
                    }
                }, Integer.valueOf(TimeUtil.getCurrentTime("yyyy"))
                        , Integer.valueOf(TimeUtil.getCurrentTime("M")) - 1, Integer.valueOf(TimeUtil.getCurrentTime("dd"))).show();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = moneyTv.getText().toString();
                if ("0.00".equals(money)) {
                    Toast.makeText(EditExchangeActivity.this, "还没设置金额呢！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (toAccount == fromAccount) {
                    Toast.makeText(EditExchangeActivity.this, "试试不同的账户吧！", Toast.LENGTH_SHORT).show();
                    return;
                }
                newTransfer = new Transfer();
                newTransfer.setId(oldTransfer.getId());
                newTransfer.setFromAccount(fromAccount);
                newTransfer.setToAccount(toAccount);
                newTransfer.setMoney(Float.valueOf(money));
                newTransfer.setTransferTime(date_tv.getText().toString());
                newTransfer.setUpdateTime(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
                myMoneyDb.updateTransfer(oldTransfer, newTransfer);
                finish();
            }
        });
    }

    private void setFromSpinnerSelected(Account fromAccount) {
        for (int i = 0; i < fromSpinner.getCount(); i++) {
            Account account = (Account) spinnerAdapter.getItem(i);
            if (fromAccount.getAccountName().equals(account.getAccountName())) {
                fromSpinner.setSelection(i, true);
            }
        }
    }

    private void setToSpinnerSelected(Account toAccount) {
        for (int i = 0; i < toSpinner.getCount(); i++) {
            Account account = (Account) spinnerAdapter.getItem(i);
            if (toAccount.getAccountName().equals(account.getAccountName())) {
                toSpinner.setSelection(i, true);
            }
        }
    }

    private void initData() {
        myMoneyDb = MyMoneyDb.getInstance(this);
        accountList = myMoneyDb.getAllAccount();
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        moneyTv = (TextView) findViewById(R.id.mm_editexchange_money_et);
        toSpinner = (Spinner) findViewById(R.id.mm_editexchange_into_account_spinner);
        fromSpinner = (Spinner) findViewById(R.id.mm_editexchange_out_account_spinner);
        spinnerAdapter = new AccountSpinnerAdapter(this, R.layout.item_select_account, accountList);
        toSpinner.setAdapter(spinnerAdapter);
        fromSpinner.setAdapter(spinnerAdapter);
        date_tv = (TextView) findViewById(R.id.mm_editexchange_date_tv);
        date_tv.setText(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
        saveBtn = (Button) findViewById(R.id.mm_editexchange_save);
        toolbar.setTitle("编辑转账");
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_activity_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditExchangeActivity.this)
                        .setTitle("删除提示")
                        .setMessage("是否删除该记录")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myMoneyDb = MyMoneyDb.getInstance(EditExchangeActivity.this);
                                myMoneyDb.deleteTransfer(oldTransfer);
                                Toast.makeText(EditExchangeActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
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
        return true;
    }
}
