package com.crest.driver;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyTAG;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;


    String mAddType;
    String mBuzzTime;
    String mRideId;
    String mUserId;
    String mBussId;
    String mRoundId;
    String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        if(getIntent().getExtras() != null){
             mAddType = getIntent().getExtras().getString("pickup_address","");
             mBuzzTime = getIntent().getExtras().getString("buzz_time","");
             mRideId = getIntent().getExtras().getString("i_ride_id","");
             mUserId = getIntent().getExtras().getString("i_user_id","");
            mBussId = getIntent().getExtras().getString("i_buzz_id","");
            mRoundId = getIntent().getExtras().getString("i_round_id","");
            mType = getIntent().getExtras().getString("type","");

        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!Preferences.getValue(getApplicationContext(),Preferences.DRIVER_ID).isEmpty()){
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    intent.putExtra("pickup_address",mAddType);
                    intent.putExtra("buzz_time", mBuzzTime);
                    intent.putExtra("i_ride_id",mRideId);
                    intent.putExtra("i_user_id",mUserId);
                    intent.putExtra("i_buzz_id",mBussId);
                    intent.putExtra("i_round_id",mRideId);
                    intent.putExtra("type",mType);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);


//        if (Constant.isOnline(getApplicationContext())){
//            getLables_api();
//        }
    }

    private void getLables_api() {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_GET_LABEL).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "");
        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();

        VolleyRequestClass.allRequest(Splash.this, newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    int responce_status = response.getInt(VolleyTAG.status);
                    String message = response.getString(VolleyTAG.message);
                    if (responce_status == VolleyTAG.response_status) {
                        JSONObject lbl_obj = response.getJSONObject("data");

                        Preferences.setValue(getApplicationContext(), Preferences.LBL_LOGIN, lbl_obj.getString("lbl_login"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_MOBILE_OR_EMAIL, lbl_obj.getString("lbl_mobile_or_email"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_PASSWORD, lbl_obj.getString("lbl_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_FORG_PASS, lbl_obj.getString("lbl_forgot_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_OR, lbl_obj.getString("lbl_or"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_SIGN_UP, lbl_obj.getString("lbl_sign_up"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_FULL_NAME, lbl_obj.getString("lbl_full_name"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_EMAIL_ADDR, lbl_obj.getString("lbl_email_address"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_MOB_NO, lbl_obj.getString("lbl_mobile_number"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CONF_PASS, lbl_obj.getString("lbl_confirm_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_SUBMIT, lbl_obj.getString("lbl_submit"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_PASS_RECOVERY, lbl_obj.getString("lbl_password_recovery"));
                        Preferences.setValue(getApplicationContext(), Preferences.lbl_mobile_number_or_email, lbl_obj.getString("lbl_mobile_number_or_email"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_RESET_PASS, lbl_obj.getString("lbl_reset_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_AUTH_CODE, lbl_obj.getString("lbl_authentication_code"));

                        Preferences.setValue(getApplicationContext(), Preferences.LBL_NEW_PASS, lbl_obj.getString("lbl_new_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_RETRY_NEW_PASS, lbl_obj.getString("lbl_retry_new_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_EDIT_PROFILE, lbl_obj.getString("lbl_edit_profile"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_FIRST_NAME, lbl_obj.getString("lbl_first_name"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_EMAIL, lbl_obj.getString("lbl_email"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_MOBILE, lbl_obj.getString("lbl_mobile"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_SAVE, lbl_obj.getString("lbl_save"));

                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CHANGE_PASS, lbl_obj.getString("lbl_change_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_OLD_PASS, lbl_obj.getString("lbl_old_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_ENTER_NEW_PASS, lbl_obj.getString("lbl_enter_new_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CONFIRM_NEW_PASS, lbl_obj.getString("lbl_confirm_new_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_BOOK_RIDE, lbl_obj.getString("lbl_book_a_ride"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_DESTI_LOCA, lbl_obj.getString("lbl_enter_destination_location"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_RIDE_LATER, lbl_obj.getString("lbl_ride_later"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_RIDE_NOW, lbl_obj.getString("lbl_ride_now"));


                        Preferences.setValue(getApplicationContext(), Preferences.LBL_LOGOUT, lbl_obj.getString("lbl_logout"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_BOOK_YOUR_RIDE, lbl_obj.getString("lbl_book_your_ride"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_MY_RIDES, lbl_obj.getString("lbl_my_rides"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_TARIFF_CARD, lbl_obj.getString("lbl_tariff_card"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_PROMO_CODE, lbl_obj.getString("lbl_promotion_code"));

                        Preferences.setValue(getApplicationContext(), Preferences.LBL_MY_WALLET, lbl_obj.getString("lbl_my_wallet"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_NOTIFICATION, lbl_obj.getString("lbl_notifications"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_FEEDBACK, lbl_obj.getString("lbl_feedback"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CONFIRM_BOOKING, lbl_obj.getString("lbl_confirm_booking"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_VEHICLE_TYPE, lbl_obj.getString("lbl_vehicle_type"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_HAVE_YOU_PROMO_CODE, lbl_obj.getString("lbl_have_you_promotion_code"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_APPLI_SURCHARGE, lbl_obj.getString("lbl_applicable_surcharge"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_BOOKING_SUCC, lbl_obj.getString("lbl_booking_sucessful"));

                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CANCEL, lbl_obj.getString("lbl_cancel"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_DONE, lbl_obj.getString("lbl_done"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_PICKUP_ARRIVING, lbl_obj.getString("lbl_pickup_arriving"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_YOUR_TRIP_CONF_PIN, lbl_obj.getString("lbl_your_trip_confirmatin_pin"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CANCEL_BOOKING, lbl_obj.getString("lbl_cancel_booking"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_BOOKING_CANCEL, lbl_obj.getString("lbl_booking_cancel"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_NO, lbl_obj.getString("lbl_no"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_YES, lbl_obj.getString("lbl_yes"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_REASON_FOR_CANCELLATION, lbl_obj.getString("lbl_reason_for_cancellation"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_UNABLE_TO_CON_DRIVER, lbl_obj.getString("lbl_unable_to_contact_driver"));


                        Preferences.setValue(getApplicationContext(), Preferences.LBL_DRIVER_DENIED_DUTY, lbl_obj.getString("lbl_driver_denied_duty"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CAB_IS_NOT_MOVING_MY_DIRECTION, lbl_obj.getString("lbl_cab_is_not_moving_in_my_direction"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_OTHER, lbl_obj.getString("lbl_other"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_ENTER_REASON, lbl_obj.getString("lbl_enter_reason"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_PROMOCODE, lbl_obj.getString("lbl_promocode"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_ENTER_CODE_HERE, lbl_obj.getString("lbl_enter_code_here"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_APPLY_CODE, lbl_obj.getString("lbl_apply_code"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_SELECT_PAYMENT_TYPE, lbl_obj.getString("lbl_select_payment_type"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CASH, lbl_obj.getString("lbl_cash"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_WALLET, lbl_obj.getString("lbl_wallet"));


                        Preferences.setValue(getApplicationContext(), Preferences.LBL_START_RIDING, lbl_obj.getString("lbl_start_riding"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_STATUS, lbl_obj.getString("lbl_status"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_MESSAGE, lbl_obj.getString("lbl_message"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_COPY_TEXT, lbl_obj.getString("lbl_copy_text"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_COMPLETE_RIDE, lbl_obj.getString("lbl_complete_ride"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_FOR_RIDE_WITH_US, lbl_obj.getString("lbl_for_ride_with_us"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_START_POINT, lbl_obj.getString("lbl_start_point"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_DROP_POINT, lbl_obj.getString("lbl_drop_point"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_PAYBLE_AMOUNT, lbl_obj.getString("lbl_payable_amount"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_MAKE_PAYMENT, lbl_obj.getString("lbl_make_payment"));


                        Preferences.setValue(getApplicationContext(), Preferences.LBL_PAYMENT_DETAIL, lbl_obj.getString("lbl_payment_detail"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_PROMOCODE_OFFER, lbl_obj.getString("lbl_promocode_offer"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_PAYMENT_DONE_BY_WALLET, lbl_obj.getString("lbl_payment_done_by_wallet"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_DUE_AMOUNT, lbl_obj.getString("lbl_due_amount"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_RIDE_SUCCESSFUL, lbl_obj.getString("lbl_ride_successful"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_RATE_THIS_RIDE, lbl_obj.getString("lbl_rate_this_ride"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_WRITE_YOUR_COMMENT, lbl_obj.getString("lbl_write_your_comment"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_RATE_NOW, lbl_obj.getString("lbl_rate_now"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_SCHEDULE_NEW_RIDE, lbl_obj.getString("lbl_schedule_new_ride"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_SCHEDULE_NOW, lbl_obj.getString("lbl_schedule_now"));


                        Preferences.setValue(getApplicationContext(), Preferences.LBL_SCHEDULE_RIDE_DETAILS, lbl_obj.getString("lbl_schedule_ride_details"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_RIDE_DATE, lbl_obj.getString("lbl_ride_date"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_RIDE_TIME, lbl_obj.getString("lbl_ride_time"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CAR, lbl_obj.getString("lbl_car"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CHANGE_MY_MIND, lbl_obj.getString("lbl_changed_my_mind"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_COMPLETE, lbl_obj.getString("lbl_complete"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_SCHEDULED, lbl_obj.getString("lbl_scheduled"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_TOTAL_FARE, lbl_obj.getString("lbl_total_fare"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_TOTAL_DISTANCE, lbl_obj.getString("lbl_total_distance"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_START_TIME, lbl_obj.getString("lbl_start_time"));

                        Preferences.setValue(getApplicationContext(), Preferences.LBL_END_TIME, lbl_obj.getString("lbl_end_time"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_TRIP_DURATION, lbl_obj.getString("lbl_trip_duration"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_RATED_TRIP, lbl_obj.getString("lbl_rated_trip"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_COMMENT_ABOUT_TRIP, lbl_obj.getString("lbl_comment_about_trip"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CANCEL_RIDE, lbl_obj.getString("lbl_cancel_ride"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_REASON_TO_CANCEL, lbl_obj.getString("lbl_reason_to_cancel"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_TERIFF_CARD, lbl_obj.getString("lbl_teriff_card"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CHOOSE_CITY, lbl_obj.getString("lbl_choose_city"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CHOOSE_CATEGORY, lbl_obj.getString("lbl_choose_category"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_FLATE_RATE, lbl_obj.getString("lbl_flat_rate"));

                        Preferences.setValue(getApplicationContext(), Preferences.LBL_STANDARD_FARE, lbl_obj.getString("lbl_standard_fare"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_BASE_FARE, lbl_obj.getString("lbl_base_fare"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_COPY_CODE, lbl_obj.getString("lbl_copy_code"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_REFERRAL_CODE, lbl_obj.getString("lbl_referral_code"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_INVITE_FRIENDS, lbl_obj.getString("lbl_invite_friends"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_FACEBOOK, lbl_obj.getString("lbl_facebook"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_TWITTER, lbl_obj.getString("lbl_twitter"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_RATE_APP, lbl_obj.getString("lbl_rate_app"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_GIVE_FEEDBACK_ABOUT_APP, lbl_obj.getString("lbl_give_feedback_about_app"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_ADD_MONEY, lbl_obj.getString("lbl_add_money"));


                        Preferences.setValue(getApplicationContext(), Preferences.LBL_ADD_MONEY_VALUE, lbl_obj.getString("lbl_add_money_value"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_SELECT_PAYMENT_MODE, lbl_obj.getString("lbl_select_payment_mode"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CARD_NUMBER, lbl_obj.getString("lbl_card_number"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_EXPIRY_DATE, lbl_obj.getString("lbl_expiry_date"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CVV, lbl_obj.getString("lbl_cvv"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_VEHICAL_NUMBER, lbl_obj.getString("lbl_vehicle_number"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_UPLOAD_RC_BOOK_PHOTO, lbl_obj.getString("lbl_upload_rc_book_photo"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_UPLOAD_PUC_PHOTO, lbl_obj.getString("lbl_upload_puc_photo"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_UPLOAD_VEHI_INSU_PHOTO, lbl_obj.getString("lbl_upload_vehicle_insrance_photo"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_RIDE_REQUEST, lbl_obj.getString("lbl_ride_request"));


                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CUSTOMER_PICKUP_ADDR, lbl_obj.getString("lbl_customer_pickup_address"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_DENIED, lbl_obj.getString("lbl_denied"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_ACCEPT, lbl_obj.getString("lbl_accept"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_MY_DRY_RUN, lbl_obj.getString("lbl_my_dry_run"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CUSTOMER_LOCA, lbl_obj.getString("lbl_customer_location"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CUS_NUM, lbl_obj.getString("lbl_customer_number"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_START_RIDE, lbl_obj.getString("lbl_start_ride"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CONFI_PIN, lbl_obj.getString("lbl_confirmation_pin"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_ENTER_CONF_PIN, lbl_obj.getString("lbl_enter_confirmation_pin"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_APPLY_PIN, lbl_obj.getString("lbl_apply_pin"));

                        Preferences.setValue(getApplicationContext(), Preferences.LBL_PAY_ANY_EXTRA_AMOUNT, lbl_obj.getString("lbl_pay_any_extra_amount"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_ADD_AMOUNT, lbl_obj.getString("lbl_add_amount"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_ADD_EXTRA_AMOUNT, lbl_obj.getString("lbl_add_extra_amount"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_SELECT_METHOD, lbl_obj.getString("lbl_select_method"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_ENTER_AMOUNT, lbl_obj.getString("lbl_enter_amount"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_TOLL, lbl_obj.getString("lbl_toll"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CAR_PARKING, lbl_obj.getString("lbl_car_parking"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_TOTAL, lbl_obj.getString("lbl_total"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_TRIP_FARE, lbl_obj.getString("lbl_trip_fare"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_TOLLS, lbl_obj.getString("lbl_tolls"));


                        Preferences.setValue(getApplicationContext(), Preferences.LBL_PARKING_CHARGES, lbl_obj.getString("lbl_parking_charges"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_SERVICE_TAX, lbl_obj.getString("lbl_service_tax"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_CONFIRM_PAYMENT, lbl_obj.getString("lbl_confirm_payment"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_PAY_COMPLETE, lbl_obj.getString("lbl_payment_complete"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_PAY_RECEIVED, lbl_obj.getString("lbl_payment_received"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_BY_WALLET, lbl_obj.getString("lbl_by_wallet"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_DASHBOARD, lbl_obj.getString("lbl_dashboard"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_MY_EARNING, lbl_obj.getString("lbl_my_earning"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_BEST_SARATHI, lbl_obj.getString("lbl_best_sarathi"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_TODAY, lbl_obj.getString("lbl_today"));

                        Preferences.setValue(getApplicationContext(), Preferences.LBL_WEEKLY, lbl_obj.getString("lbl_weekly"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_MONTHLY, lbl_obj.getString("lbl_monthly"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_AVAILABLE, lbl_obj.getString("lbl_available"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_NOT_AVAILABLE, lbl_obj.getString("lbl_not_available"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_SUCCESS, lbl_obj.getString("lbl_success"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_THIS_WEEK_TRIP, lbl_obj.getString("lbl_this_weeks_trip"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_RIDE_DEATAILS, lbl_obj.getString("lbl_ride_details"));
                        Preferences.setValue(getApplicationContext(), Preferences.LBL_DROP_DURATION, lbl_obj.getString("lbl_drop_duration"));


                        Preferences.setValue(getApplicationContext(), Preferences.ERROR, lbl_obj.getString("error"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERROR_FILE_UPLOAD, lbl_obj.getString("error_file_upload"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_NAME, lbl_obj.getString("err_req_name"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_EMAIL, lbl_obj.getString("err_req_email"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_PHONE, lbl_obj.getString("err_req_phone"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_EMAIL_OR_PHONE, lbl_obj.getString("err_req_email_or_phone"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_PASS, lbl_obj.getString("err_req_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_OTP, lbl_obj.getString("err_req_otp"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_OLD_PASS, lbl_obj.getString("err_req_old_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_DEVICE_TOKEN, lbl_obj.getString("err_req_device_token"));


                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_USER_ID, lbl_obj.getString("err_req_user_id"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_ID, lbl_obj.getString("err_req_id"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_LOGIN_ID, lbl_obj.getString("err_req_login_id"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_AUTH_TOKEN, lbl_obj.getString("err_req_auth_token"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_VEHICLE_TYPE, lbl_obj.getString("err_req_vehicle_type"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_VEHICLE_NUM, lbl_obj.getString("err_req_vehicle_number"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_PROFILE_IMAGE, lbl_obj.getString("err_req_profile_image"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_RC_BOOK_IMAGE, lbl_obj.getString("err_req_rc_book_image"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_PUC_IMAGE, lbl_obj.getString("err_req_puc_image"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_INSU_IMAGE, lbl_obj.getString("err_req_insurance_image"));


                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_LATITUDE, lbl_obj.getString("err_req_latitude"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_LONGITUDE, lbl_obj.getString("err_req_longitude"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_LANG, lbl_obj.getString("err_req_lang"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_TYPE, lbl_obj.getString("err_req_type"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_DRIVER_ID, lbl_obj.getString("err_req_driver_id"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_BUZZ_ID, lbl_obj.getString("err_req_buzz_id"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_RIDE_ID, lbl_obj.getString("err_req_ride_id"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_ACTION, lbl_obj.getString("err_req_action"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_COMMENT, lbl_obj.getString("err_req_comment"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_RIDE_CANCEL_REASON, lbl_obj.getString("err_req_ride_cancel_reason"));


                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_vehicle_id, lbl_obj.getString("err_req_vehicle_id"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_PIN, lbl_obj.getString("err_req_pin"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_MSG_NO_RIDE_FOUND, lbl_obj.getString("err_msg_no_ride_found"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_ADDR, lbl_obj.getString("err_req_address"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_ESTIMATE_AMOUNT, lbl_obj.getString("err_req_estimate_amount"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_ESTIMATE_KM, lbl_obj.getString("err_req_estimate_km"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_SORT_BY, lbl_obj.getString("err_req_sort_by"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_REQ_STATUS, lbl_obj.getString("err_req_status"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_VALID_PASS, lbl_obj.getString("err_validation_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_INVALID_OLD_PASS, lbl_obj.getString("err_invalid_old_password"));


                        Preferences.setValue(getApplicationContext(), Preferences.ERR_INVALID_EMAIL, lbl_obj.getString("err_invalid_email"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_INVALID_EMAIL_OR_PHONE, lbl_obj.getString("err_invalid_email_or_phone"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_INVALID_AUTH_TOKEN, lbl_obj.getString("err_invalid_auth_token"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_INVALID_PASS, lbl_obj.getString("err_invalid_password"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_INVALID_OTP, lbl_obj.getString("err_invalid_otp"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_INVALID_STATUS, lbl_obj.getString("err_invalid_status"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_MSG_INVALID_PIN, lbl_obj.getString("err_msg_invalid_pin"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_NO_RECORDS, lbl_obj.getString("err_no_records"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_MSG_NO_ACCOUNT, lbl_obj.getString("err_msg_no_account"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_MSG_NOT_LOOGED_IN, lbl_obj.getString("err_msg_not_logged_in"));


                        Preferences.setValue(getApplicationContext(), Preferences.ERR_MSG_EXISTS_EMAIL, lbl_obj.getString("err_msg_exists_email"));
                        Preferences.setValue(getApplicationContext(), Preferences.ERR_MSG_EXISTS_PHONE, lbl_obj.getString("err_msg_exists_phone"));
                        Preferences.setValue(getApplicationContext(), Preferences.SUCC_LOGIN_SUCC, lbl_obj.getString("succ_login_successfully"));
                        Preferences.setValue(getApplicationContext(), Preferences.SUCC_LOGOUT_SUCC, lbl_obj.getString("succ_logout_successfully"));
                        Preferences.setValue(getApplicationContext(), Preferences.SUCC_REG_SUCC, lbl_obj.getString("succ_register_successfully"));
                        Preferences.setValue(getApplicationContext(), Preferences.SUCC_PASS_UPDATE, lbl_obj.getString("succ_password_updated"));
                        Preferences.setValue(getApplicationContext(), Preferences.SUCC_STATUS_UPDATED, lbl_obj.getString("succ_status_updated"));
                        Preferences.setValue(getApplicationContext(), Preferences.SUCC_PROFILE_UPDATED, lbl_obj.getString("succ_profile_updated"));
                        Preferences.setValue(getApplicationContext(), Preferences.SUCC_OTP_SENT, lbl_obj.getString("succ_otp_sent"));
                        Preferences.setValue(getApplicationContext(), Preferences.SUCC_MSG_RIDE_STARTED, lbl_obj.getString("succ_msg_ride_started"));


                        Preferences.setValue(getApplicationContext(), Preferences.SUCC_MSG_RIDE_CANCELLED, lbl_obj.getString("succ_msg_ride_cancelled"));
                        Preferences.setValue(getApplicationContext(), Preferences.SUCC_RIDE_RATE_SUCC, lbl_obj.getString("succ_ride_rate_successfully"));
                        Preferences.setValue(getApplicationContext(), Preferences.SUCC_FEEDBACK_SUCC, lbl_obj.getString("succ_feedback_successfully"));

                    } else {
                        Toast.makeText(Splash.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }
}
