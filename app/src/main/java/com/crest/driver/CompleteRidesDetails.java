package com.crest.driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crest.driver.AdapterClasses.CompleteRideAdapter;
import com.crest.driver.AdapterClasses.GetChargesAdapter;
import com.crest.driver.AdapterClasses.MyWalletAdapter;
import com.crest.driver.AdapterClasses.PaymentCompleteAdapter;
import com.crest.driver.ModelClasses.CompleteRideModel;
import com.crest.driver.ModelClasses.GetExtraAmountModel;
import com.crest.driver.ModelClasses.MyWalletModel;
import com.crest.driver.ModelClasses.PaymentCompleteModel;
import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.FileUtils;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyTAG;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.HttpUrl;

public class CompleteRidesDetails extends AppCompatActivity implements View.OnClickListener{

    private Button bt_confirm_payment;


    private ArrayList<GetExtraAmountModel> arrayListAmount = new ArrayList<>();
    private CompleteRideAdapter mAdapter;
    private TextView txt_total;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView txtPickupAddress;
    private TextView txtDestinationAddress;
    private String mPickupAddress;
    private String mDestinationAddress;
    private String mRideId;
    private String mRideTime;
    private String mAmount;
    private float mTotalAmount = 0;
    private TextView txt_km;

    private TextView actionbar_title;
    private RecyclerView rv_payment;
    private PaymentCompleteAdapter paymentCompleteAdapter;
    public static ArrayList<PaymentCompleteModel> payList = new ArrayList<>();
    private float mTotalPayment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_rides_detail);

        if(getIntent().getExtras() !=null){
            mPickupAddress = getIntent().getExtras().getString("pickup_address");
            mDestinationAddress = getIntent().getExtras().getString("destination_address");
            mRideId = getIntent().getExtras().getString("i_ride_id");
        }
        initUI();
    }

    private void initUI() {

        actionbar_title=(TextView)findViewById(R.id.actionbar_title);
        bt_confirm_payment=(Button)findViewById(R.id.bt_confirm_payment);
        txtPickupAddress = (TextView)findViewById(R.id.txt_pic_address);
        txtDestinationAddress = (TextView)findViewById(R.id.txt_drop_address);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.rc_complete_ride);
        txt_total = (TextView)findViewById(R.id.txt_total);
        txt_km = (TextView)findViewById(R.id.txt_km);
        rv_payment = (RecyclerView)findViewById(R.id.rv_payment);

        bt_confirm_payment.setOnClickListener(this);

        actionbar_title.setText("Complete Ride");
        txtPickupAddress.setText(mPickupAddress);
        txtDestinationAddress.setText(mDestinationAddress);

        mAdapter = new CompleteRideAdapter(arrayListAmount,CompleteRidesDetails.this);
        get_ride_api();

        paymentCompleteAdapter = new PaymentCompleteAdapter(payList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_payment.setLayoutManager(mLayoutManager);
        rv_payment.setItemAnimator(new DefaultItemAnimator());
        rv_payment.setAdapter(paymentCompleteAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.bt_confirm_payment:
                Intent intent=new Intent(getApplicationContext(),PaymentComplete.class);
                intent.putExtra("pickup_address",mPickupAddress);
                intent.putExtra("destination_address",mDestinationAddress);
                intent.putExtra("date",mRideTime);
                intent.putExtra("amount",mAmount);
                intent.putExtra("i_ride_id",mRideId);
                startActivity(intent);
                break;

        }
    }

    private void get_ride_api() {
        FileUtils.showProgressBar(CompleteRidesDetails.this,progressBar);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.GET_RIDE).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
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
                        JSONObject data = response.getJSONObject("data");
                        mRideTime = data.getString("d_time");
                        JSONObject l_data = data.getJSONObject("l_data");
                        txt_km.setText(l_data.getString("actual_distance")+" km");
                        get_charges_api();

                    } else {
                        FileUtils.hideProgressBar(CompleteRidesDetails.this,progressBar);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);

    }

    private void get_charges_api() {
        FileUtils.showProgressBar(CompleteRidesDetails.this,progressBar);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.RIDE_GET_CHARGES).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("i_ride_id", mRideId);

        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        final okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();
        VolleyRequestClass.allRequest(getApplicationContext(), newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    int responce_status = response.getInt(VolleyTAG.status);
                    String message = response.getString(VolleyTAG.message);
                    if (responce_status == VolleyTAG.response_status) {
                        FileUtils.hideProgressBar(CompleteRidesDetails.this,progressBar);
                        if (!arrayListAmount.isEmpty()) {
                            arrayListAmount.clear();
                        }
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            GetExtraAmountModel getExtraAmountModel = new GetExtraAmountModel();
                            getExtraAmountModel.setAmountType(obj.getString("display_charge_type"));
                            getExtraAmountModel.setAmount(obj.getString("f_amount"));
                            mTotalAmount = mTotalAmount+ Float.valueOf(obj.getString("f_amount"));
                            arrayListAmount.add(getExtraAmountModel);
                            Log.e("TAG", "05");
                        }
                        //mAmount = String.valueOf(mTotalAmount);
                        txt_total.setText("₹ "+new DecimalFormat("##.##").format(mTotalAmount));
                        GetExtraAmountModel getExtraAmountModel = new GetExtraAmountModel();
                        getExtraAmountModel.setAmountType("Total");
                        getExtraAmountModel.setAmount(new DecimalFormat("##.##").format(mTotalAmount));
                        arrayListAmount.add(getExtraAmountModel);
                        Log.e("TAG", "07");
                        if (arrayListAmount != null) {
                            Log.e("TAG", "08");
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }

                        ride_payment_api(mRideId);

                    } else {
                        FileUtils.hideProgressBar(CompleteRidesDetails.this,progressBar);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);
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
        /*Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);*/
    }
}
