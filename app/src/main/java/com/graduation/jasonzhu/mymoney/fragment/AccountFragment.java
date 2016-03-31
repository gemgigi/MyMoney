package com.graduation.jasonzhu.mymoney.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.activity.AddAccountActivity;
import com.graduation.jasonzhu.mymoney.activity.EditAccountActivity;
import com.graduation.jasonzhu.mymoney.activity.MainActivity;
import com.graduation.jasonzhu.mymoney.adapter.AccountListViewAdapter;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.model.Account;
import com.graduation.jasonzhu.mymoney.model.TestData;
import com.graduation.jasonzhu.mymoney.util.MyApplication;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by gemha on 2016/2/23.
 */
public class AccountFragment extends Fragment {
    private static final int REQUEST_CODE = 1;
    private ListView accountListView;
    private AccountListViewAdapter accountListViewAdapter;
    private TextView balenceTv;
    private View rootView;
    private static final String TAG = "TEST";
    private List<Account> accountList;
    private MyMoneyDb myMoneyDb;

    public List<Account> getAccountList() {
        myMoneyDb = MyMoneyDb.getInstance(getContext());
        accountList = myMoneyDb.getAllAccount();
        return accountList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "AccountFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "AccountFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "AccountFragment onCreateView");
        initView();
        accountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditAccountActivity.class);
                intent.putExtra("account",accountList.get(position));
                intent.putExtra("accountList", (Serializable) accountList);
                startActivityForResult(intent, 1);
                // startActivityForResult(intent,REQUEST_CODE);
            }
        });
        setHasOptionsMenu(true);
        return rootView;
    }

    private void initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.account_overview, null);
        accountListView = (ListView) rootView.findViewById(R.id.mm_main_account_list);
        balenceTv = (TextView) rootView.findViewById(R.id.mm_main_account_balence_tv);
        accountListViewAdapter = new AccountListViewAdapter(getActivity(), R.layout.item_select_account, getAccountList());
        accountListView.setAdapter(accountListViewAdapter);
        balenceTv.setText(getAllBalance());
    }

    private String getAllBalance(){
        float balance = 0;
        for(Account account:accountList){
            balance += account.getAccountMoney();
        }
        BigDecimal bd = new BigDecimal(String.valueOf(balance));
        return bd.toPlainString();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_account_add) {
            Intent intent = new Intent(getActivity(), AddAccountActivity.class);
            intent.putExtra("accountList", (Serializable) accountList);
            startActivityForResult(intent,1);
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "AccountFragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"AccountFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "AccountFragment onResume");
//        MainActivity mainActivity = (MainActivity) getActivity();
//        if(mainActivity.isOperateOnAccount()){
            getAccountList();
            balenceTv.setText(getAllBalance());
            accountListViewAdapter.notifyDataSetChanged();
//            mainActivity.setIsOperateOnAccount(false);
//            Log.d(TAG, "AccountFragment 刷新");
//        }



    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "AccountFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "AccountFragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "AccountFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "AccountFragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "AccountFragment onDetach");
    }
}
