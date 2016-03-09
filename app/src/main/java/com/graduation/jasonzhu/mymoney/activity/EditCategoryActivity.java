package com.graduation.jasonzhu.mymoney.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.graduation.jasonzhu.mymoney.model.Category;
import com.graduation.jasonzhu.mymoney.util.MyApplication;
import com.graduation.jasonzhu.mymoney.util.TimeUtil;

import java.util.List;

public class EditCategoryActivity extends AppCompatActivity {

    private Spinner classficationSpinner;
    private Spinner mainClassficationSpinner;
    private EditText categoryNameEt;
    private Button saveBtn;
    private ArrayAdapter spinnerAdapter;
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private Toolbar toolbar;
    private String categoryType;
    private String classfication;
    private String lastClassfication;
    private LinearLayout typeLayout;
    private List<Category> mainCategoryList;
    private MyMoneyDb myMoneyDb;
    private List<String> categoryNameList;
    private Category mainCategorySelected;
    private Category targetCategory;
    private Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_operate);
        Intent intent = getIntent();
        targetCategory = (Category) intent.getSerializableExtra("data");
        categoryType = targetCategory.getType();
        classfication = intent.getStringExtra("type");
        lastClassfication = classfication;
        initData();
        initView();

        if ("二级类别".equals(classfication)) {
            typeLayout.setVisibility(View.VISIBLE);
        } else if ("一级类别".equals(classfication)) {
            typeLayout.setVisibility(View.GONE);
        }

        categoryNameEt.setText(targetCategory.getName());
        setSpinnerItemSelectedByValue(classfication);
        if (intent.getStringExtra("mainCategoryName") != null) {
            setClassficationSpItemSelected(intent.getStringExtra("mainCategoryName"));
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
                    Toast.makeText(EditCategoryActivity.this, "分类名称不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("二级类别".equals(classfication)) {
                    if (targetCategory.getName().equals(mainCategorySelected.getName())) {
                        if (categoryNameList.size() > 0) {
                            if (categoryNameList.contains(text)) {
                                Toast.makeText(EditCategoryActivity.this, "对不起，分类名称已存在！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                }else if("一级类别".equals(classfication)){
                    if (categoryNameList.size() > 0) {
                        if (categoryNameList.contains(text)) {
                            Toast.makeText(EditCategoryActivity.this, "对不起，分类名称已存在！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                if (lastClassfication.equals(classfication)) {
                    if ("一级类别".equals(classfication)) {
                        targetCategory.setName(categoryNameEt.getText().toString());
                        targetCategory.setUpdateTime(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
                    } else {
                        targetCategory.setName(categoryNameEt.getText().toString());
                        targetCategory.setUpdateTime(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
                        targetCategory.setParentId(mainCategorySelected.getId());
                    }
                    myMoneyDb.updateCategory(targetCategory);
                }
//                //执行更新操作
//                else {
//                    Category category = new Category();
//                    //由二级类别改为一级类别，该分类下流水
//                    if ("一级类别".equals(classfication)) {
//                        category.setName(categoryNameEt.getText().toString());
//                        category.setType(categoryType);
//                        category.setUpdateTime(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
//                        //由一级类别改为二级类别
//                    } else {
//                        category.setName(categoryNameEt.getText().toString());
//                        category.setUpdateTime(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
//                        category.setParentId(mainCategorySelected.getId());
//                    }
//                    //执行删除和插入操作
//                }
                Toast.makeText(EditCategoryActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }


    private void initData() {
        myMoneyDb = MyMoneyDb.getInstance(this);
        mainCategoryList = myMoneyDb.getMainCategory(categoryType);
        categoryNameList = myMoneyDb.getAllCategoryName();
    }

    private void setClassficationSpItemSelected(String mainCategoryName) {
        for (int i = 0; i < mainCategoryList.size(); i++) {
            if (mainCategoryList.get(i).getName().equals(mainCategoryName)) {
                mainClassficationSpinner.setSelection(i, true);
            }
        }
    }

    //设置获取到的分类是一级分类还是二级分类
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
        classficationSpinner.setEnabled(false);

        categorySpinnerAdapter = new CategorySpinnerAdapter(this, R.layout.item_maincategory, mainCategoryList);
        mainClassficationSpinner.setAdapter(categorySpinnerAdapter);

        //  deleteBtn = (Button) findViewById(R.id.action_delete);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setTitle("编辑" + categoryType + "类别");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_delete) {
            String msg = "";
            if("一级类别".equals(lastClassfication)){
                msg = "删除该分类，会删除其下子分类和流水账";
            }else if("二级类别".equals(lastClassfication)){
                msg = "删除该分类，会删除其下流水账";
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(EditCategoryActivity.this)
                    .setTitle("注意！")
                    .setMessage(msg)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myMoneyDb = MyMoneyDb.getInstance(EditCategoryActivity.this);
                            myMoneyDb.deleteCategory(targetCategory);
                            Toast.makeText(EditCategoryActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
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
