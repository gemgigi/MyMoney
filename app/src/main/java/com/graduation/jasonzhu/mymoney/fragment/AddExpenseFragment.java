package com.graduation.jasonzhu.mymoney.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.adapter.CategoryMainAdapter;
import com.graduation.jasonzhu.mymoney.adapter.CategorySubAdapter;
import com.graduation.jasonzhu.mymoney.adapter.AccountSpinnerAdapter;
import com.graduation.jasonzhu.mymoney.model.TestData;
import com.graduation.jasonzhu.mymoney.util.MyApplication;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gemha on 2016/2/16.
 */
public class AddExpenseFragment extends Fragment  {

    private Spinner spinner;
    private AccountSpinnerAdapter spinnerAdapter;
    private TextView category_tv;
    private TextView date_tv;
    private ListView mainList;
    private ListView subList;
    private CategoryMainAdapter categoryMainAdapter;
    private CategorySubAdapter categorySubAdapter;
    private List<String> subListText = new ArrayList<String>();
    private View categoryListView;
    private View categoryLayout;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addexpense, container, false);
        spinnerAdapter = new AccountSpinnerAdapter(MyApplication.getContext(),R.layout.item_select_account, TestData.getAccounts());
        spinner = (Spinner) view.findViewById(R.id.mm_expense_account_spinner);
        spinner.setAdapter(spinnerAdapter);
        categoryLayout = view.findViewById(R.id.mm_expense_category_layout);
        date_tv = (TextView) view.findViewById(R.id.mm_expense_date_tv);
        date_tv.setText(getCurrentTime());
       // categoryListView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.select_category, null);
        categoryMainAdapter = new CategoryMainAdapter(MyApplication.getContext(), TestData.getMainCategory());

        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        SimpleDateFormat sDateFormat = new SimpleDateFormat("H:m");
                        Date curDate = new Date(System.currentTimeMillis());
                        String time = monthOfYear+1+"月"+dayOfMonth+"日 "+sDateFormat.format(curDate);
                        date_tv.setText(time);
                    }
                },2016,1,18).show();
            }
        });
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), DoubleListActivity.class);
//                startActivity(intent);
              //  categoryMainAdapter = new CategoryMainAdapter(MyApplication.getContext(), TestData.getMainCategory());
               // AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity()){
                    @Override
                    public AlertDialog show() {
                        setView(initCategoryListView());
                        return super.show();
                    }
                };
                alertDialog.show();

            }

        });

        return view;
    }

    private String getCurrentTime(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("M月d日 H:m");
        Date curDate = new Date(System.currentTimeMillis());
        String time = sDateFormat.format(curDate);
        return time;
    }

    private View initCategoryListView() {
        categoryListView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.select_category, null);
        mainList = (ListView) categoryListView.findViewById(R.id.expense_category_mainlist);
        subList = (ListView) categoryListView.findViewById(R.id.expense_category_sublist);
        mainList.setAdapter(categoryMainAdapter);
        mainList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initAdapter(TestData.getSubCategory().get(position));
                subListText.clear();
                subListText = TestData.getSubCategory().get(position);
                categoryMainAdapter.setSelectItem(position);
                categoryMainAdapter.notifyDataSetChanged();
            }
        });

        //第一次没有点击主列表，没有对subListText赋值，所以subListText为空
        initAdapter(TestData.getSubCategory().get(0));
        subListText = TestData.getSubCategory().get(0);
        subList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), subListText.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        return categoryListView;
    }
    private void initAdapter(List<String> values) {
        categorySubAdapter = new CategorySubAdapter(MyApplication.getContext(),values);
        subList.setAdapter(categorySubAdapter);
        categorySubAdapter.notifyDataSetChanged();
    }
}
