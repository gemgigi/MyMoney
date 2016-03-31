package com.graduation.jasonzhu.mymoney.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.activity.AddRecordActivity;
import com.graduation.jasonzhu.mymoney.adapter.CategoryMainAdapter;
import com.graduation.jasonzhu.mymoney.adapter.CategorySubAdapter;
import com.graduation.jasonzhu.mymoney.adapter.AccountSpinnerAdapter;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.model.Account;
import com.graduation.jasonzhu.mymoney.model.Category;
import com.graduation.jasonzhu.mymoney.model.IncomeAndExpense;
import com.graduation.jasonzhu.mymoney.util.CategoryListView;
import com.graduation.jasonzhu.mymoney.util.CategorySelectedListener;
import com.graduation.jasonzhu.mymoney.util.LogUtil;
import com.graduation.jasonzhu.mymoney.util.TimeUtil;
import com.graduation.jasonzhu.mymoney.util.CalculatorView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by gemha on 2016/2/16.
 */
public class AddExpenseFragment extends Fragment {

    private Spinner spinner;
    private AccountSpinnerAdapter spinnerAdapter;
    private TextView moneyTv;
    private TextView mainCategoryTv;
    private TextView subCategoryTv;
    private TextView date_tv;
    private EditText remarkEt;
    private Button saveBtn;
    private ListView mainList;
    private ListView subList;
    private CategoryMainAdapter categoryMainAdapter;
    private CategorySubAdapter categorySubAdapter;
    private View categoryListView;
    private View categoryLayout;
    private View rootView;
    private MyMoneyDb myMoneyDb;
    private List<Category> mainCategoryList;
    private List<Category> subCategoryList = new ArrayList<>();
    private List<Account> accountList;
    private String mainCategoryText;
    private String subCategoryText;
    private String selectedTime;
    private Account selectedAccount;
    private Category selectedCategory;
    private AlertDialog alertDialog;
    private CategoryListView categoryLv;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("FRAGMENT_ACTION", "AddExpenseFragment onCreateView");
        initData();
        initView();
        moneyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CalculatorView calculatorView = new CalculatorView();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext()) {
                    @Override
                    public AlertDialog show() {
                        setView(calculatorView.getCalculatoView(getContext()));
                        return super.show();
                    }
                };
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moneyTv.setText(calculatorView.getResultTv().getText());
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAccount = accountList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        LogUtil.d("DATETEST","date = "+monthOfYear);
                        if (0 <= monthOfYear && monthOfYear < 9) {
                            if (0 < dayOfMonth && dayOfMonth < 10) {
                                selectedTime = year + "-" + 0 + (monthOfYear + 1) + "-" + "0" + dayOfMonth + " " + TimeUtil.getCurrentTime("HH:mm");
                            } else {
                                selectedTime = year + "-" + 0 + (monthOfYear + 1) + "-" + dayOfMonth + " " + TimeUtil.getCurrentTime("HH:mm");
                            }
                        } else {
                            if (0 < dayOfMonth && dayOfMonth < 10) {
                                selectedTime = year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth + " " + TimeUtil.getCurrentTime("HH:mm");
                            } else {
                                selectedTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " " + TimeUtil.getCurrentTime("HH:mm");
                            }

                        }
                        date_tv.setText(selectedTime);
                    }
                }, Integer.valueOf(TimeUtil.getCurrentTime("yyyy"))
                        , Integer.valueOf(TimeUtil.getCurrentTime("M")) - 1, Integer.valueOf(TimeUtil.getCurrentTime("dd"))).show();
            }
        });
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()) {
                    @Override
                    public AlertDialog show() {
                        setView(categoryLv.getCategoryView(new CategorySelectedListener() {
                            @Override
                            public void onFinish() {
                                alertDialog.dismiss();
                            }
                        }));
                        return super.show();
                    }
                };
                alertDialog = builder.show();

            }

        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = moneyTv.getText().toString();
                String remark = remarkEt.getText().toString();
                if (categoryLv.getSelectedCategory() != null) {
                    selectedCategory = categoryLv.getSelectedCategory();
                }
                IncomeAndExpense incomeAndExpense = new IncomeAndExpense();
                incomeAndExpense.setType("支出");
                incomeAndExpense.setMoney(Float.valueOf(money));
                incomeAndExpense.setCategory(selectedCategory);
                incomeAndExpense.setAccount(selectedAccount);
                incomeAndExpense.setRemark(remark);
                incomeAndExpense.setSaveTime(date_tv.getText().toString());
                incomeAndExpense.setUpdateTime(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
                myMoneyDb = MyMoneyDb.getInstance(getContext());
                myMoneyDb.saveIncomeAndExpense(incomeAndExpense);
                getActivity().setResult(1,getActivity().getIntent());
                getActivity().finish();
            }
        });
        return rootView;
    }

    private void initData() {
        myMoneyDb = MyMoneyDb.getInstance(getContext());
        mainCategoryList = myMoneyDb.getMainCategory("支出");
        accountList = myMoneyDb.getAllAccount();
        if (accountList.size() > 0) {
            selectedAccount = accountList.get(0);
        }
        if (mainCategoryList.size() > 0) {
            selectedCategory = myMoneyDb.getSubCategory(mainCategoryList.get(0).getId()).get(0);
        }
    }

    private void initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.addexpense, null);
        remarkEt = (EditText) rootView.findViewById(R.id.mm_expense_remark);
        moneyTv = (TextView) rootView.findViewById(R.id.mm_expense_money_tv);
        mainCategoryTv = (TextView) rootView.findViewById(R.id.mm_expense_maincategory_tv);
        subCategoryTv = (TextView) rootView.findViewById(R.id.mm_expense_subcategory_tv);
        spinnerAdapter = new AccountSpinnerAdapter(getContext(), R.layout.item_select_account, accountList);
        spinner = (Spinner) rootView.findViewById(R.id.mm_expense_account_spinner);
        spinner.setAdapter(spinnerAdapter);
        categoryLayout = rootView.findViewById(R.id.mm_expense_category_layout);
        saveBtn = (Button) rootView.findViewById(R.id.mm_expense_save);
        date_tv = (TextView) rootView.findViewById(R.id.mm_expense_date_tv);
        date_tv.setText(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
        mainCategoryTv.setText(mainCategoryList.get(0).getName());
        subCategoryTv.setText(selectedCategory.getName());
        categoryLv = new CategoryListView(getContext(), "支出", mainCategoryTv, subCategoryTv);
    }

//    private String getCurrentTime(){
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("M月d日 H:m");
//        Date curDate = new Date(System.currentTimeMillis());
//        String time = sDateFormat.format(curDate);
//        return time;
//    }

    private View initCategoryListView() {
        categoryListView = LayoutInflater.from(getContext()).inflate(R.layout.select_category, null);
        mainList = (ListView) categoryListView.findViewById(R.id.expense_category_mainlist);
        subList = (ListView) categoryListView.findViewById(R.id.expense_category_sublist);
        categoryMainAdapter = new CategoryMainAdapter(categoryListView.getContext(), mainCategoryList);
        mainList.setAdapter(categoryMainAdapter);
        mainList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        subList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                subCategoryList.clear();
                mainCategoryText = "";
                myMoneyDb = MyMoneyDb.getInstance(getContext());
                subCategoryList = myMoneyDb.getSubCategory(mainCategoryList.get(position).getId());
                categoryMainAdapter.setSelectItem(position);
                categoryMainAdapter.notifyDataSetChanged();
                initAdapter(subCategoryList);
                mainCategoryText = mainCategoryList.get(position).getName();
            }
        });

        //第一次没有点击主列表，没有对subListText赋值，所以subListText为空

        myMoneyDb = MyMoneyDb.getInstance(getContext());
        subCategoryList = myMoneyDb.getSubCategory(mainCategoryList.get(0).getId());
        initAdapter(subCategoryList);
        subList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subCategoryText = subCategoryList.get(position).getName();
                selectedCategory = subCategoryList.get(position);
                subCategoryTv.setText(subCategoryText);
                if (mainCategoryText != null) {
                    mainCategoryTv.setText(mainCategoryText);
                } else {
                    mainCategoryTv.setText(mainCategoryList.get(0).getName());
                }
                alertDialog.dismiss();
            }
        });
        return categoryListView;
    }

    private void initAdapter(List<Category> values) {
        categorySubAdapter = new CategorySubAdapter(categoryListView.getContext(), values);
        subList.setAdapter(categorySubAdapter);
        categorySubAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.d("FRAGMENT_ACTION", "AddExpenseFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("FRAGMENT_ACTION", "AddExpenseFragment onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d("FRAGMENT_ACTION", "AddExpenseFragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("FRAGMENT_ACTION", "AddExpenseFragment onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("FRAGMENT_ACTION", "AddExpenseFragment onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("FRAGMENT_ACTION", "AddExpenseFragment onResume");
        AddRecordActivity addRecordActivity = (AddRecordActivity) getActivity();
        LogUtil.d("CURRENT_TAB", "AddExpense" + addRecordActivity.getCurrentTab());
    }

    @Override
    public void onStop() {
        LogUtil.d("FRAGMENT_ACTION", "AddExpenseFragment onStop");
        super.onStop();
    }
}
