package com.graduation.jasonzhu.mymoney.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.model.Account;
import com.graduation.jasonzhu.mymoney.util.TimeUtil;
import com.graduation.jasonzhu.mymoney.util.CalculatorView;

import java.util.ArrayList;
import java.util.List;

public class AddAccountActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView balenceTv;
    private EditText accountNameEt;
    private Button saveBtn;
    private MyMoneyDb myMoneyDb;
    private List<Account> accountList;
    private List<String> accountNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account);
        initView();
        Intent intent = getIntent();
        accountList = (List<Account>) intent.getSerializableExtra("accountList");
        if (accountList.size() > 0) {
            for (Account account : accountList) {
                accountNameList.add(account.getAccountName());
            }
        }
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = accountNameEt.getText().toString();
                String balance = balenceTv.getText().toString();
                if (name == null || "".equals(name)) {
                    Toast.makeText(AddAccountActivity.this, "账户名称不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (accountNameList.size() > 0) {
                    if (accountNameList.contains(name)) {
                        Toast.makeText(AddAccountActivity.this, "对不起，账户名称已存在！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                myMoneyDb = MyMoneyDb.getInstance(AddAccountActivity.this);
                Account account = new Account();
                account.setAccountName(name);
                account.setAccountMoney(Float.valueOf(balance));
                account.setUpdateTime(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
                myMoneyDb.insertAccount(account);
                Toast.makeText(AddAccountActivity.this,"添加成功！",Toast.LENGTH_SHORT).show();
                setResult(2,getIntent());
                finish();
            }
        });
        balenceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CalculatorView calculatorView = new CalculatorView();
                AlertDialog.Builder builder = new AlertDialog.Builder(AddAccountActivity.this) {
                    @Override
                    public AlertDialog show() {
                        setView(calculatorView.getCalculatoView(AddAccountActivity.this));
                        return super.show();
                    }
                };
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        balenceTv.setText(calculatorView.getResultTv().getText());
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
    }

    private View initCalculator() {
        View calculatorView = getLayoutInflater().inflate(R.layout.calculator_layout, null);
        return calculatorView;
    }

    private void initView() {
        saveBtn = (Button) findViewById(R.id.mm_add_accoount_save_btn);
        balenceTv = (TextView) findViewById(R.id.mm_add_account_balence);
        accountNameEt = (EditText) findViewById(R.id.mm_add_account_accountname_et);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("添加账户");
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }


}
