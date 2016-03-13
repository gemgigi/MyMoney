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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.activity.AddRecordActivity;
import com.graduation.jasonzhu.mymoney.adapter.AccountSpinnerAdapter;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.model.Account;
import com.graduation.jasonzhu.mymoney.model.TestData;
import com.graduation.jasonzhu.mymoney.model.Transfer;
import com.graduation.jasonzhu.mymoney.util.CalculatorView;
import com.graduation.jasonzhu.mymoney.util.LogUtil;
import com.graduation.jasonzhu.mymoney.util.MyApplication;
import com.graduation.jasonzhu.mymoney.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gemha on 2016/2/16.
 */
public class AddExchangeFragment extends Fragment {

    private TextView moneyTv;
    private Spinner toSpinner;
    private Spinner fromSpinner;
    private AccountSpinnerAdapter spinnerAdapter;
    private TextView date_tv;
    private Button saveBtn;
    private View rootView;
    private String selectedTime;
    private List<Account> accountList;
    private Account fromAccount;
    private Account toAccount;
    private MyMoneyDb myMoneyDb;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("FRAGMENT_ACTION", "AddExchangeFragment onCreateView");
        initData();
        initView();
        moneyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CalculatorView calculatorView = new CalculatorView();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext()){
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
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toAccount = accountList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromAccount = accountList.get(position);
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
                        if (0 <= monthOfYear && monthOfYear < 9) {
                            if (0 < dayOfMonth && dayOfMonth < 10) {
                                selectedTime = year + "-" + 0 + (monthOfYear + 1) + "-" + "0" + dayOfMonth + " " + TimeUtil.getCurrentTime("HH:mm");
                            } else {
                                selectedTime = year + "-" + 0 + (monthOfYear + 1) + "-" + dayOfMonth + " " + TimeUtil.getCurrentTime("HH:m");
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
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = moneyTv.getText().toString();
                if("0.00".equals(money)){
                    Toast.makeText(getActivity(),"还没设置金额呢！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(toAccount == fromAccount){
                    Toast.makeText(getActivity(),"试试不同的账户吧！",Toast.LENGTH_SHORT).show();
                    return;
                }
                Transfer transfer = new Transfer();
                transfer.setFromAccount(fromAccount);
                transfer.setToAccount(toAccount);
                transfer.setMoney(Float.valueOf(money));
                transfer.setTransferTime(date_tv.getText().toString());
                transfer.setUpdateTime(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
                myMoneyDb.insertTransfer(transfer);
                getActivity().finish();
            }
        });
        return rootView;
    }

    private void initData() {
        myMoneyDb = MyMoneyDb.getInstance(getContext());
        accountList = myMoneyDb.getAllAccount();
        if (accountList.size() > 0) {
            toAccount = accountList.get(0);
            fromAccount = accountList.get(0);
        }
    }

    public void initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.addexchange, null);
        moneyTv = (TextView) rootView.findViewById(R.id.mm_exchange_money_et);
        toSpinner = (Spinner) rootView.findViewById(R.id.mm_exchange_into_account_spinner);
        fromSpinner = (Spinner) rootView.findViewById(R.id.mm_exchange_out_account_spinner);
        spinnerAdapter = new AccountSpinnerAdapter(MyApplication.getContext(), R.layout.item_select_account, accountList);
        toSpinner.setAdapter(spinnerAdapter);
        fromSpinner.setAdapter(spinnerAdapter);
        date_tv = (TextView) rootView.findViewById(R.id.mm_exchange_date_tv);
        date_tv.setText(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
        saveBtn = (Button) rootView.findViewById(R.id.mm_exchange_save);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.d("FRAGMENT_ACTION", "AddExchangeFragment onAttach");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("FRAGMENT_ACTION", "AddExchangeFragment onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d("FRAGMENT_ACTION", "AddExchangeFragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("FRAGMENT_ACTION", "AddExchangeFragment onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("FRAGMENT_ACTION", "AddExchangeFragment onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("FRAGMENT_ACTION", "AddExchangeFragment onResume");
        AddRecordActivity addRecordActivity = (AddRecordActivity) getActivity();
        LogUtil.d("CURRENT_TAB", "AddExchange" + addRecordActivity.getCurrentTab());
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("FRAGMENT_ACTION", "AddExchangeFragment onStop");
    }
}
