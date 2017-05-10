package com.crest.driver;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.GPSTracker;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyTAG;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import okhttp3.HttpUrl;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView actionbar_title, tv_forgot_password;
    private Button bt_login, bt_signup;
    private EditText et_email, et_password;
    private String TAG = getClass().getName();
    GPSTracker gps;
    double latitude, longitude;
    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        setLanguage("en");
        initUI();

        Log.d(TAG, "" + FirebaseInstanceId.getInstance().getToken());
        actionbar_title.setText(R.string.actionbar_login);
        tv_forgot_password.setOnClickListener(this);
        bt_signup.setOnClickListener(this);
        bt_login.setOnClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)){
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},05);

            return;
        }

    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == 05){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 100, mLocationListener);

                Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }*/

    private void initUI() {
        Constant.CHECK_GPS = true;
        actionbar_title = (TextView) findViewById(R.id.actionbar_title);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        bt_login = (Button) findViewById(R.id.btn_login);
        bt_signup = (Button) findViewById(R.id.bt_signup);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);


//        et_email.setText("aaronharper.cis@gmail.com");
//        et_password.setText("111111");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forgot_password:
                Intent forgot_password = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(forgot_password);
                break;
            case R.id.bt_signup:
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                break;
            case R.id.btn_login:

                if (validate()) {
                    if (Constant.isOnline(getApplicationContext())) {
                        userLoginAPI();
                    }
                }

                gps = new GPSTracker(v.getContext(), Login.this);
                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    Log.d("######", "lat: " + latitude + "long :" + longitude);

                } else {
                    gps.showSettingsAlert();
                }
                break;
        }
    }


    private void userLoginAPI() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_LOGIN).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("v_username", et_email.getText().toString());
        urlBuilder.addQueryParameter("v_password", et_password.getText().toString());
        urlBuilder.addQueryParameter("v_device_token", FirebaseInstanceId.getInstance().getToken());
        urlBuilder.addQueryParameter("lang", "en");
        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();
        VolleyRequestClass.allRequest(Login.this, newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    int responce_status = response.getInt(VolleyTAG.status);
                    String message = response.getString(VolleyTAG.message);
                    if (responce_status == VolleyTAG.response_status) {
                        Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
                        JSONObject jsonObject = response.getJSONObject("data");
                        Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
                        Preferences.setValue(getApplicationContext(), Preferences.DRIVER_ID, jsonObject.getString("id"));
                        Preferences.setValue(getApplicationContext(), Preferences.DRIVER_AUTH_TOKEN, jsonObject.getString("v_token"));
                        Preferences.setValue(getApplicationContext(), Preferences.VEHICLE_ID, jsonObject.getString("vehicle_id"));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }

    private boolean validate() {

        if (et_email.getText().toString().equals("")){
            et_email.setError("Please enter email or mobie no.");
        }else  if(!Character.isAlphabetic(et_email.getText().toString().trim().charAt(0)))
        {
            if (isValidMobile(et_email.getText().toString().trim())) {
                if (et_password.getText().toString().equals("")) {
                    et_password.setError("Please enter password.");
                } else {
                    return true;
                }
            } else et_email.setError("Invalid mobile no.");

        }else {
            if (et_email.getText().toString().matches(Constant.emailPattern)) {
                if (et_password.getText().toString().equals("")) {
                    et_password.setError("Please enter password.");
                } else {
                    return true;
                }
            } else et_email.setError("Invalid email.");
        }

        /*if (et_email.getText().toString().equals("")) {
            et_email.setError("Please enter email or mobie no.");
        } else {
            if (et_password.getText().toString().equals("")) {
                et_password.setError("Please enter password.");
            } else {
                return true;
            }
        }*/
        return false;
    }
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void setLanguage(String mLanguage) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(mLanguage);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }


   /* Log.e("LOG_TAG","LATITUDE = " + String.valueOf(location.getLatitude()));
        Log.e("LOG_TAG","LONGATITUDE = "+ String.valueOf(location.getLongitude()));*/
}

