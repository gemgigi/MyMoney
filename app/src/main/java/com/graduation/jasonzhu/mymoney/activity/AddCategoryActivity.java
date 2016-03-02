package com.graduation.jasonzhu.mymoney.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.model.Category;

import java.util.List;

public class AddCategoryActivity extends AppCompatActivity {
    private Spinner categoryTypeSpinner;
    private Spinner categoryMainTypeSp;
    private EditText categoryNameEt;
    private Button saveBtn;
    private ArrayAdapter spinnerAdapter;
    private Toolbar toolbar;
    private String type;
    private List<Category> mainCategoryList;
    private LinearLayout typeLayout;
    private MyMoneyDb myMoneyDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_operate);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        initData();
        initView();
        categoryTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] types = getResources().getStringArray(R.array.categoryType);
                if ("二级类别".equals(types[position])) {
                    typeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initData() {
        myMoneyDb = MyMoneyDb.getInstance(this);
        //查询主分类
        mainCategoryList = myMoneyDb.getMainCategory();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        typeLayout = (LinearLayout) findViewById(R.id.maintype_layout);
        categoryTypeSpinner = (Spinner) findViewById(R.id.mm_operate_category_type_spinner);
        categoryMainTypeSp = (Spinner) findViewById(R.id.mm_operate_category_maintype_spinner);
        categoryNameEt = (EditText) findViewById(R.id.mm_operate_category_name_et);
        saveBtn = (Button) findViewById(R.id.mm_operate_category_save_btn);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categoryType, android.R.layout.simple_list_item_1);
        categoryTypeSpinner.setAdapter(spinnerAdapter);
        //categoryMainTypeSp.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mainCategoryList));
        toolbar.setTitle("添加" + type + "类别");
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_activity_menu, menu);
        return true;
    }
}
