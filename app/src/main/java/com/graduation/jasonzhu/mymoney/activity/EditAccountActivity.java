package com.graduation.jasonzhu.mymoney.activity;

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

public class EditAccountActivity extends AppCompatActivity {

    private TextView balenceTv;
    private EditText accountNameEt;
    private Button saveBtn;
    private Toolbar toolbar;
    private static final int RESULT_OK = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account);
        initView();

        balenceTv.setText(String.valueOf(getIntent().getFloatExtra("balence", 0.00f)));
        Intent intent = new Intent();
        intent.putExtra("type", "account");
       // setResult(RESULT_OK);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                Toast.makeText(this, "EditAccount", Toast.LENGTH_SHORT).show();
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
