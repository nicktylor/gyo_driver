package com.crest.driver;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crest.driver.Multipart.MultipartRequest;
import com.crest.driver.Multipart.MultipartResponce;
import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.FileUtils;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyTAG;
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

import okhttp3.HttpUrl;

public class VeryfyActivity extends AppCompatActivity {

    private ImageView mBack;
    private TextView mMobileNo;
    private TextView mVarifyCode;
    private Button mVeryfy;
    private ProgressBar mProgressBar;

    private String et_user_name = "";
    private String et_vehicle_number = "";
    private String et_mobile_number = "";
    private String et_password = "";
    private String device_token = "";
    private String filePathProfile = "";
    private String filePathRCBook = "";
    private String filePathPUC = "";
    private String filePathInsurance = "";
    private String vehicalType = "";
    private String et_email = "";
    private String latitude = "";
    private String longitude = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veryfy);

        mBack = (ImageView)findViewById(R.id.img_back);
        mMobileNo = (TextView)findViewById(R.id.txt_mobile);
        mVarifyCode = (TextView)findViewById(R.id.txt_varify_code);
        mVeryfy = (Button)findViewById(R.id.btn_veryfy);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        if(getIntent().getExtras() != null){
            et_user_name = getIntent().getExtras().getString("et_user_name","");
            et_vehicle_number = getIntent().getExtras().getString("et_vehicle_number","");
            et_mobile_number = getIntent().getExtras().getString("et_mobile_number","");
            et_password = getIntent().getExtras().getString("et_password","");
            device_token = getIntent().getExtras().getString("device_token","");
            filePathProfile = getIntent().getExtras().getString("filePathProfile","");
            filePathRCBook = getIntent().getExtras().getString("filePathRCBook","");
            filePathPUC = getIntent().getExtras().getString("filePathPUC","");
            filePathInsurance = getIntent().getExtras().getString("filePathInsurance","");
            vehicalType = getIntent().getExtras().getString("vehicalType","");
            et_email = getIntent().getExtras().getString("et_email","");
            latitude = getIntent().getExtras().getString("latitude","");
            longitude = getIntent().getExtras().getString("longitude","");

            mMobileNo.setText(et_mobile_number);
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mVeryfy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify_mobileno_api(mMobileNo.getText().toString(),mVarifyCode.getText().toString());
            }
        });

    }

    private void verify_mobileno_api(String mMobileNo,String mOtp) {
        FileUtils.showProgressBar(VeryfyActivity.this, mProgressBar);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.VERIFY_ACCOUNT).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("v_username", mMobileNo);
        urlBuilder.addQueryParameter("v_otp", mOtp);

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
                        FileUtils.hideProgressBar(VeryfyActivity.this, mProgressBar);
                        showAlertDialog(message,true);
                    } else {
                        FileUtils.hideProgressBar(VeryfyActivity.this, mProgressBar);
                       // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        showAlertDialog(message,false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);
    }


    private void registration() {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = bos.toByteArray();
        HttpPost postRequest = new HttpPost(Constant.URL_SINGUP);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            reqEntity.addPart("device", new StringBody("ANDROID"));
            reqEntity.addPart("v_name", new StringBody(et_user_name));
            reqEntity.addPart("v_email", new StringBody(et_email));
            reqEntity.addPart("v_phone", new StringBody(et_mobile_number));
            reqEntity.addPart("v_password", new StringBody(et_password));
            reqEntity.addPart("v_device_token", new StringBody(device_token));
            reqEntity.addPart("v_vehicle_number", new StringBody(et_vehicle_number));
            reqEntity.addPart("v_vehicle_type", new StringBody(vehicalType));

            if (filePathProfile.equals("")) {

            } else {
                File user_image = new File(filePathProfile);
                reqEntity.addPart("v_image", new FileBody(user_image));
            }

            if (filePathRCBook.equals("")) {


            } else {
                File rc_book_file = new File(filePathRCBook);
                reqEntity.addPart("v_image_rc_book", new FileBody(rc_book_file));
            }
            if (filePathPUC.equals("")) {

            } else {
                File puc_file = new File(filePathPUC);
                reqEntity.addPart("v_image_puc", new FileBody(puc_file));
            }

            if (filePathInsurance.equals("")) {

            }else{
                File insurance_file = new File(filePathInsurance);
                reqEntity.addPart("v_image_insurance", new FileBody(insurance_file));
            }


            reqEntity.addPart("l_latitude", new StringBody(""+latitude));
            reqEntity.addPart("l_longitude", new StringBody(""+longitude));

            Log.d("######", "@@@@@@2" + reqEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MultipartRequest.setRequest(VeryfyActivity.this, reqEntity, postRequest, new MultipartResponce() {
            @Override
            public void responce(final JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("1")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void showAlertDialog(String message, final boolean isSuccess){
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(VeryfyActivity.this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(isSuccess){
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            dialog.cancel();
                        }else {
                            dialog.cancel();
                        }
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
