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
import com.graduation.jasonzhu.mymoney.adapter.CategorySpinner;
import com.graduation.jasonzhu.mymoney.model.Category;

public class EditCategoryActivity extends AppCompatActivity {

    private Spinner classficationSpinner;
    private Spinner mainClassficationSpinner;
    private EditText categoryNameEt;
    private Button saveBtn;
    private ArrayAdapter spinnerAdapter;
    private Toolbar toolbar;
    private String categoryType;
    private String classfication;
    private LinearLayout typeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_operate);

        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra("data");
        categoryType = category.getType();
        classfication = intent.getStringExtra("type");
        initView();
        categoryNameEt.setText(category.getName());
        setSpinnerItemSelectedByValue(classfication);
        if ("二级类别".equals(classfication)) {
            typeLayout.setVisibility(View.VISIBLE);
        }else if("一级类别".equals(classfication)){
            typeLayout.setVisibility(View.GONE);
        }

        classficationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] classfications = getResources().getStringArray(R.array.categoryType);
                if ("一级类别".equals(classfications[position])) {
                    typeLayout.setVisibility(View.GONE);
                }
                if ("二级类别".equals(classfications[position])) {
                    typeLayout.setVisibility(View.VISIBLE);
                }
                classfication = classfications[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //设置获取到的账户为选中项
    public void setSpinnerItemSelectedByValue(String value) {
        String[] types = getResources().getStringArray(R.array.categoryType);
        if (types[0].equals(value)) {
            classficationSpinner.setSelection(0, true);// 默认选中项
        } else if (types[1].equals(value)) {
            classficationSpinner.setSelection(1, true);// 默认选中项
        }

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        typeLayout = (LinearLayout) findViewById(R.id.maintype_layout);
        classficationSpinner = (Spinner) findViewById(R.id.mm_operate_category_type_spinner);
        mainClassficationSpinner = (Spinner) findViewById(R.id.mm_operate_category_maintype_spinner);
        categoryNameEt = (EditText) findViewById(R.id.mm_operate_category_name_et);
        saveBtn = (Button) findViewById(R.id.mm_operate_category_save_btn);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categoryType, android.R.layout.simple_list_item_1);
        classficationSpinner.setAdapter(spinnerAdapter);

//        categorySpinner = new CategorySpinner(this, R.layout.item_maincategory, mainCategoryList);
//        mainClassficationSpinner.setAdapter(categorySpinner);

        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setTitle("编辑" + categoryType + "类别");
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
