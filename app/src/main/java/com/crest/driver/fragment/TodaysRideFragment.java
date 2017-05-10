package com.crest.driver.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crest.driver.AdapterClasses.MyRidesTodayAdapter;
import com.crest.driver.ModelClasses.MyRidesModel;
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


public class TodaysRideFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private RecyclerView rv_today_ride;
    MyRidesTodayAdapter myRidesTodayAdapter;
    View view;
    public static ArrayList<MyRidesModel> ridesList;
    private TextView tv_today_no_record;

    public TodaysRideFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void initUI() {
        rv_today_ride = (RecyclerView) view.findViewById(R.id.rv_today_ride);
        tv_today_no_record = (TextView) view.findViewById(R.id.tv_today_no_record);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.crest.goyo.fragment
        view = inflater.inflate(R.layout.fragment_todays_ride, container, false);

        initUI();
        ridesList = new ArrayList<MyRidesModel>();
//        for (int i = 0; i < 10; i++) {
//            ridesList.add(new MyRidesModel("Annie TATE. ", "12/07/2016 at 03:00 PM", "8.5 Rs", "Wallet"));
//
//        }

        myRidesTodayAdapter = new MyRidesTodayAdapter(ridesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_today_ride.setLayoutManager(mLayoutManager);
        rv_today_ride.setItemAnimator(new DefaultItemAnimator());
        rv_today_ride.setAdapter(myRidesTodayAdapter);

        getDriverRides_daily_api();

        return view;
    }

    private void getDriverRides_daily_api() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_GET_DRIVER_RIDES).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token",Preferences.getValue(getContext(),Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("sort_by", "today");

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

                            ridesList.add(new MyRidesModel(data_obj.getString("id"),
                                    data_obj.getString("user_v_name"),
                                    data_obj.getString("d_time"),
                                    l_data.getString("final_amount"),
                                    l_data.getString("payment_mode"),
                                    data_obj.getString("user_v_image")));
                        }
                        myRidesTodayAdapter.notifyDataSetChanged();


                    } else {
                        tv_today_no_record.setVisibility(View.VISIBLE);
                        rv_today_ride.setVisibility(View.GONE);

//                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);
    }

}
