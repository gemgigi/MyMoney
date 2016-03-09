package com.graduation.jasonzhu.mymoney.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

import java.util.ArrayList;
import java.util.List;

public class EditAccountActivity extends AppCompatActivity {

    private TextView balenceTv;
    private EditText accountNameEt;
    private Button saveBtn;
    private Toolbar toolbar;
    private List<Account> accountList;
    private List<String> accountNameList = new ArrayList<>();
    private static final int RESULT_OK = 1;
    private MyMoneyDb myMoneyDb;
    private Account targetAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account);
        initView();
        Intent intent = getIntent();
        targetAccount = (Account) intent.getSerializableExtra("account");
        balenceTv.setText(String.valueOf(targetAccount.getAccountMoney()));
        accountNameEt.setText(targetAccount.getAccountName());
        accountList = (List<Account>) intent.getSerializableExtra("accountList");
        if (accountList.size() > 0) {
            for (Account account : accountList) {
                accountNameList.add(account.getAccountName());
            }
        }
//        Intent intent = new Intent();
//        intent.putExtra("type", "account");
//       // setResult(RESULT_OK);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = accountNameEt.getText().toString();
                String balance = balenceTv.getText().toString();
                if (name == null || "".equals(name)) {
                    Toast.makeText(EditAccountActivity.this, "账户名称不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (accountNameList.size() > 0) {
                    if (accountNameList.contains(name)) {
                        Toast.makeText(EditAccountActivity.this, "对不起，账户名称已存在！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                myMoneyDb = MyMoneyDb.getInstance(EditAccountActivity.this);
                targetAccount.setAccountMoney(Float.valueOf(balance));
                targetAccount.setAccountName(name);
                targetAccount.setUpdateTime(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
                myMoneyDb.updateAccount(targetAccount);
                Toast.makeText(EditAccountActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void initView() {
        balenceTv = (TextView) findViewById(R.id.mm_edit_account_balence);
        accountNameEt = (EditText) findViewById(R.id.mm_edit_account_accountname_et);
        saveBtn = (Button) findViewById(R.id.mm_edit_accoount_save_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("编辑账户");
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(EditAccountActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditAccountActivity.this)
                        .setTitle("注意！")
                        .setMessage("删除该账户，也会删除其下的流水账")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myMoneyDb = MyMoneyDb.getInstance(EditAccountActivity.this);
                                myMoneyDb.deleteAccount(targetAccount);
                                Toast.makeText(EditAccountActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_activity_menu, menu);
        return true;
    }

}
