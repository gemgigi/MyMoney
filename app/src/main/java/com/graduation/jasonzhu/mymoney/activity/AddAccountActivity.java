package com.graduation.jasonzhu.mymoney.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.adapter.AccountListViewAdapter;
import com.graduation.jasonzhu.mymoney.model.TestData;

public class AddAccountActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView balenceTv;
    private Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account);
        initView();
    }

    private void initView() {
        saveBtn = (Button) findViewById(R.id.mm_add_accoount_save_btn);
        balenceTv = (TextView) findViewById(R.id.mm_add_account_balence);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("添加账户");
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return true;
    }
}
