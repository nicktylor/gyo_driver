package com.crest.driver.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    public static final String DRIVER_ID = "DRIVER_ID";
    public static final String DRIVER_AUTH_TOKEN = "DRIVER_AUTH_TOKEN";
    public static final String EMAIL_ID = "EMAIL_ID";
    public static final String VEHICLE_ID = "VEHICLE_ID";
    public static final String DRIVER_STATUS = "DRIVER_STATUS";
    public static final String EMAIL_ID_RESET_PASSWORD = "EMAIL_ID_RESET_PASSWORD";
    public static final String RIDE_ID = "id";
    public static final String CANCEL_REASON_ID = "CANCEL_REASON_ID";







































    public static final String LBL_LOGIN = "lbl_login";
    public static final String LBL_MOBILE_OR_EMAIL = "lbl_mobile_or_email";
    public static final String LBL_PASSWORD = "lbl_password";
    public static final String LBL_FORG_PASS = "lbl_forgot_password";
    public static final String LBL_OR = "lbl_or";
    public static final String LBL_SIGN_UP = "lbl_sign_up";
    public static final String LBL_FULL_NAME = "lbl_full_name";
    public static final String LBL_EMAIL_ADDR = "lbl_email_address";
    public static final String LBL_MOB_NO = "lbl_mobile_number";
    public static final String LBL_CONF_PASS = "lbl_confirm_password";
    public static final String LBL_SUBMIT = "lbl_submit";
    public static final String LBL_PASS_RECOVERY = "lbl_password_recovery";
    public static final String lbl_mobile_number_or_email = "lbl_mobile_number_or_email";
    public static final String LBL_RESET_PASS = "lbl_reset_password";
    public static final String LBL_AUTH_CODE = "lbl_authentication_code";
    public static final String LBL_NEW_PASS = "lbl_new_password";
    public static final String LBL_RETRY_NEW_PASS = "lbl_retry_new_password";
    public static final String LBL_EDIT_PROFILE = "lbl_edit_profile";
    public static final String LBL_FIRST_NAME = "lbl_first_name";
    public static final String LBL_EMAIL = "lbl_email";

    public static final String LBL_MOBILE = "lbl_mobile";
    public static final String LBL_SAVE = "lbl_save";
    public static final String LBL_CHANGE_PASS = "lbl_change_password";
    public static final String LBL_OLD_PASS = "lbl_old_password";
    public static final String LBL_ENTER_NEW_PASS = "lbl_enter_new_password";
    public static final String LBL_CONFIRM_NEW_PASS = "lbl_confirm_new_password";
    public static final String LBL_BOOK_RIDE = "lbl_book_a_ride";

    public static final String LBL_DESTI_LOCA = "lbl_enter_destination_location";
    public static final String LBL_RIDE_LATER = "lbl_ride_later";
    public static final String LBL_RIDE_NOW = "lbl_ride_now";
    public static final String LBL_LOGOUT = "lbl_logout";
    public static final String LBL_BOOK_YOUR_RIDE = "lbl_book_your_ride";
    public static final String LBL_MY_RIDES = "lbl_my_rides";
    public static final String LBL_TARIFF_CARD = "lbl_tariff_card";


    public static final String LBL_PROMO_CODE = "lbl_promotion_code";
    public static final String LBL_MY_WALLET = "lbl_my_wallet";
    public static final String LBL_NOTIFICATION = "lbl_notifications";
    public static final String LBL_FEEDBACK = "lbl_feedback";
    public static final String LBL_CONFIRM_BOOKING = "lbl_confirm_booking";
    public static final String LBL_VEHICLE_TYPE = "lbl_vehicle_type";

    public static final String LBL_HAVE_YOU_PROMO_CODE = "lbl_have_you_promotion_code";
    public static final String LBL_APPLI_SURCHARGE = "lbl_applicable_surcharge";
    public static final String LBL_BOOKING_SUCC = "lbl_booking_sucessful";
    public static final String LBL_CANCEL = "lbl_cancel";
    public static final String LBL_DONE = "lbl_done";
    public static final String LBL_PICKUP_ARRIVING = "lbl_pickup_arriving";
    public static final String LBL_YOUR_TRIP_CONF_PIN = "lbl_your_trip_confirmatin_pin";
    public static final String LBL_CANCEL_BOOKING = "lbl_cancel_booking";
    public static final String LBL_BOOKING_CANCEL = "lbl_booking_cancel";
    public static final String LBL_NO = "lbl_no";
    public static final String LBL_YES = "lbl_yes";
    public static final String LBL_REASON_FOR_CANCELLATION = "lbl_reason_for_cancellation";
    public static final String LBL_UNABLE_TO_CON_DRIVER = "lbl_unable_to_contact_driver";



    public static final String LBL_DRIVER_DENIED_DUTY = "lbl_driver_denied_duty";
    public static final String LBL_CAB_IS_NOT_MOVING_MY_DIRECTION = "lbl_cab_is_not_moving_in_my_direction";
    public static final String LBL_OTHER = "lbl_other";
    public static final String LBL_ENTER_REASON = "lbl_enter_reason";
    public static final String LBL_PROMOCODE = "lbl_promocode";
    public static final String LBL_ENTER_CODE_HERE = "lbl_enter_code_here";
    public static final String LBL_APPLY_CODE = "lbl_apply_code";
    public static final String LBL_SELECT_PAYMENT_TYPE = "lbl_select_payment_type";
    public static final String LBL_CASH = "lbl_cash";
    public static final String LBL_WALLET = "lbl_wallet";


    public static final String LBL_START_RIDING = "lbl_start_riding";
    public static final String LBL_STATUS = "lbl_status";
    public static final String LBL_MESSAGE = "lbl_message";
    public static final String LBL_COPY_TEXT = "lbl_copy_text";
    public static final String LBL_COMPLETE_RIDE = "lbl_complete_ride";
    public static final String LBL_FOR_RIDE_WITH_US = "lbl_for_ride_with_us";
    public static final String LBL_START_POINT = "lbl_start_point";
    public static final String LBL_DROP_POINT = "lbl_drop_point";
    public static final String LBL_PAYBLE_AMOUNT = "lbl_payable_amount";
    public static final String LBL_MAKE_PAYMENT = "lbl_make_payment";


    public static final String LBL_PAYMENT_DETAIL = "lbl_payment_detail";
    public static final String LBL_PROMOCODE_OFFER = "lbl_promocode_offer";
    public static final String LBL_PAYMENT_DONE_BY_WALLET = "lbl_payment_done_by_wallet";
    public static final String LBL_DUE_AMOUNT = "lbl_due_amount";
    public static final String LBL_RIDE_SUCCESSFUL = "lbl_ride_successful";
    public static final String LBL_RATE_THIS_RIDE = "lbl_rate_this_ride";
    public static final String LBL_WRITE_YOUR_COMMENT = "lbl_write_your_comment";
    public static final String LBL_RATE_NOW = "lbl_rate_now";
    public static final String LBL_SCHEDULE_NEW_RIDE = "lbl_schedule_new_ride";
    public static final String LBL_SCHEDULE_NOW = "lbl_schedule_now";

    public static final String LBL_SCHEDULE_RIDE_DETAILS = "lbl_schedule_ride_details";
    public static final String LBL_RIDE_DATE = "lbl_ride_date";
    public static final String LBL_RIDE_TIME = "lbl_ride_time";
    public static final String LBL_CAR = "lbl_car";
    public static final String LBL_CHANGE_MY_MIND = "lbl_changed_my_mind";
    public static final String LBL_COMPLETE = "lbl_complete";
    public static final String LBL_SCHEDULED = "lbl_scheduled";
    public static final String LBL_TOTAL_FARE = "lbl_total_fare";
    public static final String LBL_TOTAL_DISTANCE = "lbl_total_distance";
    public static final String LBL_START_TIME = "lbl_start_time";


    public static final String LBL_END_TIME = "lbl_end_time";
    public static final String LBL_TRIP_DURATION = "lbl_trip_duration";
    public static final String LBL_RATED_TRIP = "lbl_rated_trip";
    public static final String LBL_COMMENT_ABOUT_TRIP = "lbl_comment_about_trip";
    public static final String LBL_CANCEL_RIDE = "lbl_cancel_ride";
    public static final String LBL_REASON_TO_CANCEL = "lbl_reason_to_cancel";
    public static final String LBL_TERIFF_CARD = "lbl_teriff_card";
    public static final String LBL_CHOOSE_CITY = "lbl_choose_city";
    public static final String LBL_CHOOSE_CATEGORY = "lbl_choose_category";
    public static final String LBL_FLATE_RATE = "lbl_flat_rate";

    public static final String LBL_STANDARD_FARE = "lbl_standard_fare";
    public static final String LBL_BASE_FARE = "lbl_base_fare";
    public static final String LBL_COPY_CODE = "lbl_copy_code";
    public static final String LBL_REFERRAL_CODE = "lbl_referral_code";
    public static final String LBL_INVITE_FRIENDS = "lbl_invite_friends";
    public static final String LBL_FACEBOOK = "lbl_facebook";
    public static final String LBL_TWITTER = "lbl_twitter";
    public static final String LBL_RATE_APP = "lbl_rate_app";
    public static final String LBL_GIVE_FEEDBACK_ABOUT_APP = "lbl_give_feedback_about_app";
    public static final String LBL_ADD_MONEY = "lbl_add_money";



    public static final String LBL_ADD_MONEY_VALUE = "lbl_add_money_value";
    public static final String LBL_SELECT_PAYMENT_MODE = "lbl_select_payment_mode";
    public static final String LBL_CARD_NUMBER = "lbl_card_number";
    public static final String LBL_EXPIRY_DATE = "lbl_expiry_date";
    public static final String LBL_CVV = "lbl_cvv";
    public static final String LBL_VEHICAL_NUMBER = "lbl_vehicle_number";
    public static final String LBL_UPLOAD_RC_BOOK_PHOTO = "lbl_upload_rc_book_photo";
    public static final String LBL_UPLOAD_PUC_PHOTO = "lbl_upload_puc_photo";
    public static final String LBL_UPLOAD_VEHI_INSU_PHOTO = "lbl_upload_vehicle_insrance_photo";
    public static final String LBL_RIDE_REQUEST = "lbl_ride_request";


    public static final String LBL_CUSTOMER_PICKUP_ADDR = "lbl_customer_pickup_address";
    public static final String LBL_DENIED = "lbl_denied";
    public static final String LBL_ACCEPT = "lbl_accept";
    public static final String LBL_MY_DRY_RUN = "lbl_my_dry_run";
    public static final String LBL_CUSTOMER_LOCA = "lbl_customer_location";
    public static final String LBL_CUS_NUM = "lbl_customer_number";
    public static final String LBL_START_RIDE = "lbl_start_ride";
    public static final String LBL_CONFI_PIN = "lbl_confirmation_pin";
    public static final String LBL_ENTER_CONF_PIN = "lbl_enter_confirmation_pin";
    public static final String LBL_APPLY_PIN = "lbl_apply_pin";


    public static final String LBL_PAY_ANY_EXTRA_AMOUNT = "lbl_pay_any_extra_amount";
    public static final String LBL_ADD_AMOUNT = "lbl_add_amount";
    public static final String LBL_ADD_EXTRA_AMOUNT = "lbl_add_extra_amount";
    public static final String LBL_SELECT_METHOD = "lbl_select_method";
    public static final String LBL_ENTER_AMOUNT = "lbl_enter_amount";
    public static final String LBL_TOLL = "lbl_toll";
    public static final String LBL_CAR_PARKING = "lbl_car_parking";
    public static final String LBL_TOTAL = "lbl_total";
    public static final String LBL_TRIP_FARE = "lbl_trip_fare";
    public static final String LBL_TOLLS = "lbl_tolls";

    public static final String LBL_PARKING_CHARGES = "lbl_parking_charges";
    public static final String LBL_SERVICE_TAX = "lbl_service_tax";
    public static final String LBL_CONFIRM_PAYMENT = "lbl_confirm_payment";
    public static final String LBL_PAY_COMPLETE = "lbl_payment_complete";
    public static final String LBL_PAY_RECEIVED = "lbl_payment_received";
    public static final String LBL_BY_WALLET = "lbl_by_wallet";
    public static final String LBL_DASHBOARD = "lbl_dashboard";
    public static final String LBL_MY_EARNING = "lbl_my_earning";
    public static final String LBL_BEST_SARATHI = "lbl_best_sarathi";
    public static final String LBL_TODAY = "lbl_today";


    public static final String LBL_WEEKLY = "lbl_weekly";
    public static final String LBL_MONTHLY = "lbl_monthly";
    public static final String LBL_AVAILABLE = "lbl_available";
    public static final String LBL_NOT_AVAILABLE = "lbl_not_available";
    public static final String LBL_SUCCESS = "lbl_success";
    public static final String LBL_THIS_WEEK_TRIP = "lbl_this_weeks_trip";
    public static final String LBL_RIDE_DEATAILS = "lbl_ride_details";
    public static final String LBL_DROP_DURATION = "lbl_drop_duration";


    public static final String ERROR = "error";
    public static final String ERROR_FILE_UPLOAD = "error_file_upload";
    public static final String ERR_REQ_NAME = "err_req_name";
    public static final String ERR_REQ_EMAIL = "err_req_email";
    public static final String ERR_REQ_PHONE = "err_req_phone";
    public static final String ERR_REQ_EMAIL_OR_PHONE = "err_req_email_or_phone";
    public static final String ERR_REQ_PASS = "err_req_password";
    public static final String ERR_REQ_OTP = "err_req_otp";
    public static final String ERR_REQ_OLD_PASS = "err_req_old_password";
    public static final String ERR_REQ_DEVICE_TOKEN = "err_req_device_token";


    public static final String ERR_REQ_USER_ID = "err_req_user_id";
    public static final String ERR_REQ_ID = "err_req_id";
    public static final String ERR_REQ_LOGIN_ID = "err_req_login_id";
    public static final String ERR_REQ_AUTH_TOKEN = "err_req_auth_token";
    public static final String ERR_REQ_VEHICLE_TYPE = "err_req_vehicle_type";
    public static final String ERR_REQ_VEHICLE_NUM = "err_req_vehicle_number";
    public static final String ERR_REQ_PROFILE_IMAGE = "err_req_profile_image";
    public static final String ERR_REQ_RC_BOOK_IMAGE = "err_req_rc_book_image";
    public static final String ERR_REQ_PUC_IMAGE = "err_req_puc_image";
    public static final String ERR_REQ_INSU_IMAGE = "err_req_insurance_image";


    public static final String ERR_REQ_LATITUDE = "err_req_latitude";
    public static final String ERR_REQ_LONGITUDE = "err_req_longitude";
    public static final String ERR_REQ_LANG = "err_req_lang";
    public static final String ERR_REQ_TYPE = "err_req_type";
    public static final String ERR_REQ_DRIVER_ID = "err_req_driver_id";
    public static final String ERR_REQ_BUZZ_ID = "err_req_buzz_id";
    public static final String ERR_REQ_RIDE_ID = "err_req_ride_id";
    public static final String ERR_REQ_ACTION = "err_req_action";
    public static final String ERR_REQ_COMMENT = "err_req_comment";
    public static final String ERR_REQ_RIDE_CANCEL_REASON = "err_req_ride_cancel_reason";


    public static final String ERR_REQ_vehicle_id = "err_req_vehicle_id";
    public static final String ERR_REQ_PIN = "err_req_pin";
    public static final String ERR_MSG_NO_RIDE_FOUND = "err_msg_no_ride_found";
    public static final String ERR_REQ_ADDR = "err_req_address";
    public static final String ERR_REQ_ESTIMATE_AMOUNT = "err_req_estimate_amount";
    public static final String ERR_REQ_ESTIMATE_KM = "err_req_estimate_km";
    public static final String ERR_REQ_SORT_BY = "err_req_sort_by";
    public static final String ERR_REQ_STATUS = "err_req_status";
    public static final String ERR_VALID_PASS = "err_validation_password";
    public static final String ERR_INVALID_OLD_PASS = "err_invalid_old_password";

    public static final String ERR_INVALID_EMAIL = "err_invalid_email";
    public static final String ERR_INVALID_EMAIL_OR_PHONE = "err_invalid_email_or_phone";
    public static final String ERR_INVALID_AUTH_TOKEN = "err_invalid_auth_token";
    public static final String ERR_INVALID_PASS = "err_invalid_password";
    public static final String ERR_INVALID_OTP = "err_invalid_otp";
    public static final String ERR_INVALID_STATUS = "err_invalid_status";
    public static final String ERR_MSG_INVALID_PIN = "err_msg_invalid_pin";
    public static final String ERR_NO_RECORDS = "err_no_records";
    public static final String ERR_MSG_NO_ACCOUNT = "err_msg_no_account";
    public static final String ERR_MSG_NOT_LOOGED_IN = "err_msg_not_logged_in";

    public static final String ERR_MSG_EXISTS_EMAIL = "err_msg_exists_email";
    public static final String ERR_MSG_EXISTS_PHONE = "err_msg_exists_phone";
    public static final String SUCC_LOGIN_SUCC = "succ_login_successfully";
    public static final String SUCC_LOGOUT_SUCC = "succ_logout_successfully";
    public static final String SUCC_REG_SUCC = "succ_register_successfully";
    public static final String SUCC_PASS_UPDATE = "succ_password_updated";
    public static final String SUCC_STATUS_UPDATED = "succ_status_updated";
    public static final String SUCC_PROFILE_UPDATED = "succ_profile_updated";
    public static final String SUCC_OTP_SENT = "succ_otp_sent";
    public static final String SUCC_MSG_RIDE_STARTED = "succ_msg_ride_started";


    public static final String SUCC_MSG_RIDE_CANCELLED = "succ_msg_ride_cancelled";
    public static final String SUCC_RIDE_RATE_SUCC= "succ_ride_rate_successfully";
    public static final String SUCC_FEEDBACK_SUCC = "succ_feedback_successfully";
    public static final String DRIVER_NOTIF_RIDE_ID = "DRIVER_NOTIF_RIDE_ID";
    public static final String DRIVER_NOTIF_PICKUP_ADDRESS = "DRIVER_NOTIF_PICKUP_ADDRESS";
    public static final String DRIVER_NOTIF_BUZZ_TIME = "DRIVER_NOTIF_BUZZ_TIME";
    public static final String DRIVER_NOTIF_USER_ID ="DRIVER_NOTIF_USER_ID" ;
    public static final String DRIVER_COME_FROM = "DRIVER_COME_FROM";


    public static void setValue(Context context, String Key, String Value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Key, Value);
        editor.commit();
    }

    public static String getValue(Context context, String Key) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(Key, "");
    }
}