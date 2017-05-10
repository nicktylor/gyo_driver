package com.crest.driver;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crest.driver.AdapterClasses.GetChargesAdapter;
import com.crest.driver.ModelClasses.AddAmountModel;
import com.crest.driver.ModelClasses.GetExtraAmountModel;
import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.FileUtils;
import com.crest.driver.Utils.GPSTracker;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyTAG;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.HttpUrl;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,View.OnClickListener,android.location.LocationListener{

    private GoogleMap mMap;
    GPSTracker gps;

    private TextView actionbar_title,tv_add_amount;
     Dialog dialog;
    private LinearLayout lay_complete_ride,lay_start_ride,lay_select_location_simple,lay_select_location;
    private RelativeLayout lay_dr_start_ride,lay_dr_complete_ride;

    private TextView mPickup;
    private TextView mDestatioonation;
    private TextView mNumber;
    private TextView mPicAddress;
    private TextView mDropAddress;
    private LinearLayout mCall;
    private LinearLayout mStartRide;

    private String mPickupAddress;
    private String mDestinationAddress;

    private double pic_lat;
    private double pic_long;
    private double drop_lat;
    private double drop_long;
    private double current_lat;
    private double current_long;

    private String phone;
    private String s_pic_lat;
    private String s_pic_long;
    private String s_drop_lat;
    private String s_drop_long;
    private String mRideId;

    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 3000;
    private static final float LOCATION_DISTANCE = 10f;

    private LatLng CURRENT_LAT_LONG;
    private LatLng PICUP_LAT_LONG;
    private LatLng DROP_LAT_LONG;

    private HashMap<String,String> hashMap = new HashMap<String, String>();

    private ArrayList<String> mSpineerItemsArray = new ArrayList<>();
    private ArrayList<String> mSpineerItemsArrayKey = new ArrayList<>();
    private ProgressBar progressBar;

    //private ArrayList<LatLng> mMarkerPoints = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(getIntent().getExtras() !=null){
            mPickupAddress = getIntent().getExtras().getString("pickup_address");
            mDestinationAddress = getIntent().getExtras().getString("destination_address");
            s_pic_lat= getIntent().getExtras().getString("pic_lat");
            s_pic_long= getIntent().getExtras().getString("pic_long");
            s_drop_lat= getIntent().getExtras().getString("drop_lat");
            s_drop_long= getIntent().getExtras().getString("drop_long");
            phone = getIntent().getExtras().getString("v_phone");
            mRideId = getIntent().getExtras().getString("i_ride_id");

            pic_lat = Double.valueOf(s_pic_lat);
            pic_long = Double.valueOf(s_pic_long);
            drop_lat = Double.valueOf(s_drop_lat);
            drop_long = Double.valueOf(s_drop_long);

            PICUP_LAT_LONG = new LatLng(pic_lat,pic_long);
            DROP_LAT_LONG = new LatLng(drop_lat,drop_long);

            Log.e("TAG","1");

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gps = new GPSTracker(MapsActivity.this, MapsActivity.this);
        if (gps.canGetLocation()) {
            current_lat = gps.getLatitude();
            current_long = gps.getLongitude();
            Log.d("######", "lat: " + current_lat + "long :" + current_long);
            /*PICUP_LAT_LONG = new LatLng(23.0425,72.5145);
            DROP_LAT_LONG = new LatLng(23.2156,72.6369);*/
            CURRENT_LAT_LONG = new LatLng(current_lat,current_long);
            Log.e("TAG","2");
        } else {
            gps.showSettingsAlert();
        }

        ride_charges_type_api();

        initUI();

    }

    private void initUI() {

        getMessageFromNotification();

        mPickup =(TextView)findViewById(R.id.txt_pickup);
        mDestatioonation =(TextView) findViewById(R.id.txt_destatination);
        mNumber = (TextView)findViewById(R.id.txt_cust_no);
        mCall = (LinearLayout)findViewById(R.id.ll_call);
        mStartRide = (LinearLayout)findViewById(R.id.lay_start_ride);
        mPicAddress = (TextView)findViewById(R.id.txt_pic_address);
        mDropAddress = (TextView)findViewById(R.id.txt_drop_address);

        lay_start_ride=(LinearLayout)findViewById(R.id.lay_start_ride);
        lay_dr_start_ride=(RelativeLayout)findViewById(R.id.lay_dr_start_ride);
        lay_dr_complete_ride=(RelativeLayout)findViewById(R.id.lay_dr_complete_ride);
        lay_select_location_simple=(LinearLayout)findViewById(R.id.lay_select_location_simple);
        lay_select_location=(LinearLayout)findViewById(R.id.lay_select_location);
        lay_complete_ride=(LinearLayout)findViewById(R.id.lay_complete_ride);
        actionbar_title=(TextView)findViewById(R.id.actionbar_title);
        tv_add_amount=(TextView)findViewById(R.id.tv_add_amount);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        //mPickup.setText(mPickupAddress);
        mDestatioonation.setText(mPickupAddress);
        mNumber.setText(phone);

        actionbar_title.setText("My Dry Run");
         dialog = new Dialog(MapsActivity.this);
        lay_start_ride.setOnClickListener(this);
        lay_complete_ride.setOnClickListener(this);
        tv_add_amount.setOnClickListener(this);


        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone!=null && !phone.isEmpty())
                call(phone);
            }
        });

        mStartRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPinDialog();
            }
        });
    }

    private void call(String no){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CALL_PHONE},05);
            return;
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+no.trim()));
        startActivity(callIntent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 05){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                call(phone);
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(MapsActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lay_start_ride:

                dialog.setContentView(R.layout.dialog_confirm_pin);
                Button bt_apply = (Button) dialog.findViewById(R.id.btn_apply);
                bt_apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionbar_title.setText("Start riding");
                        lay_dr_start_ride.setVisibility(View.GONE);
                        lay_select_location_simple.setVisibility(View.GONE);
                        lay_select_location.setVisibility(View.VISIBLE);
                        lay_dr_complete_ride.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                break;
            case R.id.lay_complete_ride:
                ride_complete_api();
                break;
            case R.id.tv_add_amount:
                addExtraAmountDialog();
                break;
        }
    }

    private void ride_complete_api() {
        FileUtils.showProgressBar(MapsActivity.this,progressBar);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.RIDE_COMPLETE).newBuilder();
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
                        FileUtils.hideProgressBar(MapsActivity.this,progressBar);

                        stopService(new Intent(MapsActivity.this, UpdateLocationService.class));
                        startService(Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID),
                                Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN),
                                Preferences.getValue(getApplicationContext(), Preferences.VEHICLE_ID),"0");

                        Intent intent=new Intent(MapsActivity.this,CompleteRidesDetails.class);
                        intent.putExtra("i_ride_id",mRideId);
                        intent.putExtra("pickup_address",mPickupAddress);
                        intent.putExtra("destination_address",mDestinationAddress);
                        startActivity(intent);

                    } else {
                        FileUtils.hideProgressBar(MapsActivity.this,progressBar);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);

    }

    private class CompleteRide extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


    private void confirmPinDialog() {
        final Dialog dialog = new Dialog(MapsActivity.this);
        dialog.setContentView(R.layout.dialog_confirm_pin);
        dialog.setCancelable(false);
        // set the custom dialog components - text, image and button
        final EditText mPin = (EditText) dialog.findViewById(R.id.edttxt_pin);
        Button bt_accept = (Button) dialog.findViewById(R.id.btn_apply);
        Button bt_denied = (Button) dialog.findViewById(R.id.btn_cancel);
        final ProgressBar progressBar11 = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        bt_denied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // if button is clicked, close the custom dialog
        bt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = mPin.getText().toString().trim();
                if (!pin.isEmpty() && pin != null)
                {
                    FileUtils.showProgressBar(MapsActivity.this,progressBar11);
                    confirm_pin_api(mRideId,pin,dialog,progressBar11);
                }else {
                    mPin.setError("Please Enter Pin");
                }
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    public void startService(String login_id,String v_token,String i_vehicle_id,String i_ride_id){
        Intent myIntent = new Intent(MapsActivity.this,UpdateLocationService.class);
        myIntent.putExtra("login_id",login_id);
        myIntent.putExtra("v_token",v_token);
        myIntent.putExtra("i_vehicle_id",i_vehicle_id);
        myIntent.putExtra("i_ride_id",i_ride_id);
        myIntent.putExtra("run_type","ride");
        startService(myIntent);
    }

    private void confirm_pin_api(final String mRideId, final String mPin , final Dialog dialog, final ProgressBar progressBar) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.START_RIDE).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token",  Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("i_vehicle_id",  Preferences.getValue(getApplicationContext(), Preferences.VEHICLE_ID));
        urlBuilder.addQueryParameter("i_ride_id", mRideId);
        urlBuilder.addQueryParameter("v_pin", mPin);
        urlBuilder.addQueryParameter("run_type", "ride");

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
                        FileUtils.hideProgressBar(MapsActivity.this,progressBar);
                        dialog.dismiss();
                        lay_dr_start_ride.setVisibility(View.GONE);
                        lay_select_location_simple.setVisibility(View.GONE);
                        lay_dr_complete_ride.setVisibility(View.VISIBLE);
                        lay_select_location.setVisibility(View.VISIBLE);
                        mPicAddress.setText(mPickupAddress);
                        mDropAddress.setText(mDestinationAddress);
                        stopService(new Intent(MapsActivity.this, UpdateLocationService.class));
                        startService(Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID),
                                Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN),
                                Preferences.getValue(getApplicationContext(), Preferences.VEHICLE_ID),mRideId);
                        if(mMap != null){
                            mMap.clear();
                            removeMarker();
                            drawRoot(mMap,PICUP_LAT_LONG,DROP_LAT_LONG);
                        }
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        FileUtils.hideProgressBar(MapsActivity.this,progressBar);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);

    }

    ////////////////////////////////////////////////GOOGLE MAPS CURRENT LOCATION UPDATE/////////////////////////////////

    private void initMyLocation(){

        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    this);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,this);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }


    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        Log.e(TAG, "onLocationChanged: " + location);

        CURRENT_LAT_LONG = new LatLng(location.getLatitude(),location.getLongitude());
        myMarker.setPosition(CURRENT_LAT_LONG);
        my_marker.flat(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT_LAT_LONG,19));
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Log.e(TAG, "onProviderDisabled: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Log.e(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        Log.e(TAG, "onStatusChanged: " + provider);
    }

    //////////////////////////////////////////////////////ADD CHARGE CODE//////////////////////////////////////////////////////////


    private void addExtraAmountDialog() {
        final Dialog dialog = new Dialog(MapsActivity.this);
        dialog.setContentView(R.layout.dialog_add_amount);
        dialog.setCancelable(true);

        Button bt_done = (Button) dialog.findViewById(R.id.bt_done);
        Button btn_add_mount = (Button) dialog.findViewById(R.id.btn_add_amount);
        final ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress_bar);
        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.rc_charges);
        final EditText edt_amount = (EditText)dialog.findViewById(R.id.edttxt_amount);
        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner_add_amount);

        get_charges_api(recyclerView,progressBar);

        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,mSpineerItemsArray);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_add_mount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = edt_amount.getText().toString().trim();
                if(!amount.isEmpty() && amount != null){
                    add_charges_api(mSpineerItemsArrayKey.get(spinner.getSelectedItemPosition()),amount,"",
                            recyclerView,edt_amount,progressBar);
                }else {
                    edt_amount.setError("Please Enter Amount");
                }
            }
        });

        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }



    private void ride_charges_type_api() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.RIDE_CHARGE_TYPE).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");

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
                        dialog.dismiss();

                        JSONArray dataArray = response.getJSONArray("data");
                        for(int i=0; i<dataArray.length();i++){
                            JSONArray datavalue = dataArray.getJSONArray(i);
                            hashMap.put(datavalue.getString(0),datavalue.getString(1));
                        }

                        Iterator myVeryOwnIterator = hashMap.keySet().iterator();
                        while(myVeryOwnIterator.hasNext()) {
                            String key=(String)myVeryOwnIterator.next();
                            mSpineerItemsArrayKey.add(key);
                            mSpineerItemsArray.add(hashMap.get(key));
                        }

                    } else {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);

    }



    private ArrayList<GetExtraAmountModel> arrayListAmount = new ArrayList<>();
    private void get_charges_api(final RecyclerView recyclerView, final ProgressBar progressBar) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.RIDE_GET_CHARGES).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token",  Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
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
                        dialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        if (!arrayListAmount.isEmpty()){
                            arrayListAmount.clear();
                        }
                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){
                            JSONObject obj = data.getJSONObject(i);
                            GetExtraAmountModel getExtraAmountModel = new GetExtraAmountModel();
                            getExtraAmountModel.setAmountType(obj.getString("display_charge_type"));
                            getExtraAmountModel.setAmount(obj.getString("f_amount"));
                            arrayListAmount.add(getExtraAmountModel);
                            Log.e("TAG","05");
                        }

                        Log.e("TAG","07");
                        if (arrayListAmount != null)
                        {
                            Log.e("TAG","08");
                            GetChargesAdapter mAdapter = new GetChargesAdapter(arrayListAmount);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }

                    } else {
                        dialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);
    }

    private void add_charges_api(String mChargeType, String mAmount , String mChargeInfo,
                                 final RecyclerView recyclerView, final EditText editTextAmount, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.RIDE_ADD_CHARGE).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token",  Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("i_ride_id", mRideId);//mRideId
        urlBuilder.addQueryParameter("v_charge_type", mChargeType);
        urlBuilder.addQueryParameter("v_charge_info", mChargeInfo);
        urlBuilder.addQueryParameter("f_amount", mAmount);

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
                        dialog.dismiss();
                        editTextAmount.setText("");
                        get_charges_api(recyclerView,progressBar);
                    } else {
                        dialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);

    }

    ///////////////////////////////////////////////////GOOGLE MAPS CODE//////////////////////////////////////////////////////////////////////

    MarkerOptions my_marker;
    Marker myMarker;
    Marker picMarker;
    Marker dropMarker;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addMarkers();

        drawRoot(mMap,CURRENT_LAT_LONG,PICUP_LAT_LONG);
    }

    private void addMarkers(){
        try{
            MarkerOptions customer_marker_pic = new MarkerOptions().position(PICUP_LAT_LONG)
                    .title("Pickup Point")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_location));
            picMarker = mMap.addMarker(customer_marker_pic);

            MarkerOptions customer_marker_drop = new MarkerOptions().position(DROP_LAT_LONG)
                    .title("Drop Point")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_location));
             dropMarker = mMap.addMarker(customer_marker_drop);

            my_marker = new MarkerOptions().position(CURRENT_LAT_LONG)
                    .title("Me")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_location));
            myMarker = mMap.addMarker(my_marker);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(CURRENT_LAT_LONG));

        }catch (Exception e){
            Log.e("TAG","Error");
        }

        initMyLocation();
    }

    private void removeMarker(){
        my_marker = new MarkerOptions().position(CURRENT_LAT_LONG)
                .title("Me")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_location));
        myMarker = mMap.addMarker(my_marker);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(CURRENT_LAT_LONG));

        picMarker.remove();
        dropMarker.remove();
    }

    private void drawRoot(GoogleMap googleMap,LatLng picup,LatLng drop){
        LatLng origin = picup;
        LatLng dest = drop;
        // Getting URL to the Google Directions API
        String url = getUrl(origin, dest);
        Log.d("onMapClick", url.toString());
        FetchUrl FetchUrl = new FetchUrl();

        // Start downloading json data from Google Directions API
        FetchUrl.execute(url);
        //move map camera
            /*mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));*/
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin,19));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(19), 2000, null);
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }


    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.e("TAG","DATA = "+data);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }

    //////////////////////////////////////////BROADCAST RECEIVER////////////////////////////////////////////////////

    private void customerRideCancleDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MapsActivity.this);
        builder1.setTitle("Ride cancelltion request");
        builder1.setMessage("Customer have cancel this ride");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private BroadcastReceiver mReceiveMessageFromNotification;

    private void getMessageFromNotification(){
        Log.e("TAG","Nikunj");
        mReceiveMessageFromNotification = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                Log.e("TAG","that means device is registered successfully");
                if(intent.getAction().equals(MyFirebaseMessagingService.MESSAGE_SUCCESS_MAPS)){
                    customerRideCancleDialog();
                }
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiveMessageFromNotification);
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiveMessageFromNotification,
                new IntentFilter(MyFirebaseMessagingService.MESSAGE_SUCCESS_MAPS));
    }


}
