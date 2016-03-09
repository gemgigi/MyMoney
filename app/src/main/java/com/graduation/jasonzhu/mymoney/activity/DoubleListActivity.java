package com.graduation.jasonzhu.mymoney.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.adapter.CategoryMainAdapter;
import com.graduation.jasonzhu.mymoney.adapter.CategorySubAdapter;
import com.graduation.jasonzhu.mymoney.model.TestData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gemha on 2016/2/17.
 */
public class DoubleListActivity extends AppCompatActivity {

    private ListView mainList;
    private ListView subList;
    private CategoryMainAdapter categoryMainAdapter;
    private CategorySubAdapter categorySubAdapter;
    private List<String> subListText = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_category);
        initView();
      //  categoryMainAdapter = new CategoryMainAdapter(this, TestData.getMainCategory());
        mainList.setAdapter(categoryMainAdapter);
        mainList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initAdapter(TestData.getSubCategory().get(position));
                subListText = TestData.getSubCategory().get(position);
                categoryMainAdapter.setSelectItem(position);
                categoryMainAdapter.notifyDataSetChanged();
            }
        });

        initAdapter(TestData.getSubCategory().get(0));

        subList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DoubleListActivity.this,subListText.get(position),Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initAdapter(List<String> values) {
      //  categorySubAdapter = new CategorySubAdapter(this,values);
        subList.setAdapter(categorySubAdapter);
        categorySubAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mainList = (ListView) findViewById(R.id.expense_category_mainlist);
        subList = (ListView) findViewById(R.id.expense_category_sublist);
    }
}
