package com.crest.driver.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crest.driver.AdapterClasses.MyRidesMonthlyAdapter;
import com.crest.driver.AdapterClasses.MyRidesTodayAdapter;
import com.crest.driver.AdapterClasses.MyRidesWeeklyAdapter;
import com.crest.driver.ModelClasses.MyRidesModel;
import com.crest.driver.ModelClasses.MyRidesMonthlyModel;
import com.crest.driver.ModelClasses.MyRidesWeeklyModel;
import com.crest.driver.R;
import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyTAG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.HttpUrl;


public class MonthlyRideFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private View view;
    private RecyclerView rv_monthly_ride;
    MyRidesMonthlyAdapter myRidesMonthlyAdapter;

    public static ArrayList<MyRidesMonthlyModel> ridesList = new ArrayList<>();
    private TextView tv_monthly_no_record;

    public MonthlyRideFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void initUI() {
        rv_monthly_ride = (RecyclerView) view.findViewById(R.id.rv_monthly_ride);
        tv_monthly_no_record = (TextView) view.findViewById(R.id.tv_monthly_no_record);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.crest.goyo.fragment
        view = inflater.inflate(R.layout.fragment_monthy_ride, container, false);
        initUI();

        myRidesMonthlyAdapter = new MyRidesMonthlyAdapter(ridesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_monthly_ride.setLayoutManager(mLayoutManager);
        rv_monthly_ride.setItemAnimator(new DefaultItemAnimator());
        rv_monthly_ride.setAdapter(myRidesMonthlyAdapter);

        getDriverRides_monthly_api();


        return view;
    }

    private void getDriverRides_monthly_api() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_GET_DRIVER_RIDES).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token",Preferences.getValue(getContext(),Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("sort_by", "monthly");

        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();

        VolleyRequestClass.allRequest(getContext(), newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    int responce_status = response.getInt(VolleyTAG.status);
                    String message = response.getString(VolleyTAG.message);
                    if (responce_status == VolleyTAG.response_status) {

                        JSONArray data_array = response.getJSONArray("data");
                        JSONObject data_obj;
                        ridesList.clear();
                        for (int i = 0; i < data_array.length(); i++) {
                            data_obj = data_array.getJSONObject(i);
                            JSONObject l_data =  data_obj.getJSONObject("l_data");

                            ridesList.add(new MyRidesMonthlyModel(data_obj.getString("id"),
                                    data_obj.getString("user_v_name"),
                                    data_obj.getString("d_time"),
                                    l_data.getString("final_amount"),
                                    l_data.getString("payment_mode"),
                                    data_obj.getString("user_v_image")));
                        }
                        myRidesMonthlyAdapter.notifyDataSetChanged();

                    } else {
                        tv_monthly_no_record.setVisibility(View.VISIBLE);
                        rv_monthly_ride.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);
    }

}
