package com.crest.driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;

import org.json.JSONObject;

import okhttp3.HttpUrl;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {
    private TextView actionbar_title;
    private Button bt_submit;
    private EditText et_auth, et_new_password, et_retry_new_password;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reset_password);

        initUI();

        actionbar_title.setText(R.string.actionbar_reset_password);
        bt_submit.setOnClickListener(this);
    }

    private void initUI() {
        actionbar_title = (TextView) findViewById(R.id.actionbar_title);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        et_auth = (EditText) findViewById(R.id.et_auth);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_retry_new_password = (EditText) findViewById(R.id.et_retry_new_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                if (validate(et_auth, et_new_password, et_retry_new_password))
                    if (Constant.isOnline(getApplicationContext()))
                        resetPassword(Preferences.getValue(getApplicationContext(), Preferences.EMAIL_ID), et_new_password, et_auth);
                break;
        }
    }

    private void resetPassword(String email, EditText et_new_password, EditText et_auth) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_RESET_PASS).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("v_username", email);
        urlBuilder.addQueryParameter("v_password", et_new_password.getText().toString().trim());
        urlBuilder.addQueryParameter("v_otp", et_auth.getText().toString().trim());
        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();
        VolleyRequestClass.allRequest(ResetPassword.this, newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                final String success = response.optString("status").toString();
                final String message = response.optString("message").toString();
                String value = String.valueOf(success);
                if (value.equals("0")) {
                    Toast.makeText(ResetPassword.this, message, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ResetPassword.this, message, Toast.LENGTH_LONG).show();
                    intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, true);
    }

    private boolean validate(EditText et_auth, EditText et_new_password, EditText et_retry_new_password) {
        if (et_auth.getText().toString().trim().length() > 0) {
            if (et_new_password.getText().toString().trim().length() > 0) {
                if (et_new_password.getText().toString().trim().length() >= 6) {
                    if (et_new_password.getText().toString().trim().equals(et_retry_new_password.getText().toString().trim())) {
                        return true;
                    } else et_retry_new_password.setError("Please enter same password.");
                } else et_new_password.setError("Password must be six to ten charachets.");
            } else et_new_password.setText("please enter new password.");
        } else et_auth.setError("Please enter authentication code.");
        return false;
    }
}
