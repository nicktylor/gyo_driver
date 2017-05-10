package com.crest.driver.AdapterClasses;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.crest.driver.fragment.MonthlyFragment;
import com.crest.driver.fragment.MonthlyRideFragment;
import com.crest.driver.fragment.TodayFragment;
import com.crest.driver.fragment.TodaysRideFragment;
import com.crest.driver.fragment.WeeklyFragment;
import com.crest.driver.fragment.WeeklyRideFragment;


/**
 * Created by jasson on 7/12/16.
 */

public class MyRidesViewPagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    public MyRidesViewPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TodaysRideFragment todaysRideFragment = new TodaysRideFragment();
                return todaysRideFragment;
            case 1:
                WeeklyRideFragment weeklyRideFragment = new WeeklyRideFragment();
                return weeklyRideFragment;
            case 2:
                MonthlyRideFragment monthlyRideFragment = new MonthlyRideFragment();
                return monthlyRideFragment;
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