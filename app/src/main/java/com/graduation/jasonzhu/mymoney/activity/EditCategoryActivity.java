package com.graduation.jasonzhu.mymoney.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.model.Category;

public class EditCategoryActivity extends AppCompatActivity {

    private Spinner categoryTypeSpinner;
    private EditText categoryNameEt;
    private Button saveBtn;
    private ArrayAdapter spinnerAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_operate);
        initView();
        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra("data");
        categoryNameEt.setText(category.getName());
        setSpinnerItemSelectedByValue(intent.getStringExtra("type"));
    }

    //设置获取到的账户为选中项
    public void setSpinnerItemSelectedByValue(String value) {
        String[] types = getResources().getStringArray(R.array.categoryType);
        if (types[0].equals(value)) {
            categoryTypeSpinner.setSelection(0, true);// 默认选中项
        } else if(types[1].equals(value)){
            categoryTypeSpinner.setSelection(1, true);// 默认选中项
        }

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        categoryTypeSpinner = (Spinner) findViewById(R.id.mm_operate_category_type_spinner);
        categoryNameEt = (EditText) findViewById(R.id.mm_operate_category_name_et);
        saveBtn = (Button) findViewById(R.id.mm_operate_category_save_btn);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categoryType, android.R.layout.simple_list_item_1);
        categoryTypeSpinner.setAdapter(spinnerAdapter);
        toolbar.setTitle("编辑类别");
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
