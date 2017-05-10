package com.crest.driver.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crest.driver.AdapterClasses.GetChargesAdapter;
import com.crest.driver.AdapterClasses.MyEarningsAdapter;
import com.crest.driver.ModelClasses.GetExtraAmountModel;
import com.crest.driver.ModelClasses.MyEarningDetail;
import com.crest.driver.ModelClasses.MyEarningsModel;
import com.crest.driver.MyRidesDetails;
import com.crest.driver.R;
import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.FileUtils;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyTAG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.HttpUrl;


public class MyEarningsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private RecyclerView rv_my_earnings;
    MyEarningsAdapter myEarningsAdapter;
    View view;

    private ProgressBar progressBar;
    private Button mSubmit;
    private TextView mToDate;
    private TextView mFromDate;
    private LinearLayout mllTodate;
    private LinearLayout mllFromdate;
    private String mFromTimestamp;
    private String mToTimeStamp;

    public static ArrayList<MyEarningsModel> earningList = new ArrayList<>();
    public static ArrayList<MyEarningDetail> listEarningDetail = new ArrayList<>();
    public MyEarningsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.crest.goyo.fragment
        view= inflater.inflate(R.layout.fragment_my_earnings, container, false);

        initUI();
        return view;
    }

    private void initUI() {

        mSubmit = (Button) view.findViewById(R.id.bt_submit);
        rv_my_earnings = (RecyclerView) view.findViewById(R.id.rv_my_earnings);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mToDate = (TextView) view.findViewById(R.id.txt_to_date);
        mFromDate = (TextView) view.findViewById(R.id.txt_from_date);
        mllFromdate = (LinearLayout)view.findViewById(R.id.ll_from_date);
        mllTodate = (LinearLayout)view.findViewById(R.id.ll_to_date);

        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        mToDate.setText(date);
        mToTimeStamp =String.valueOf(Calendar.getInstance().getTimeInMillis());

        myEarningsAdapter = new MyEarningsAdapter(earningList,listEarningDetail);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toDate = mToDate.getText().toString().trim();
                String fromDate = mFromDate.getText().toString().trim();
                if(!toDate.isEmpty() && !fromDate.isEmpty())
                {
                    get_driver_earning_api();
                }else {
                    Toast.makeText(getActivity(),"Please select date.",Toast.LENGTH_SHORT).show();
                }

            }
        });

        mllFromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePicker();
            }
        });

        mllTodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePicker();
            }
        });
    }

    private void toDatePicker(){
        Calendar calendar = Calendar.getInstance();
        final  SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mToDate.setText(dateFormatter.format(newDate.getTime()));
                mToTimeStamp = String.valueOf(newDate.getTimeInMillis());
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
    }


    private void fromDatePicker(){
        Calendar calendar = Calendar.getInstance();
        final  SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mFromDate.setText(dateFormatter.format(newDate.getTime()));
                mFromTimestamp = String.valueOf(newDate.getTimeInMillis());
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMaxDate(Long.valueOf(mToTimeStamp)-(1000*60*60*24*1));//Before 1 day
    }


    private void get_driver_earning_api() {
        FileUtils.showProgressBar(getActivity(),progressBar);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.GET_DRIVER_EARNING).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getActivity(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token",  Preferences.getValue(getActivity(), Preferences.DRIVER_AUTH_TOKEN));
        urlBuilder.addQueryParameter("from_date", mFromTimestamp);
        urlBuilder.addQueryParameter("to_date", mToTimeStamp);
        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        final okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();
        VolleyRequestClass.allRequest(getActivity(), newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    int responce_status = response.getInt(VolleyTAG.status);
                    String message = response.getString(VolleyTAG.message);
                    if (responce_status == VolleyTAG.response_status) {
                        FileUtils.hideProgressBar(getActivity(),progressBar);
                        earningList.clear();
                        listEarningDetail.clear();
                        JSONObject data = response.getJSONObject("data");
                        JSONArray wallet_history = data.getJSONArray("wallet_history");
                        for(int i=0;i<wallet_history.length();i++){
                            JSONObject obj = wallet_history.getJSONObject(i);
                            JSONObject detail = obj.getJSONObject("details");
                            MyEarningsModel myEarningsModel = new MyEarningsModel(obj.getString("message"),FileUtils.setDate(getActivity(),detail.getString("ride_date")));
                            earningList.add(myEarningsModel);

                            MyEarningDetail myEarningDetail = new MyEarningDetail(FileUtils.setDate(getActivity(),detail.getString("ride_date")),
                                                                                    detail.getString("i_ride_id"),
                                                                                    detail.getString("vehicle_type"),
                                                                                    detail.getString("action"),
                                                                                    detail.getString("action_amount"),
                                                                                    detail.getString("balance"));
                            listEarningDetail.add(myEarningDetail);

                        }

                        if (earningList != null)
                        {
                            Log.e("TAG","two earningList"+earningList.size());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            rv_my_earnings.setLayoutManager(mLayoutManager);
                            rv_my_earnings.setItemAnimator(new DefaultItemAnimator());
                            rv_my_earnings.setAdapter(myEarningsAdapter);
                            myEarningsAdapter.notifyDataSetChanged();
                        }

                    } else {
                        FileUtils.hideProgressBar(getActivity(),progressBar);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);

    }

}
