package com.graduation.jasonzhu.mymoney.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.adapter.CategorySpinnerAdapter;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.model.Account;
import com.graduation.jasonzhu.mymoney.model.Category;
import com.graduation.jasonzhu.mymoney.util.MyApplication;
import com.graduation.jasonzhu.mymoney.util.TimeUtil;

import java.util.List;

public class AddCategoryActivity extends AppCompatActivity {
    private Spinner classficationSpinner;
    private Spinner mainClassficationSpinner;
    private EditText categoryNameEt;
    private Button saveBtn;
    private ArrayAdapter spinnerAdapter;
    private Toolbar toolbar;
    private String categoryType;
    private List<Category> mainCategoryList;
    private List<String> categoryList;
    private LinearLayout typeLayout;
    private MyMoneyDb myMoneyDb;
    private String classfication;
    private Category mainCategorySelected;
    private CategorySpinnerAdapter categorySpinnerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_operate);
        Intent intent = getIntent();
        categoryType = intent.getStringExtra("type");
        categoryList = intent.getStringArrayListExtra("categoryList");
        initData();
        initView();
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
                Log.d("分类","classfication = "+classfication);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mainClassficationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainCategorySelected = mainCategoryList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMoneyDb = MyMoneyDb.getInstance(MyApplication.getContext());
                String text = categoryNameEt.getText().toString();
                if (text == null || "".equals(text)) {
                    Toast.makeText(AddCategoryActivity.this, "分类名称不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (categoryList.size() > 0) {
                    if (categoryList.contains(text)) {
                        Toast.makeText(AddCategoryActivity.this, "对不起，分类名称已存在！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Category category = new Category();
                if ("一级类别".equals(classfication)) {
                    category.setName(categoryNameEt.getText().toString());
                    category.setType(categoryType);
                    category.setUpdateTime(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
                } else {
                    category.setName(categoryNameEt.getText().toString());
                    category.setType(categoryType);
                    category.setUpdateTime(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
                    category.setParentId(mainCategorySelected.getId());
                }
                myMoneyDb.insertCategory(category);
                Toast.makeText(AddCategoryActivity.this,"添加成功！",Toast.LENGTH_SHORT).show();
                setResult(3);
                finish();
            }
        });
    }

    private void initData() {
        myMoneyDb = MyMoneyDb.getInstance(this);
        //查询主分类
        mainCategoryList = myMoneyDb.getMainCategory(categoryType);
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
        categorySpinnerAdapter = new CategorySpinnerAdapter(this, R.layout.item_maincategory, mainCategoryList);
        mainClassficationSpinner.setAdapter(categorySpinnerAdapter);
        toolbar.setTitle("添加" + categoryType + "类别");
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

    @Override
    protected void onStop() {
        super.onStop();

    }
}
