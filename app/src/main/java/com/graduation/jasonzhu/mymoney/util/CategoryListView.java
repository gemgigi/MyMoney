package com.graduation.jasonzhu.mymoney.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.adapter.CategoryMainAdapter;
import com.graduation.jasonzhu.mymoney.adapter.CategorySubAdapter;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.model.Category;

import java.util.List;

/**
 * Created by gemha on 2016/3/9.
 */
public class CategoryListView {
    private View categoryListView;
    private View categoryLayout;
    private ListView mainList;
    private ListView subList;
    private CategoryMainAdapter categoryMainAdapter;
    private CategorySubAdapter categorySubAdapter;
    private String mainCategoryText;
    private String subCategoryText;
    private Context context;
    private List<Category> mainCategoryList;
    private List<Category> subCategoryList;
    private MyMoneyDb myMoneyDb;
    private Category selectedCategory;
    private TextView mainCategoryTv;
    private TextView subCategoryTv;
    private String type;
    private Category lastMainCategory;

    public CategoryListView(Context context, String type, TextView mainCategoryTv, TextView subCategoryTv) {
        this.context = context;
        this.type = type;
        this.mainCategoryTv = mainCategoryTv;
        this.subCategoryTv = subCategoryTv;
        getMainCategoryList();
    }

    public List<Category> getMainCategoryList() {
        myMoneyDb = MyMoneyDb.getInstance(context);
        mainCategoryList = myMoneyDb.getMainCategory(type);
        return mainCategoryList;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public View getCategoryView(final CategorySelectedListener listener) {

        categoryListView = LayoutInflater.from(context).inflate(R.layout.select_category, null);
        mainList = (ListView) categoryListView.findViewById(R.id.expense_category_mainlist);
        subList = (ListView) categoryListView.findViewById(R.id.expense_category_sublist);
        categoryMainAdapter = new CategoryMainAdapter(context, mainCategoryList);
        mainList.setAdapter(categoryMainAdapter);
        mainList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        subList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainCategoryText = "";
                lastMainCategory = mainCategoryList.get(position);
                myMoneyDb = MyMoneyDb.getInstance(context);
                subCategoryList = myMoneyDb.getSubCategory(mainCategoryList.get(position).getId());
                categoryMainAdapter.setSelectItem(position);
                categoryMainAdapter.notifyDataSetChanged();
                initChildAdapter(subCategoryList);
                mainCategoryText = mainCategoryList.get(position).getName();
            }
        });

        //第一次没有点击主列表，没有对subCategoryList赋值，subCategoryList会为空
        myMoneyDb = MyMoneyDb.getInstance(context);
        if (lastMainCategory != null) {
            for (int i = 0; i < mainCategoryList.size(); i++) {
                if(mainCategoryList.get(i) == lastMainCategory){
                    mainList.setSelection(i);
                }
            }
            subCategoryList = myMoneyDb.getSubCategory(lastMainCategory.getId());
        } else {
            subCategoryList = myMoneyDb.getSubCategory(mainCategoryList.get(0).getId());
        }
        initChildAdapter(subCategoryList);
        subList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subCategoryText = subCategoryList.get(position).getName();
                selectedCategory = subCategoryList.get(position);
                subCategoryTv.setText(subCategoryText);
                //若只点击了子分类，没有点击父分类，即默认第一个
                if (mainCategoryText != null) {
                    mainCategoryTv.setText(mainCategoryText);
                } else {
                    mainCategoryTv.setText(mainCategoryList.get(0).getName());
                }
                listener.onFinish();
            }
        });
        return categoryListView;
    }

    private void initChildAdapter(List<Category> values) {
        categorySubAdapter = new CategorySubAdapter(context, values);
        subList.setAdapter(categorySubAdapter);
        categorySubAdapter.notifyDataSetChanged();
    }


}
