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
import com.graduation.jasonzhu.mymoney.activity.EditExchangeActivity;
import com.graduation.jasonzhu.mymoney.activity.EditExpenseActivity;
import com.graduation.jasonzhu.mymoney.activity.EditIncomeActivity;
import com.graduation.jasonzhu.mymoney.activity.MainActivity;
import com.graduation.jasonzhu.mymoney.adapter.DayAccountExpandLvAdapter;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.model.DaySummary;
import com.graduation.jasonzhu.mymoney.model.IncomeAndExpense;
import com.graduation.jasonzhu.mymoney.model.Summary;
import com.graduation.jasonzhu.mymoney.model.TestData;
import com.graduation.jasonzhu.mymoney.model.Transfer;
import com.graduation.jasonzhu.mymoney.util.DaySummaryComparator;
import com.graduation.jasonzhu.mymoney.util.LogUtil;
import com.graduation.jasonzhu.mymoney.util.MyApplication;
import com.graduation.jasonzhu.mymoney.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
    private List<IncomeAndExpense> dayList = new ArrayList<>();
    private List<List<IncomeAndExpense>> childList = new ArrayList<>();
    private List<DaySummary> child = new ArrayList<>();
    private DaySummary daySummary;
    private List<Transfer> transferList;
    private MyMoneyDb myMoneyDb;
    private int lastClick = -1;
    private static final String TAG = "TEST";


    public Summary getYearSummary(String year) {
        myMoneyDb = MyMoneyDb.getInstance(getContext());
        yearSummary = myMoneyDb.getYearIncomeAndExpense(year);
        return yearSummary;
    }

    public List<Summary> getGroupList(String year) {
        myMoneyDb = MyMoneyDb.getInstance(getContext());
        groupList = myMoneyDb.getMonthIncomeAndExpense(year);
        return groupList;
    }

    public List<IncomeAndExpense> getDayList(String year, String month) {
        myMoneyDb = MyMoneyDb.getInstance(getContext());
        dayList = myMoneyDb.getDayIncomeAndExpense(year, month);
        return dayList;
    }

//    public DaySummary getDaySummary(String year, String month) {
//        myMoneyDb = MyMoneyDb.getInstance(getContext());
//        daySummary = new DaySummary();
//        daySummary.setIncomeAndExpenseList(myMoneyDb.getDayIncomeAndExpense(year, month));
//        daySummary.setTransferList(myMoneyDb.getTransfer(year, month));
//        return daySummary;
//    }


    public List<Transfer> getTransfer(String year, String month) {
        myMoneyDb = MyMoneyDb.getInstance(getContext());
        transferList = myMoneyDb.getTransfer(year, month);
        return transferList;
    }

    public List<List<IncomeAndExpense>> getChildList() {
        return childList;
    }

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
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
//        Date date = new Date();
//        String time = dateFormat.format(date);
        getData(TimeUtil.getCurrentTime("yyyy-MM").split("-"));
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
                IncomeAndExpense incomeAndExpense = childList.get(groupPosition).get(childPosition);
                if (incomeAndExpense.getRecordType() == 0) {
                    Intent intent = new Intent(getActivity(), EditIncomeActivity.class);
                    switch (childList.get(groupPosition).get(childPosition).getType()) {
                        case "收入":
                            intent.putExtra("data", childList.get(groupPosition).get(childPosition));
                            startActivityForResult(intent, 1);
                            break;
                        case "支出":
                            intent.putExtra("data", childList.get(groupPosition).get(childPosition));
                            startActivityForResult(intent, 1);
                            break;
                    }
                } else if (incomeAndExpense.getRecordType() == 1) {
                    Intent intent = new Intent(getActivity(), EditExchangeActivity.class);
                    intent.putExtra("data", incomeAndExpense.getTransfer());
                    startActivity(intent);
                }

                return false;
            }
        });
        return rootView;
    }

    private void initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.income_expense_statistics, null);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.mm_main_expandlv);
        dayAccountExpandLvAdapter = new DayAccountExpandLvAdapter(getContext()
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (resultCode) {
//            case 1:
//                Log.d("TAG", "from income_expense");
//                break;
//            case 2:
//                Log.d("TAG", "account");
//                break;
//            case 3:
//                Log.d("TAG", "from category");
//                break;
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "IncomeAndExpenseFragment onResume");
//        MainActivity mainActivity = (MainActivity) getActivity();
//        if (mainActivity.isOperateOnIncomeExpense()) {
        getData(TimeUtil.getCurrentTime("yyyy-MM").split("-"));
        LogUtil.d(TAG, groupList.toString());
        yearExpenseTv.setText(yearSummary.getExpense());
        yearIncomeTv.setText(yearSummary.getIncome());
        yearBalanceTv.setText(yearSummary.getBalance());
        // dayAccountExpandLvAdapter.notifyDataSetChanged();
        dayAccountExpandLvAdapter = new DayAccountExpandLvAdapter(getContext()
                , groupList, childList);
        expandableListView.setAdapter(dayAccountExpandLvAdapter);
//            mainActivity.setIsOperateOnIncomeExpense(false);
//            Log.d(TAG, "IncomeAndExpenseFragment 刷新");
        //}
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
        childList.clear();
        groupList.clear();
        myMoneyDb = MyMoneyDb.getInstance(getContext());
        //查询年度总收入
        getYearSummary(time[0]);
        if (!"0.0".equals(yearSummary.getExpense()) || !"0.0".equals(yearSummary.getIncome()) ||
                !"0.0".equals(yearSummary.getBalance())) {
            //查询月总收入
            getGroupList(time[0]);
            //查询月明细
            for (int i = 0; i < groupList.size(); i++) {
                String month = "";
                if (1 < Integer.valueOf(groupList.get(i).getMonth()) && Integer.valueOf(groupList.get(i).getMonth()) < 10) {
                    month = "0" + Integer.valueOf(groupList.get(i).getMonth());
                } else {
                    month = groupList.get(i).getMonth();
                }
                getDayList(time[0], month);
                getTransfer(time[0], month);
                for (Transfer transfer : transferList) {
                    IncomeAndExpense incomeAndExpense = new IncomeAndExpense();
                    incomeAndExpense.setTransfer(transfer);
                    incomeAndExpense.setSaveTime(transfer.getTransferTime());
                    incomeAndExpense.setUpdateTime(transfer.getUpdateTime());
                    incomeAndExpense.setRecordType(1);
                    dayList.add(incomeAndExpense);
                }
                Collections.sort(dayList, new DaySummaryComparator());
                if (dayList.size() > 0) {
                    childList.add(dayList);
                }
            }
        } else {
            for (int i = 1; i <= 12; i++) {
                String month = "";
                if (1 <= i && i < 10) {
                    month = "0" + i;
                } else {
                    month = String.valueOf(i);
                }
                getTransfer(time[0], month);
                if (transferList.size() > 0) {
                    Summary group = new Summary();
                    group.setMonth(String.valueOf(i));
                    group.setExpense(String.valueOf(0.00f));
                    group.setIncome(String.valueOf(0.00f));
                    group.setBalance(String.valueOf(0.00f));
                    groupList.add(group);
                    dayList = new ArrayList<>();
                    for (Transfer transfer : transferList) {
                        IncomeAndExpense incomeAndExpense = new IncomeAndExpense();
                        incomeAndExpense.setTransfer(transfer);
                        incomeAndExpense.setSaveTime(transfer.getTransferTime());
                        incomeAndExpense.setUpdateTime(transfer.getUpdateTime());
                        incomeAndExpense.setRecordType(1);
                        dayList.add(incomeAndExpense);
                    }
                    Collections.sort(dayList, new DaySummaryComparator());
                    if (dayList.size() > 0) {
                        childList.add(dayList);
                    }
                }
            }
        }
        LogUtil.d(TAG, childList.toString());
    }
}
