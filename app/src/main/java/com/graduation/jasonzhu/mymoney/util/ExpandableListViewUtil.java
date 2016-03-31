package com.graduation.jasonzhu.mymoney.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * Created by gemha on 2016/3/15.
 */
public class ExpandableListViewUtil {

    public static void setListViewHeightBasedOnChildren(ExpandableListView exlistView) {
        // 获取ListView对应的Adapter
        ExpandableListAdapter listAdapter = exlistView.getExpandableListAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getGroupCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listGroup = listAdapter.getGroupView(i, false, null, exlistView);
            listGroup.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listGroup.getMeasuredHeight(); // 统计所有子项的总高度
            for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                View listItem = listAdapter.getChildView(i, j, false, null, (ViewGroup) listGroup);
                listItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
            }

        }

        ViewGroup.LayoutParams params = exlistView.getLayoutParams();
        params.height = totalHeight
                + (exlistView.getDividerHeight() * (listAdapter.getGroupCount()));
        LogUtil.d("expand_height", String.valueOf(params.height));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        exlistView.setLayoutParams(params);
    }

}
