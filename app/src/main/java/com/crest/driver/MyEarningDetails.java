package com.crest.driver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crest.driver.ModelClasses.MyEarningDetail;
import com.crest.driver.Utils.Constant;

public class MyEarningDetails extends AppCompatActivity implements View.OnClickListener {
    private TextView actionbar_title;
    MyEarningDetail myEarningDetail;

    private TextView txt_date;
    private TextView txt_trip_id;
    private TextView txt_car_type;
    private TextView txt_acount_type;
    private TextView txt_acount_amount;
    private TextView txt_balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_earning_details);
        if (getIntent().getExtras() != null){
            myEarningDetail = getIntent().getExtras().getParcelable(Constant.MY_EARNING_DETAIL);
        }
        initUI();
    }

    private void initUI() {
        actionbar_title=(TextView)findViewById(R.id.actionbar_title);
        txt_date = (TextView)findViewById(R.id.txt_date);
        txt_car_type = (TextView)findViewById(R.id.txt_car_type);
        txt_trip_id = (TextView)findViewById(R.id.txt_trip_id);
        txt_acount_type = (TextView)findViewById(R.id.txt_acount_type);
        txt_acount_amount = (TextView)findViewById(R.id.txt_acount_amount);
        txt_balance = (TextView)findViewById(R.id.txt_balance);

        actionbar_title.setText("My Earning");
        if(myEarningDetail != null){
            txt_date.setText(myEarningDetail.getDate());
            txt_car_type.setText(myEarningDetail.getType());
            txt_trip_id.setText(myEarningDetail.getTripId());
            txt_acount_type.setText(myEarningDetail.getAction());
            txt_acount_amount.setText(myEarningDetail.getActionAmount());
            txt_balance.setText(myEarningDetail.getBalance());
        }

    }

    @Override
    public void onClick(View v) {

    }
}
