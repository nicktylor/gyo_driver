package com.crest.driver.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crest.driver.AdapterClasses.DashboardViewPagerAdapter;
import com.crest.driver.AdapterClasses.RideCancelAdapter;
import com.crest.driver.MainActivity;
import com.crest.driver.MapsActivity;
import com.crest.driver.ModelClasses.RideCancelModel;
import com.crest.driver.R;
import com.crest.driver.UpdateLocationService;
import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.GPSTracker;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyTAG;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

import static com.google.android.gms.internal.zzt.TAG;


public class DashboardFragment extends Fragment implements LocationListener, LocationSource.OnLocationChangedListener, OnMapReadyCallback, TabLayout.OnTabSelectedListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    private TabLayout activity_tab_layout;
    private ViewPager activity_view_pager;
    private RelativeLayout lay_status;
    private boolean isActive = true;
    private DashboardViewPagerAdapter dashboardViewPagerAdapter;
    private String[] tabTitle = {
            "Today",
            "Weekly",
            "Monthly"
    };
    View view;
    private RadioButton rb_other;
    private RecyclerView rv_cancl_reason;
    private LinearLayoutManager layoutManager;
    private List<RideCancelModel> list = new ArrayList<>();
    private RideCancelAdapter adapter;
    private EditText et_reason;
    private Button bt_cancel, bt_done;
    private TextView tv_status;
    String e_status;

    GPSTracker gps;

    private LatLng CURRENT_LAT_LONG;

    private static final int LOCATION_INTERVAL = 100000;
    private static final float LOCATION_DISTANCE = 100f;



    public DashboardFragment() {
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

        get_driver_duty_status_api();

        for (int i = 0; i < tabTitle.length; i++) {
            activity_tab_layout.addTab(activity_tab_layout.newTab().setText("" + tabTitle[i]));
        }
        dashboardViewPagerAdapter = new DashboardViewPagerAdapter(getActivity().getSupportFragmentManager(), activity_tab_layout.getTabCount());//Adding adapter to pager
        activity_view_pager.setAdapter(dashboardViewPagerAdapter);
        activity_tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        activity_tab_layout.setOnTabSelectedListener(this);
        activity_view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(activity_tab_layout));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_deshbord);
        mapFragment.getMapAsync(this);

    }

    private void get_driver_duty_status_api() {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_DRIVER_DUTY_STATUS).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token",Preferences.getValue(getContext(),Preferences.DRIVER_AUTH_TOKEN));
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

                        JSONObject jsonObject = response.getJSONObject("data");
                        e_status = jsonObject.getString("e_status");
                        //Preferences.setValue(getActivity(),Preferences.DRIVER_STATUS,e_status);
                        if (e_status.toString().equals("active")){
                            isActive = true;
                            lay_status.setBackground(ContextCompat.getDrawable(getContext(),R.color.colorGreen));
                            tv_status.setText("AVAILABLE");
                            ((MainActivity)getActivity()).startService(Preferences.getValue(getActivity(), Preferences.DRIVER_ID),
                                    Preferences.getValue(getActivity(), Preferences.DRIVER_AUTH_TOKEN),
                                    Preferences.getValue(getActivity(), Preferences.VEHICLE_ID),"0");
                        }
                        else {
                            isActive = false;
                            lay_status.setBackground(ContextCompat.getDrawable(getContext(),R.color.colorRed));
                            tv_status.setText("NOT AVAILABLE");
                            getActivity().stopService(new Intent(getActivity(), UpdateLocationService.class));
                        }

//                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                    } else {

                        //Preferences.setValue(getActivity(),Preferences.DRIVER_STATUS,e_status);
//                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.crest.goyo.fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;

    }

    private void initUI() {

        activity_tab_layout = (TabLayout) getView().findViewById(R.id.activity_tab_layout);
        activity_view_pager = (ViewPager) getView().findViewById(R.id.activity_view_pager);
        lay_status = (RelativeLayout) getView().findViewById(R.id.lay_status);
        tv_status = (TextView) getView().findViewById(R.id.tv_status);
        lay_status.setOnClickListener(this);

        gps = new GPSTracker(getActivity(), getActivity());
        if (gps.canGetLocation()) {
            CURRENT_LAT_LONG = new LatLng(gps.getLatitude(),gps.getLongitude());
            Log.e("TAG","2");
        } else {
            gps.showSettingsAlert();
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        activity_view_pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_status:
                if(isActive){
                    get_driver_duty_status_update_api("inactive");
                }else {
                    get_driver_duty_status_update_api("active");
                }
                break;

        }
    }

    private void get_driver_duty_status_update_api(final String status) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_DRIVER_DUTY_STATUS_UPDATE).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token", Preferences.getValue(getContext(), Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("i_vehicle_id",Preferences.getValue(getContext(),Preferences.VEHICLE_ID));
        urlBuilder.addQueryParameter("e_status",status);
        urlBuilder.addQueryParameter("l_latitude",Preferences.getValue(getContext(),Preferences.VEHICLE_ID));
        urlBuilder.addQueryParameter("l_longitude",Preferences.getValue(getContext(),Preferences.VEHICLE_ID));
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
                        //Preferences.setValue(getActivity(),Preferences.DRIVER_STATUS,e_status);
                        if (status.equalsIgnoreCase("active")){
                            isActive = true;
                            lay_status.setBackground(ContextCompat.getDrawable(getContext(),R.color.colorGreen));
                            tv_status.setText("AVAILABLE");
                            ((MainActivity)getActivity()).startService(Preferences.getValue(getActivity(), Preferences.DRIVER_ID),
                                    Preferences.getValue(getActivity(), Preferences.DRIVER_AUTH_TOKEN),
                                    Preferences.getValue(getActivity(), Preferences.VEHICLE_ID),"0");
                        }
                        else {
                            isActive = false;
                            lay_status.setBackground(ContextCompat.getDrawable(getContext(),R.color.colorRed));
                            tv_status.setText("NOT AVAILABLE");
                            getActivity().stopService(new Intent(getActivity(), UpdateLocationService.class));
                        }

                    } else {
//                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }

    GoogleMap mMap;
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        try {
            my_marker = new MarkerOptions().position(CURRENT_LAT_LONG)
                    .title("Me")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_location));
            myMarker = mMap.addMarker(my_marker);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT_LAT_LONG,19));

            initMyLocation();

        }catch (Exception  e){
            Log.e("TAG","Error");
        }

    }

    MarkerOptions my_marker;
    Marker myMarker;

    @Override
    public void onLocationChanged(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        myMarker.setPosition(latLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,19));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private LocationManager mLocationManager = null;

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        }
    }
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

        LatLng latLng = new LatLng(mLocationManager.getLastKnownLocation("").getLatitude(),mLocationManager.getLastKnownLocation("").getLatitude());
        if(myMarker == null){
            my_marker = new MarkerOptions().position(latLng)
                    .title("Me")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_location));
            my_marker.flat(true);

            myMarker = mMap.addMarker(my_marker);
        }
    }
}
