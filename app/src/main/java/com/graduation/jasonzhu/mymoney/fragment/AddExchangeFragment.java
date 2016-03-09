package com.graduation.jasonzhu.mymoney.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.activity.AddRecordActivity;
import com.graduation.jasonzhu.mymoney.adapter.AccountSpinnerAdapter;
import com.graduation.jasonzhu.mymoney.model.TestData;
import com.graduation.jasonzhu.mymoney.util.LogUtil;
import com.graduation.jasonzhu.mymoney.util.MyApplication;
import com.graduation.jasonzhu.mymoney.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gemha on 2016/2/16.
 */
public class AddExchangeFragment extends Fragment {

    private Spinner intoSpinner;
    private Spinner outSpinner;
    private AccountSpinnerAdapter spinnerAdapter;
    private TextView date_tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("FRAGMENT_ACTION", "AddExchangeFragment onCreateView");
        View view = inflater.inflate(R.layout.addexchange, container, false);
        intoSpinner = (Spinner) view.findViewById(R.id.mm_exchange_into_account_spinner);
        outSpinner = (Spinner) view.findViewById(R.id.mm_exchange_out_account_spinner);
        spinnerAdapter = new AccountSpinnerAdapter(MyApplication.getContext(),R.layout.item_select_account, TestData.getAccounts());
        intoSpinner.setAdapter(spinnerAdapter);
        outSpinner.setAdapter(spinnerAdapter);
        date_tv = (TextView) view.findViewById(R.id.mm_exchange_date_tv);
        date_tv.setText(TimeUtil.getCurrentTime("M月d日 H:m"));
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
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.d("FRAGMENT_ACTION","AddExchangeFragment onAttach");

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
