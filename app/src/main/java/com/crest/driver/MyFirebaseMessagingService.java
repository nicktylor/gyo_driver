package com.crest.driver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.crest.driver.MainActivity;
import com.crest.driver.R;
import com.crest.driver.Utils.Preferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static String notifData;
    String ride_id,pickup_address,buzz_time,i_user_id,i_buzz_id,mType = null,i_round_id;

    public static final String MESSAGE_SUCCESS = "MessageSuccess";
    public static final String MESSAGE_SUCCESS_MAPS = "MessageSuccessMapss";
    public static final String MESAGE_ERROR = "MessageError";
    public static final String MESSAGE_NOTIFICATION = "MessageNotification";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        ride_id = remoteMessage.getData().get("i_ride_id");
        pickup_address = remoteMessage.getData().get("pickup_address");
        buzz_time = remoteMessage.getData().get("buzz_time");
        i_user_id = remoteMessage.getData().get("i_user_id");
        i_buzz_id = remoteMessage.getData().get("i_buzz_id");
        i_round_id = remoteMessage.getData().get("i_round_id");
        mType = remoteMessage.getData().get("type");

        Log.e("TAG",remoteMessage.getData().toString());

        Log.e("TAG","buzz_time"+buzz_time);
        Log.e("TAG","i_round_id"+i_round_id);
        Log.e("TAG","mType = "+mType);
        if(mType != null){
            if(mType.equalsIgnoreCase("driver_ride_other_assign")){
                Log.e("TAG","mType = "+mType);
                SendMessageToDeitician(mType);
                return;
            }else if(mType.equalsIgnoreCase("driver_ride_cancel")){
                rideCancleRequest();
                sendNotification("");
            }else if(mType.equalsIgnoreCase("driver_ride_buzz")){
                SendMessageNotification();
                sendNotification("");
            }
        }


//      Preferences.setValue(getApplicationContext(),Preferences.DRIVER_COME_FROM,"NOTIF");

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        Log.d(TAG, "data: " + remoteMessage.getData());
        Log.d(TAG, "data:.... " + remoteMessage.getData().get("i_ride_id"));
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendNotification(String notoficationType) {

        Intent intent;

        if(notoficationType.equalsIgnoreCase("driver_ride_cancel")){
            intent = new Intent(this, MapsActivity.class);
        }else {
            intent = new Intent(this, MainActivity.class);
        }

        intent.putExtra("pickup_address",pickup_address);
        intent.putExtra("buzz_time", buzz_time);
        intent.putExtra("i_ride_id",ride_id);
        intent.putExtra("i_user_id",i_user_id);
        intent.putExtra("i_buzz_id",i_buzz_id);
        intent.putExtra("i_round_id",i_round_id);
        intent.putExtra("type",mType);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0/* Request code */, intent,
                0);

        Notification noti = new Notification.Builder(this)
                .setContentTitle("Ride request")
                .setContentText(buzz_time + "sec")
                .setSmallIcon(R.drawable.ic_taxi)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                //.setLargeIcon(aBitmap)
                .build();

        noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;

        /*NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_taxi)
                .setContentTitle("Ride request")
                .setContentText(buzz_time + "sec")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);*/

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(5, noti);
    }

    private void SendMessageToDeitician(String Message) {
        //Registration complete intent initially null
        Intent registrationComplete = null;
        try {
            //You can also extend the app by storing the token in to your server
            Log.e("Chate Message", "message:" + Message);
            //on registration complete creating intent with success
            registrationComplete = new Intent(MESSAGE_SUCCESS);
            //Putting the token to the intent
            registrationComplete.putExtra("message", Message);

        } catch (Exception e) {
            //If any error occurred
            Log.e("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(MESAGE_ERROR);
        }
        //Sending the broadcast that registration is completed
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void rideCancleRequest() {
        //Registration complete intent initially null
        Intent registrationComplete = null;
        try {
            registrationComplete = new Intent(MESSAGE_SUCCESS_MAPS);
        } catch (Exception e) {
            registrationComplete = new Intent(MESAGE_ERROR);
        }
        //Sending the broadcast that registration is completed
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void SendMessageNotification() {
        //Registration complete intent initially null
        Intent registrationComplete = null;
        try {

            Log.e("TAG","Notification buzz_time="+buzz_time);
            registrationComplete = new Intent(MESSAGE_NOTIFICATION)
            .putExtra("pickup_address",pickup_address)
            .putExtra("buzz_time", buzz_time)
            .putExtra("i_ride_id",ride_id)
            .putExtra("i_user_id",i_user_id)
            .putExtra("i_buzz_id",i_buzz_id)
            .putExtra("i_round_id",i_round_id)
            .putExtra("type",mType);
            Log.e("TAG","mType1 = "+mType);

        } catch (Exception e) {
            //If any error occurred
            Log.e("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(MESAGE_ERROR);
        }
        //Sending the broadcast that registration is completed
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}
