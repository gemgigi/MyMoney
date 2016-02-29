package com.graduation.jasonzhu.mymoney.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by gemha on 2016/2/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> tabTitles;
    private Context context;

    public ViewPagerAdapter(FragmentManager fm,Context context,List<Fragment> fragments,List<String> tabTitles) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.tabTitles = tabTitles;
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

    //    /***
//     *  添加选项卡
//     */
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        container.addView(views.get(position));
//        return views.get(position);
//    }
//
//    /***
//     * 销毁选项卡
//     * @param container
//     * @param position
//     * @param object
//     */
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView(views.get(position));
//    }
}
