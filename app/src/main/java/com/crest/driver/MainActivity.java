package com.crest.driver;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.crest.driver.AdapterClasses.RideCancelAdapter;
import com.crest.driver.ModelClasses.RideCancelModel;
import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.FileUtils;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyRequestClassNew;
import com.crest.driver.VolleyLibrary.VolleyTAG;
import com.crest.driver.fragment.DashboardFragment;
import com.crest.driver.fragment.MyEarningsFragment;
import com.crest.driver.fragment.MyRidesFragment;

import com.crest.driver.fragment.MyWalletFragment;


import com.crest.driver.fragment.TermsandConditionsFragment;
import com.crest.driver.other.CircleTransform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

import static android.R.attr.value;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Dialog dialog1;


    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Button bt_accept, bt_denied, bt_cancel, bt_done;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, edit_profile, actionbar_title, logout;
    private Toolbar toolbar;
    private RecyclerView rv_cancl_reason;
    private RadioButton rb_other;
    private RideCancelAdapter adapter;
    boolean doubleBackToExitPressedOnce = false;
    private List<RideCancelModel> list = new ArrayList<>();
    EditText et_reason;

    String mAddType,mBuzzTime,mRideId,mUserId,mBussId,mRoundId,mType = "";
    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://thetechhacker.com/wp-content/uploads/2014/11/3.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_DASHBOARD = "DASHBOARD";
    private static final String TAG_MY_RIDES = "MY RIDES";
    private static final String TAG_MY_EARNING = "MY EARNING";
    private static final String TAG_MY_WALLET = "MY WALLET";
    private static final String TAG_TERMS_CONDITIONS = "TERMS CONDITIONS";

    public static String CURRENT_TAG = TAG_DASHBOARD;
    private BroadcastReceiver mReceiveMessageFromNotification;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.e("TAG","onCreate");
        startService(Preferences.getValue(MainActivity.this, Preferences.DRIVER_ID),
                Preferences.getValue(MainActivity.this, Preferences.DRIVER_AUTH_TOKEN),
                Preferences.getValue(MainActivity.this, Preferences.VEHICLE_ID),"0");

        dialog1 = new Dialog(MainActivity.this);
        dialog1.setContentView(R.layout.dialog_ride_request);
        dialog1.setCancelable(false);

        showTimerDialog();

        mHandler = new Handler();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        actionbar_title = (TextView) findViewById(R.id.actionbar_title);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        edit_profile = (TextView) navHeader.findViewById(R.id.edit_profile);
        logout = (TextView) navHeader.findViewById(R.id.logout);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        edit_profile.setOnClickListener(this);
        logout.setOnClickListener(this);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        actionbar_title.setText(R.string.nav_dashboard);
        if (Constant.isOnline(getApplicationContext())) {
            getDriverProfileAPI();
        }
        // load nav menu header data
       //loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASHBOARD;
            loadHomeFragment();
        }

        getMessageFromNotification();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},05);

            return;
        }
        isGpsAnable();

    }

    public void startService(String login_id,String v_token,String i_vehicle_id,String i_ride_id){
        Intent myIntent = new Intent(MainActivity.this,UpdateLocationService.class);
        myIntent.putExtra("login_id",login_id);
        myIntent.putExtra("v_token",v_token);
        myIntent.putExtra("i_vehicle_id",i_vehicle_id);
        myIntent.putExtra("i_ride_id",i_ride_id);
        myIntent.putExtra("run_type","dry");
        startService(myIntent);
    }

    private void isGpsAnable(){
        LocationManager lm = (LocationManager)MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("GPS Network Not Available");
            dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MainActivity.this.startActivity(myIntent);
                    //get gps
                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                }
            });

            dialog.show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Checking the request code of our request
        if(requestCode == 05){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                isGpsAnable();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void dialogTimer(String mTime, final String mBussId, final String mRideId,final String mRoundId) {
        // set the custom dialog components - text, image and button
        final TextView tv_time = (TextView) dialog1.findViewById(R.id.tv_time);
        final ProgressBar progressBar = (ProgressBar)dialog1.findViewById(R.id.progress_bar);
        int sec = Integer.valueOf(mTime);
        int time = sec* 1000;
        final CountDownTimer countDownTimer = new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_time.setText("" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                dialog1.dismiss();
            }

        }.start();
        bt_accept = (Button) dialog1.findViewById(R.id.bt_accept);
        bt_denied = (Button) dialog1.findViewById(R.id.bt_denied);
        bt_denied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBuzzTime = "";
                if (Constant.isOnline(getApplicationContext())) {
                    buzz_action_api(mBussId,mRideId,dialog1,mRoundId);
                }
                dialog1.dismiss();
                show_req_cancl_dialog(mRoundId);

            }
        });
        // if button is clicked, close the custom dialog
        bt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.onFinish();
//                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//                startActivity(intent);
                buzz_action_accept_api(mBussId,mRideId,dialog1,mRoundId,progressBar);

            }
        });


        dialog1.show();
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void buzz_action_accept_api(String mBussId, final String mRideId, final Dialog dialog, final String mRoundId, final ProgressBar progressBar) {
        FileUtils.showProgressBar(MainActivity.this,progressBar);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_BUZZ_ACTION).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("i_buzz_id", mBussId);
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("i_ride_id", mRideId);
        urlBuilder.addQueryParameter("v_token",  Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("i_vehicle_id",  Preferences.getValue(getApplicationContext(), Preferences.VEHICLE_ID));
        urlBuilder.addQueryParameter("i_round_id", mRoundId);
        urlBuilder.addQueryParameter("action", "accept");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("run_type", "dry");
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
                        FileUtils.hideProgressBar(MainActivity.this,progressBar);
                        dialog.dismiss();
                        getRideAPI(mRideId);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        FileUtils.hideProgressBar(MainActivity.this,progressBar);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);

    }



    ///////////////////////////////////Ride Detalil////////F////////////////////////////////

    private void getRideAPI(final String mRideId) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_GET_RIDE).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(MainActivity.this, Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token", Preferences.getValue(MainActivity.this, Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("i_ride_id", mRideId);
        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();
        VolleyRequestClass.allRequest(MainActivity.this, newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    int responce_status = response.getInt(VolleyTAG.status);
                    String message = response.getString(VolleyTAG.message);
                    if (responce_status == VolleyTAG.response_status) {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONObject l_data = jsonObject.getJSONObject("l_data");
                        JSONObject user_data = jsonObject.getJSONObject("user_data");
                        String vehicle_type = l_data.getString("vehicle_type");
                        String estimate_amount = l_data.getString("estimate_amount");
                        String pickup_address = l_data.getString("pickup_address");
                        String destination_addres = l_data.getString("destination_address");

                        String pic_lat = l_data.getString("pickup_latitude");
                        String pic_long = l_data.getString("pickup_longitude");
                        String drop_lat = l_data.getString("destination_latitude");
                        String drop_long = l_data.getString("destination_longitude");

                        String phone = user_data.getString("v_phone");

                        Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                        intent.putExtra("pickup_address",pickup_address);
                        intent.putExtra("destination_address",destination_addres);
                        intent.putExtra("pic_lat",pic_lat);
                        intent.putExtra("pic_long",pic_long);
                        intent.putExtra("drop_lat",drop_lat);
                        intent.putExtra("drop_long",drop_long);
                        intent.putExtra("v_phone",phone);
                        intent.putExtra("i_ride_id",mRideId);
                        startActivity(intent);

                    } else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////


    private void buzz_action_api(String mBussId, String mRideId, final Dialog dialog,final String mRoundId) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_BUZZ_ACTION).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("i_buzz_id", mBussId);
        urlBuilder.addQueryParameter("i_ride_id", mRideId);
        urlBuilder.addQueryParameter("action", "denied");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token",  Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("i_vehicle_id",  Preferences.getValue(getApplicationContext(), Preferences.VEHICLE_ID));
        urlBuilder.addQueryParameter("i_round_id", mRoundId);
        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();

        VolleyRequestClass.allRequest(MainActivity.this, newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    int responce_status = response.getInt(VolleyTAG.status);
                    String message = response.getString(VolleyTAG.message);
                    if (responce_status == VolleyTAG.response_status) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, true);

    }

    private void getDriverProfileAPI() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_DRIVER_GET_PROFILE).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();
        VolleyRequestClass.allRequest(MainActivity.this, newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    int responce_status = response.getInt(VolleyTAG.status);
                    String message = response.getString(VolleyTAG.message);
                    if (responce_status == VolleyTAG.response_status) {
//                        Toast.makeText(EditProfile.this, message, Toast.LENGTH_LONG).show();
                        JSONObject jsonObject = response.getJSONObject("data");
                        txtName.setText(jsonObject.getString("v_name"));
                        if (jsonObject.getString("v_image").equals("")) {

                        } else {
                            Glide.with(MainActivity.this).load(jsonObject.getString("v_image"))
                                    .crossFade()
                                    .thumbnail(0.5f)
                                    .bitmapTransform(new CircleTransform(MainActivity.this))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imgProfile);
                        }

//                        et_vehicle_number.setText(jsonObject.getString("v_phone"));
//                        et_mobile_number.setText(jsonObject.getString(""));
//                        if (jsonObject.getString("v_image").equals("")) ;
//                        Picasso.with(getApplicationContext()).load("" + jsonObject.getString("v_image").toString().replaceAll("\'", "")).error(R.drawable.add_image).placeholder(R.drawable.add_image).memoryPolicy(MemoryPolicy.NO_CACHE).into(img_profile);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText("Ravi Tamada");
//        txtWebsite.setText("www.androidhive.info");

        // loading header background image
//        Glide.with(this).load(urlNavHeaderBg)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);


    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item

        //Closing drawer on item click
        drawer.closeDrawers();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            // show or hide the fab button
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                DashboardFragment dashboardFragment = new DashboardFragment();
                return dashboardFragment;
            case 1:
                // photos
                MyRidesFragment myRidesFragment = new MyRidesFragment();

                return myRidesFragment;
            case 2:
                // movies fragment
                MyWalletFragment myWalletFragment = new MyWalletFragment();
                return myWalletFragment;
            case 3:
                // notifications fragment
                MyEarningsFragment myEarningsFragment = new MyEarningsFragment();
                return myEarningsFragment;

            case 4:
                // notifications fragment
                TermsandConditionsFragment termsandConditionsFragment = new TermsandConditionsFragment();
                return termsandConditionsFragment;

            case 5:
                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();


                // settings fragment
//                MyWalletFragment myWalletFragment = new MyWalletFragment();
//                return myWalletFragment;
//            case 5:
//                // settings fragment
//                NotificationsFragment notificationsFragment = new NotificationsFragment();
//                return notificationsFragment;
//            case 6:
//                // settings fragment
//                FeedbackFragment feedbackFragment = new FeedbackFragment();
//                return feedbackFragment;
            default:
                return new DashboardFragment();
        }
    }


    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }


    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_dashboard:
                        navItemIndex = 0;
                        actionbar_title.setText(R.string.nav_dashboard);
                        CURRENT_TAG = TAG_DASHBOARD;
                        break;
                    case R.id.nav_my_rides:
                        navItemIndex = 1;
                        actionbar_title.setText(R.string.nav_my_rides);
                        CURRENT_TAG = TAG_MY_RIDES;
                        break;
                    case R.id.nav_my_wallet:
                        navItemIndex = 2;
                        actionbar_title.setText(R.string.nav_my_wallet);
                        CURRENT_TAG = TAG_MY_WALLET;
                        break;
                    case R.id.nav_my_earnings:
                        navItemIndex = 3;
                        actionbar_title.setText(R.string.nav_my_earnings);
                        CURRENT_TAG = TAG_MY_EARNING;
                        break;
                    case R.id.nav_my_terms_conditions:
                        navItemIndex = 4;
                        actionbar_title.setText(R.string.terms_conditions);
                        CURRENT_TAG = TAG_TERMS_CONDITIONS;
                        break;

                    default:
//                        navItemIndex = 0;
//                        actionbar_title.setText(R.string.nav_dashboard);
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

        /*if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
        finishAffinity();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_DASHBOARD;
                loadHomeFragment();
                return;
            }
        }*/

        super.onBackPressed();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawers();
                }
                Intent mIntent = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(mIntent);
                break;

            case R.id.logout:
                driver_logout_api();
                break;
        }
    }

    private void show_req_cancl_dialog(final String mRoundId) {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_request_cancl_reason);

        rv_cancl_reason = (RecyclerView) dialog.findViewById(R.id.rv_cancl_reason);
//        rb_other = (RadioButton) dialog.findViewById(R.id.rb_other);
        et_reason = (EditText) dialog.findViewById(R.id.et_reason);
        bt_cancel = (Button) dialog.findViewById(R.id.bt_cancel);
        bt_done = (Button) dialog.findViewById(R.id.bt_done);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_cancl_reason.setLayoutManager(layoutManager);
        adapter = new RideCancelAdapter(list);
        rv_cancl_reason.setAdapter(adapter);

        if (Constant.isOnline(getApplicationContext())) {
            getRideCancelReason_Api();
        }

        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.isOnline(getApplicationContext())) {
                    driver_cancel_ride(dialog,mRoundId);
                }

            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    private void driver_cancel_ride(final Dialog dialog,String mRoundId) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_DRIVER_CANCEL_RIDE).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("i_ride_id", "2");
        urlBuilder.addQueryParameter("cancel_reason_id",Preferences.getValue(getApplicationContext(),"cancel_id") );
        urlBuilder.addQueryParameter("cancel_reason_text", et_reason.getText().toString());
        urlBuilder.addQueryParameter("i_vehicle_id",  Preferences.getValue(getApplicationContext(), Preferences.VEHICLE_ID));
        urlBuilder.addQueryParameter("i_round_id", mRoundId);
        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        VolleyRequestClassNew.allRequest(getApplicationContext(), newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    int responce_status = response.getInt(VolleyTAG.status);
                    String message = response.getString(VolleyTAG.message);
                    if (responce_status == VolleyTAG.response_status) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, true);

    }

    private void getRideCancelReason_Api() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_GET_RIDE_CANCEL).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("v_type", "driver");
        urlBuilder.addQueryParameter("v_token",  Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
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
                        list.clear();
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            list.add(new RideCancelModel(jsonObject.getString("j_title"),jsonObject.getString("id")));
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);

    }

    private void driver_logout_api() {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_LOGOUT).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("v_token",  Preferences.getValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getApplicationContext(), Preferences.DRIVER_ID));

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

                        stopService(new Intent(MainActivity.this, UpdateLocationService.class));
                        Preferences.setValue(getApplicationContext(), Preferences.DRIVER_ID, "");
                        Preferences.setValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN, "");
                        Preferences.setValue(getApplicationContext(), Preferences.VEHICLE_ID, "");
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);

                    } else {
//                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("TAG","onStart()");
    }

    private void showTimerDialog(){
        if(getIntent().getExtras() != null){
            mAddType = getIntent().getExtras().getString("pickup_address","");
            mBuzzTime = getIntent().getExtras().getString("buzz_time","");
            mRideId = getIntent().getExtras().getString("i_ride_id","");
            mUserId = getIntent().getExtras().getString("i_user_id","");
            mBussId = getIntent().getExtras().getString("i_buzz_id","");
            mRoundId = getIntent().getExtras().getString("i_round_id","");
            mType = getIntent().getExtras().getString("type","");
            Log.e("TAG","mBuzzTime = "+mBuzzTime);
            Log.e("TAG","mRoundId = "+mRoundId);
            Log.e("TAG","mType = "+mType);

            if(mType!="" && mType.equalsIgnoreCase("driver_ride_buzz"))
                dialogTimer(mBuzzTime,mBussId,mRideId,mRoundId);
        }
    }

    private void getMessageFromNotification(){
        Log.e("TAG","Nikunj");
        mReceiveMessageFromNotification = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                Log.e("TAG","that means device is registered successfully");
                if(intent.getAction().equals(MyFirebaseMessagingService.MESSAGE_SUCCESS)){
                    //Toast.makeText(getApplicationContext(), "MESSAGE_SUCCESS", Toast.LENGTH_LONG).show();
                    if(dialog1.isShowing()){
                       dialog1.dismiss();
                    }
                } else if (intent.getAction().equals(MyFirebaseMessagingService.MESSAGE_NOTIFICATION)){
                    Log.e("TAG","MESSAGE_NOTIFICATION Buss time = "+intent.getStringExtra("buzz_time"));
                    if(intent.getExtras() != null){
                        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(5);

                        mAddType = intent.getStringExtra("pickup_address");
                        mBuzzTime = intent.getStringExtra("buzz_time");
                        mRideId = intent.getStringExtra("i_ride_id");
                        mUserId = intent.getStringExtra("i_user_id");
                        mBussId = intent.getStringExtra("i_buzz_id");
                        mRoundId = intent.getStringExtra("i_round_id");
                        mType = intent.getStringExtra("type");
                        Log.e("TAG","mType2 = "+mType);
                        Log.e("TAG","mRoundId = "+mRoundId);
                        Log.e("TAG","mBuzzTime = "+mBuzzTime);

                        if(mType!="" && mType.equalsIgnoreCase("driver_ride_buzz"))
                            dialogTimer(mBuzzTime,mBussId,mRideId,mRoundId);
                    }
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

        isGpsAnable();

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiveMessageFromNotification,
                new IntentFilter(MyFirebaseMessagingService.MESSAGE_SUCCESS));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiveMessageFromNotification,
                new IntentFilter(MyFirebaseMessagingService.MESAGE_ERROR));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiveMessageFromNotification,
                new IntentFilter(MyFirebaseMessagingService.MESSAGE_NOTIFICATION));
    }

}