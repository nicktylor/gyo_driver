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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import okhttp3.HttpUrl;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private TextView actionbar_title;
    private Button bt_submit;
    private EditText edit_email;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgot_password);

        initUI();

        actionbar_title.setText(R.string.actionbar_password_recovery);
        bt_submit.setOnClickListener(this);

    }

    private void initUI() {
        actionbar_title = (TextView) findViewById(R.id.actionbar_title);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        edit_email = (EditText) findViewById(R.id.edit_email);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                if (Constant.isOnline(getApplicationContext()))
                    if (validate(edit_email)) forgotPass(edit_email);
                break;
        }
    }

    private void forgotPass(final EditText edit_email) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.URL_FORGOT_PASS).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang","en");
        urlBuilder.addQueryParameter("v_username", edit_email.getText().toString());
        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();
        VolleyRequestClass.allRequest(ForgotPassword.this, newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                final String success = response.optString("status").toString();
                final String message = response.optString("message").toString();
                String value = String.valueOf(success);
                if (value.equals("0")) {
                    Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_LONG).show();
                    intent = new Intent(getApplicationContext(), ResetPassword.class);
                    Preferences.setValue(getApplicationContext(),Preferences.EMAIL_ID_RESET_PASSWORD,edit_email.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        }, true);
    }

    private boolean validate(EditText email) {
        if (email.getText().toString().trim().length() > 0) {
                if (!Character.isAlphabetic(email.getText().toString().trim().charAt(0)))
                {
                    if (isValidMobile(email.getText().toString().trim())) {
                        return true;
                    } else email.setError("Invalid mobile no.");

                }else {
                    if (email.getText().toString().matches(Constant.emailPattern)) {
                        return true;
                    } else email.setError("Invalid email.");
                }

        } else email.setError("Please enter email or phone number.");
        return false;
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
