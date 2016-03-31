package com.graduation.jasonzhu.mymoney.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jason on 3/29/16.
 */
public class ReportViewPager extends ViewPager {


    private int childId;

    public ReportViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (childId > 0) {
            View scroll = findViewById(childId);
            if (scroll != null) {
                Rect rect = new Rect();
                scroll.getHitRect(rect);
                if (rect.contains((int) event.getX(), (int) event.getY())) {
                    return false;
                }
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }
}
