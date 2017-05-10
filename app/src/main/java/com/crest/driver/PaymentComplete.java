package com.crest.driver;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crest.driver.AdapterClasses.CompleteRideAdapter;
import com.crest.driver.AdapterClasses.PaymentCompleteAdapter;
import com.crest.driver.ModelClasses.CompleteRideModel;
import com.crest.driver.ModelClasses.PaymentCompleteModel;
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

public class PaymentComplete extends AppCompatActivity implements View.OnClickListener{
    private TextView actionbar_title;
    private RecyclerView rv_payment;
    private PaymentCompleteAdapter paymentCompleteAdapter;
    public static ArrayList<PaymentCompleteModel> payList = new ArrayList<>();
    private Button bt_rate_ride;

    private String mRideTime;
    private String mAmount;
    private String mPickupAddress;
    private String mDestinationAddress;
    private String mRideId;

    private float mTotalPayment = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_complete);

        if(getIntent().getExtras() != null){
            mPickupAddress = getIntent().getExtras().getString("pickup_address");
            mDestinationAddress = getIntent().getExtras().getString("destination_address");
            mRideTime = getIntent().getExtras().getString("date");
            mAmount = getIntent().getExtras().getString("amount");
            mRideId = getIntent().getExtras().getString("i_ride_id");
        }

        initUI();


        paymentCompleteAdapter = new PaymentCompleteAdapter(payList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_payment.setLayoutManager(mLayoutManager);
        rv_payment.setItemAnimator(new DefaultItemAnimator());
        rv_payment.setAdapter(paymentCompleteAdapter);
    }

    private void initUI() {

        actionbar_title=(TextView)findViewById(R.id.actionbar_title);
        rv_payment=(RecyclerView)findViewById(R.id.rv_payment);
        bt_rate_ride=(Button)findViewById(R.id.bt_rate_ride);

        bt_rate_ride.setOnClickListener(this);

        actionbar_title.setText("Payment Complete");

        ride_payment_api(mRideId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.bt_rate_ride:
                Intent intent=new Intent(getApplicationContext(),RateThisRide.class);
                intent.putExtra("pickup_address",mPickupAddress);
                intent.putExtra("destination_address",mDestinationAddress);
                intent.putExtra("date",mRideTime);
                intent.putExtra("amount",mAmount);
                intent.putExtra("i_ride_id",mRideId);
                startActivity(intent);
                break;

        }
    }

    private void ride_payment_api(final String mRideId) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.RIDE_PAYMENT).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token",  Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("i_ride_id", mRideId);

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

                        payList.clear();
                        JSONObject data = response.getJSONObject("data");
                        JSONArray payment_data = data.getJSONArray("payment_data");
                        for (int i=0;i<payment_data.length();i++){
                            payList.add(new PaymentCompleteModel(payment_data.getJSONObject(i).getString("v_type"),payment_data.getJSONObject(i).getString("f_amount")));
                            mTotalPayment = mTotalPayment + Float.valueOf(payment_data.getJSONObject(i).getString("f_amount"));
                        }
                        payList.add(new PaymentCompleteModel("Total",String.valueOf(mTotalPayment)));
                        paymentCompleteAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}
