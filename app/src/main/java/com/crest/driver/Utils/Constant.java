package com.crest.driver.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by jasson on 27/6/16.
 */
public class Constant {
//    http://35.154.226.99:8081/api/api-list
//    http://192.168.0.222:3000/api/
    public static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //public static final String BASE_URL = "http://35.154.226.99:8081/api/";
    //public static final String BASE_URL = "http://35.154.123.76:8081/api/";
    //public static final String BASE_URL = "http://192.168.0.222:3000/api";

    public static final String BASE_URL = "http://35.154.230.244:8081/api/";

    public static final String URL_LOGIN = BASE_URL + "driverLogin";
    public static final String URL_LOGOUT = BASE_URL + "logout";
    public static final String URL_FORGOT_PASS = BASE_URL + "driverForgotPassword";
    public static final String URL_CHANGE_PASSWORD = BASE_URL + "driverPasswordUpdate";
    public static final String URL_RESET_PASS = BASE_URL + "driverResetPassword";
    public static final String URL_SINGUP = BASE_URL + "driverSignUp";
    public static final String URL_EDIT_PROFIE = BASE_URL + "driverProfileUpdate";
    public static final String URL_DRIVER_GET_PROFILE = BASE_URL + "driverProfileGet";
    public static final String URL_VEHICLE_TYPE = BASE_URL + "getVehicleTypes";
    public static final String URL_GET_DRIVER_RIDE_DETAIL = BASE_URL + "getDriverRideDetails";
    public static final String DRIVER_LOCATION_UPDATE = BASE_URL + "driverLocationUpdate";
    public static final String URL_GET_RIDE = BASE_URL + "getRide";
    public static final String START_RIDE = BASE_URL + "startRide";
    public static final String GET_DRIVER_WALLETE = BASE_URL + "getDriverWallet";
    public static final String RIDE_CHARGE_TYPE = BASE_URL +"rideChargeTypes";
    public static final String RIDE_ADD_CHARGE = BASE_URL+"rideAddCharge";
    public static final String RIDE_GET_CHARGES =  BASE_URL+"rideGetCharges";
    public static final String GET_DRIVER_EARNING = BASE_URL+"getDriverEarning";
    public static final String RIDE_COMPLETE = BASE_URL+"rideComplete";
    public static final String GET_RIDE = BASE_URL+"getRide";
    public static final String RATE_THIS_RIDE = BASE_URL+"rideRate";
    public static final String RIDE_PAYMENT = BASE_URL + "ridePayment";
    public static final String URL_TERMS_COND = BASE_URL + "getCms";
    public static final String VERIFY_ACCOUNT = BASE_URL + "verifyAccount";


    public static boolean CHECK_GPS = true;


    public static final String URL_BUZZ_ACTION = BASE_URL + "buzzAction";
    public static final String URL_GET_RIDE_CANCEL = BASE_URL + "getRideCancelReasons";
    public static final String URL_DRIVER_CANCEL_RIDE = BASE_URL + "cancelRide";
    public static final String URL_GET_LABEL = BASE_URL + "getLables";
    public static final String URL_DRIVER_DUTY_STATUS = BASE_URL + "driverDutyStatusGet";
    public static final String URL_DRIVER_DUTY_STATUS_UPDATE = BASE_URL + "driverDutyStatusUpdate";
    public static final String URL_DRIVER_GET_DASHBOARD = BASE_URL + "driverGetDashboard";
    public static final String URL_GET_DRIVER_RIDES= BASE_URL + "getDriverRides";

    public static boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(c, "No internet connection.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static final String IS_FIRS_TIME = "is_first_time";

    public static final String MY_EARNING_DETAIL = "my_earning_detail";
}

