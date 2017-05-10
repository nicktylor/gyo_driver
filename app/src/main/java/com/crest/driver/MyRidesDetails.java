package com.crest.driver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.FileUtils;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyTAG;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.HttpUrl;

public class MyRidesDetails extends AppCompatActivity {
    private TextView actionbar_title,tv_start_time,tv_end_time,tv_total_time;
    private TextView tv_name,tv_description;

    private TextView mPicupPoint;
    private TextView mDropPoint;
    private TextView mTotalRs;
    private TextView mDistance;
    private ImageView mProfilePic;
    private RatingBar mMyRating;
    private TextView mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_my_rides_detail);

        initUI();

        getDriverRideDetails_api();
    }

    private void initUI() {

        actionbar_title=(TextView)findViewById(R.id.actionbar_title);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_description = (TextView) findViewById(R.id.tv_description);
        mPicupPoint = (TextView)findViewById(R.id.txt_picup_point);
        mDropPoint = (TextView)findViewById(R.id.txt_drop_point);
        mProfilePic = (ImageView)findViewById(R.id.img_profile);
        tv_total_time = (TextView)findViewById(R.id.txt_totaltime);
        mMyRating = (RatingBar)findViewById(R.id.MyRating);
        mDate = (TextView)findViewById(R.id.txt_date);
        mTotalRs = (TextView)findViewById(R.id.txt_total_rs);
        mDistance = (TextView)findViewById(R.id.txt_distance);

        actionbar_title.setText("Ride Details");
    }

    private void getDriverRideDetails_api() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_GET_DRIVER_RIDE_DETAIL).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token",Preferences.getValue(getApplicationContext(),Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("i_ride_id",Preferences.getValue(getApplicationContext(),Preferences.RIDE_ID));

        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();

        VolleyRequestClass.allRequest(getApplicationContext(), newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    int responce_status = response.getInt(VolleyTAG.status);
                    String message = response.getString(VolleyTAG.message);
                    if (responce_status == VolleyTAG.response_status) {

                        JSONObject data = response.getJSONObject("data");

                        String startTime = DateUtils.formatDateTime(MyRidesDetails.this, Long.parseLong(data.getString("d_start")), DateUtils.FORMAT_SHOW_TIME);
                        String endTime = DateUtils.formatDateTime(MyRidesDetails.this, Long.parseLong(data.getString("d_end")), DateUtils.FORMAT_SHOW_TIME);
                        tv_start_time.setText(startTime);
                        tv_end_time.setText(endTime);

                        mDate.setText(FileUtils.setDate(MyRidesDetails.this,data.getString("d_time")));
                        mMyRating.setRating(Float.valueOf(data.getString("ride_i_rate")));
                        //tv_total_time.setText(getHours(startTime,endTime));
                        tv_name.setText(data.getString("user_v_name"));
                        tv_description.setText(data.getString("ride_l_comment"));

                        if(data.getString("user_v_name").isEmpty()){
                            Glide
                                    .with(MyRidesDetails.this)
                                    .load(R.drawable.user)
                                    .centerCrop()
                                    .crossFade()
                                    .into(mProfilePic);
                        }else {
                            Glide
                                    .with(MyRidesDetails.this)
                                    .load(data.getString("user_v_name"))
                                    .centerCrop()
                                    .crossFade()
                                    .into(mProfilePic);
                        }

                        JSONObject l_data = data.getJSONObject("l_data");
                        mPicupPoint.setText(l_data.getString("pickup_address"));
                        mDropPoint.setText(l_data.getString("destination_address"));
                        mTotalRs.setText("Rs. "+l_data.getString("final_amount"));
                        mDistance.setText(l_data.getString("actual_distance")+" km");

                        JSONObject trip_time = l_data.getJSONObject("trip_time");
                        int days = Integer.valueOf(trip_time.getString("days"));
                        int hours = Integer.valueOf(trip_time.getString("hours"));
                        int minutes = Integer.valueOf(trip_time.getString("minutes"));
                        int seconds = Integer.valueOf(trip_time.getString("seconds"));
                        if(days!=0){
                            tv_total_time.setText(days+"Day "+hours+":"+minutes+":"+seconds+" hr");
                        }else {
                            tv_total_time.setText(hours+":"+minutes+":"+seconds+" hr");
                        }

//                        tv_end_time.setText(data.getString("d_end"));

                    } else {

//                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);
    }

    private String getHours(String StartDate,String EndDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        int days;
        int min = 0;
        int hours = 0;
        try {
            Date date1 = simpleDateFormat.parse(StartDate);
            Date date2 = simpleDateFormat.parse(EndDate);

            long difference = date2.getTime() - date1.getTime();
            days = (int) (difference / (1000*60*60*24));
            hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            hours = (hours < 0 ? -hours : hours);
            Log.e("TAG","HRS = "+ hours+"Min = "+min);
        }catch (Exception e){

        }
        return String.valueOf(hours) +":"+String.valueOf(min)+" hr";
    }
}
