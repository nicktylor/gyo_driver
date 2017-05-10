package com.crest.driver.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crest.driver.AdapterClasses.DashboardViewPagerAdapter;
import com.crest.driver.AdapterClasses.MyEarningsAdapter;
import com.crest.driver.AdapterClasses.MyRidesViewPagerAdapter;
import com.crest.driver.ModelClasses.MyEarningsModel;
import com.crest.driver.R;

import java.util.ArrayList;


public class MyRidesFragment extends Fragment implements TabLayout.OnTabSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    private TabLayout ride_tab_layout;
    private ViewPager ride_view_pager;
    private MyRidesViewPagerAdapter myRidesViewPagerAdapter;
    private String[] tabTitle = {
            "Today",
            "Weekly",
            "Monthly"
    };
    View view;

    public MyRidesFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        for (int i = 0; i < tabTitle.length; i++) {
            ride_tab_layout.addTab(ride_tab_layout.newTab().setText("" + tabTitle[i]));
        }
        myRidesViewPagerAdapter = new MyRidesViewPagerAdapter(getActivity().getSupportFragmentManager(), ride_tab_layout.getTabCount());//Adding adapter to pager
        ride_view_pager.setAdapter(myRidesViewPagerAdapter);
        ride_tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        ride_tab_layout.setOnTabSelectedListener(this);
        ride_view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(ride_tab_layout));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.crest.goyo.fragment
        view= inflater.inflate(R.layout.fragment_my_rides, container, false);
        return view;

    }

    private void initUI() {

        ride_tab_layout = (TabLayout) getView().findViewById(R.id.ride_tab_layout);
        ride_view_pager = (ViewPager) getView().findViewById(R.id.ride_view_pager);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        ride_view_pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
