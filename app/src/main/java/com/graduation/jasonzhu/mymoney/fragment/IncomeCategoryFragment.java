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
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.graduation.jasonzhu.mymoney.R;
import com.graduation.jasonzhu.mymoney.activity.EditCategoryActivity;
import com.graduation.jasonzhu.mymoney.activity.MainActivity;
import com.graduation.jasonzhu.mymoney.adapter.CategoryExpandLvAdapter;
import com.graduation.jasonzhu.mymoney.db.MyMoneyDb;
import com.graduation.jasonzhu.mymoney.model.Category;
import com.graduation.jasonzhu.mymoney.util.ExpandableListViewUtil;
import com.graduation.jasonzhu.mymoney.util.LoadDataCallBackListener;
import com.graduation.jasonzhu.mymoney.util.LogUtil;
import com.graduation.jasonzhu.mymoney.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gemha on 2016/2/28.
 */
public class IncomeCategoryFragment extends Fragment {
    private View rootView;
    private ExpandableListView expandableListView;
    private CategoryExpandLvAdapter categoryExpandLvAdapter;
    private int lastClick = -1;
    private List<Category> categoryList = new ArrayList<>();
    private MyMoneyDb myMoneyDb;
    private String TAG = "TEST";
    private LoadDataCallBackListener loadDataCallBackListener;
    private boolean isCreate = false;


    public List<Category> getCategoryList(final LoadDataCallBackListener loadDataCallBackListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                myMoneyDb = MyMoneyDb.getInstance(getContext());
                try {
                    categoryList = myMoneyDb.getAllCategory("收入");
                    loadDataCallBackListener.onFinish(isCreate);
                } catch (Exception e) {
                    loadDataCallBackListener.onError(e);
                }
            }
        }).start();
        return categoryList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "IncomeCategoryFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "IncomeCategoryFragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "IncomeCategoryFragment onCreateView");
        rootView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.category_list, null);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.mm_main_category_expanlist);
        categoryExpandLvAdapter = new CategoryExpandLvAdapter(getContext(), categoryList);
        expandableListView.setAdapter(categoryExpandLvAdapter);
        expandableListView.setGroupIndicator(null);
        setOnClickListener();
        initData();
        //  initView();
        return rootView;
    }

    private void initData() {
        getCategoryList(new LoadDataCallBackListener() {
            @Override
            public void onFinish(boolean isCreate) {
                LogUtil.d("TAG", "isCreate = " + isCreate);
                if (!isCreate) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initView();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            categoryExpandLvAdapter.notifyDataSetChanged();
//                    ExpandableListViewUtil.setListViewHeightBasedOnChildren(expandableListView);
                            Log.d(TAG, "IncomeCategoryFragment 刷新");
                        }
                    });

                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void initView() {
        categoryExpandLvAdapter = new CategoryExpandLvAdapter(getContext(), categoryList);
        expandableListView.setAdapter(categoryExpandLvAdapter);
        isCreate = true;

    }

    private void setOnClickListener() {
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                long packedPosition = expandableListView.getExpandableListPosition(position);
                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                //   int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    Intent intent = new Intent(getActivity(), EditCategoryActivity.class);
                    intent.putExtra("data", categoryList.get(position));
                    intent.putExtra("type", "一级类别");
                    startActivityForResult(intent, 1);
                    return true;
                }
                return false;
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

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
                Intent intent = new Intent(getActivity(), EditCategoryActivity.class);
                intent.putExtra("data", categoryList.get(groupPosition).getCategoryList().get(childPosition));
                intent.putExtra("mainCategoryName", categoryList.get(groupPosition).getName());
                intent.putExtra("type", "二级类别");
                startActivityForResult(intent, 1);
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "IncomeCategoryFragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "IncomeCategoryFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "IncomeCategoryFragment onResume");
        MainActivity mainActivity = (MainActivity) getActivity();
        LogUtil.d(TAG,"IncomeCategory "+mainActivity.isOperateOnIncomeExpense());
//        if (!isCreate) {
            initData();
//            mainActivity.setIsOperateOnIncomeExpense(false);
//            LogUtil.d("TAG", "IncomeCategoryFragment 刷新");
//        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "IncomeCategoryFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "IncomeCategoryFragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "IncomeCategoryFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "IncomeCategoryFragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "IncomeCategoryFragment onDetach");
    }


}
