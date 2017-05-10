package com.crest.driver;


import android.content.Intent;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.FileUtils;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyTAG;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import okhttp3.HttpUrl;

public class RateThisRide extends AppCompatActivity implements View.OnClickListener {
    private TextView actionbar_title;
    private Button bt_rate_now;

    private String mRideTime;
    private String mAmount;
    private String mPickupAddress;
    private String mDestinationAddress;
    private String mRideId;


    private TextView mDate;
    private TextView mRs;
    private TextView mPicAddress;
    private TextView mDropAddress;
    private RatingBar mRatingBar;
    private EditText mCommnent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_rate_this_ride);

        if(getIntent().getExtras() != null){
            mPickupAddress = getIntent().getExtras().getString("pickup_address");
            mDestinationAddress = getIntent().getExtras().getString("destination_address");
            mRideTime = getIntent().getExtras().getString("date");
            mAmount = getIntent().getExtras().getString("amount");
            mRideId = getIntent().getExtras().getString("i_ride_id");
        }

        initUI();
    }

    private void initUI() {
        actionbar_title=(TextView)findViewById(R.id.actionbar_title);
        bt_rate_now=(Button)findViewById(R.id.bt_rate_now);
        mDate = (TextView)findViewById(R.id.txt_date);
        mRs = (TextView)findViewById(R.id.txt_amount);
        mPicAddress = (TextView)findViewById(R.id.txt_pic_address);
        mDropAddress = (TextView)findViewById(R.id.txt_drop_address);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        mRatingBar = (RatingBar)findViewById(R.id.rating_bar);
        mCommnent = (EditText)findViewById(R.id.txt_comment);

        bt_rate_now.setOnClickListener(this);
        actionbar_title.setText(R.string.actionbar_rate_ride);
        mDate.setText(FileUtils.setDate(RateThisRide.this,mRideTime));
        mRs.setText("₹"+mAmount);
        mPicAddress.setText(mPickupAddress);
        mDropAddress.setText(mDestinationAddress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_rate_now:
                if(!mCommnent.getText().toString().equalsIgnoreCase(""))
                {
                    rate_ride_api(mCommnent.getText().toString(),String.valueOf(mRatingBar.getRating()));
                }else {
                    mCommnent.setError("Please enter your coment.");
                }

                break;
        }

    }

    private void rate_ride_api(String mComment,String mRate) {
        FileUtils.showProgressBar(RateThisRide.this,progressBar);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.RATE_THIS_RIDE).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("i_ride_id", mRideId);
        urlBuilder.addQueryParameter("v_type", "driver");
        urlBuilder.addQueryParameter("l_comment", mComment);
        urlBuilder.addQueryParameter("i_rate", mRate);

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
                        FileUtils.hideProgressBar(RateThisRide.this,progressBar);
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        FileUtils.hideProgressBar(RateThisRide.this,progressBar);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);

    }
}
