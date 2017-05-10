package com.crest.driver.AdapterClasses;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.crest.driver.fragment.MonthlyFragment;
import com.crest.driver.fragment.TodayFragment;
import com.crest.driver.fragment.WeeklyFragment;




/**
 * Created by jasson on 7/12/16.
 */

public class DashboardViewPagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    public DashboardViewPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TodayFragment todayFragment = new TodayFragment();
                return todayFragment;
            case 1:
                WeeklyFragment weeklyFragment = new WeeklyFragment();
                return weeklyFragment;
            case 2:
                MonthlyFragment monthlyFragment = new MonthlyFragment();
                return monthlyFragment;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}