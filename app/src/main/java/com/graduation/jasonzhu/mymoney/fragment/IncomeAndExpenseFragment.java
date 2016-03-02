package com.graduation.jasonzhu.mymoney.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.activity.EditExpenseActivity;
import com.graduation.jasonzhu.mymoney.activity.EditIncomeActivity;
import com.graduation.jasonzhu.mymoney.activity.MainActivity;
import com.graduation.jasonzhu.mymoney.adapter.DayAccountExpandLvAdapter;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.model.IncomeAndExpense;
import com.graduation.jasonzhu.mymoney.model.Summary;
import com.graduation.jasonzhu.mymoney.model.TestData;
import com.graduation.jasonzhu.mymoney.util.MyApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gemha on 2016/2/23.
 */
public class IncomeAndExpenseFragment extends Fragment {

    private TextView yearBalanceTv;
    private TextView yearIncomeTv;
    private TextView yearExpenseTv;
    private ExpandableListView expandableListView;
    private DayAccountExpandLvAdapter dayAccountExpandLvAdapter;
    private View rootView;
    private Summary yearSummary;
    private List<Summary> groupList = new ArrayList<>();
    private List<List<IncomeAndExpense>> childList = new ArrayList<>();
    private MyMoneyDb myMoneyDb;
    private int lastClick = -1;
    private static final String TAG = "TEST";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "IncomeAndExpenseFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "IncomeAndExpenseFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "IncomeAndExpenseFragment onCreateView");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        String time = dateFormat.format(date);
        getData(time.split("-"));
        initView();
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

//                if (TestData.getExpandListGroup().get(groupPosition).isEmpty()) {
//                    return true;
//                }
//                return false;
                if (lastClick == -1) {
                    expandableListView.expandGroup(groupPosition);
                }

                if (lastClick != -1 && lastClick != groupPosition) {
                    expandableListView.collapseGroup(lastClick);
                    expandableListView.expandGroup(groupPosition);
                } else if (lastClick == groupPosition) {
                    if (expandableListView.isGroupExpanded(groupPosition))
                        expandableListView.collapseGroup(groupPosition);
                    else if (!expandableListView.isGroupExpanded(groupPosition))
                        expandableListView.expandGroup(groupPosition);
                }

                lastClick = groupPosition;
                return true;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (TestData.getIncomeAndExpenseList().get(groupPosition).get(childPosition).getType()) {
                    case "收入":
                        Intent intent = new Intent(getActivity(), EditIncomeActivity.class);
                        intent.putExtra("income_data", TestData.getIncomeAndExpenseList().get(groupPosition).get(childPosition));
                        startActivity(intent);
                        break;
                    case "支出":
                        Intent intent2 = new Intent(getActivity(), EditExpenseActivity.class);
                        intent2.putExtra("expense_data", TestData.getIncomeAndExpenseList().get(groupPosition).get(childPosition));
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
        return rootView;
    }

    private void initView() {
        rootView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.income_expense_statistics, null);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.mm_main_expandlv);
        dayAccountExpandLvAdapter = new DayAccountExpandLvAdapter(MyApplication.getContext()
                , groupList, childList);
        expandableListView.setAdapter(dayAccountExpandLvAdapter);
        //去除箭头
        expandableListView.setGroupIndicator(null);
        yearBalanceTv = (TextView) rootView.findViewById(R.id.mm_main_balance_tv);
        yearIncomeTv = (TextView) rootView.findViewById(R.id.mm_main_income_tv);
        yearExpenseTv = (TextView) rootView.findViewById(R.id.mm_main_expense_tv);
        yearBalanceTv.setText(yearSummary.getBalance());
        yearIncomeTv.setText(yearSummary.getIncome());
        yearExpenseTv.setText(yearSummary.getExpense());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "IncomeAndExpenseFragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "IncomeAndExpenseFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "IncomeAndExpenseFragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "IncomeAndExpenseFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "IncomeAndExpenseFragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "IncomeAndExpenseFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "IncomeAndExpenseFragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "IncomeAndExpenseFragment onDetach");
    }


    public void getData(String[] time) {
        myMoneyDb = MyMoneyDb.getInstance(getContext());
        //查询年度总收入
        yearSummary = myMoneyDb.getYearIncomeAndExpense(time[0]);
        if (!"0.0".equals(yearSummary.getExpense()) && !"0.0".equals(yearSummary.getIncome())) {
            //查询月总收入
            groupList = myMoneyDb.getMonthIncomeAndExpense(time[0]);
            //查询月明细
            //  Log.d("DATA",Integer.valueOf(time[1]).toString());
            for (int i = Integer.valueOf(time[1]); i > 0; i--) {
                List<IncomeAndExpense> list = myMoneyDb.getDayIncomeAndExpense(time[0], time[1]);
                if (list.size() > 0) {
                    childList.add(list);
                }
            }
        }
    }
}
