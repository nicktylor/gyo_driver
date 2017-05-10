package com.crest.driver.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crest.driver.AdapterClasses.MyWalletAdapter;
import com.crest.driver.ModelClasses.MyWalletModel;
import com.crest.driver.R;
import com.crest.driver.Utils.Constant;
import com.crest.driver.Utils.Preferences;
import com.crest.driver.VolleyLibrary.RequestInterface;
import com.crest.driver.VolleyLibrary.VolleyRequestClass;
import com.crest.driver.VolleyLibrary.VolleyTAG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.HttpUrl;


public class MyWalletFragment extends Fragment implements View.OnClickListener {

    private View view;
    private RecyclerView rv_my_wallet;
    private MyWalletAdapter myWalletAdapter;
    private TextView mWallet;
    public static ArrayList<MyWalletModel> walletList;

    public MyWalletFragment() {
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
        view = inflater.inflate(R.layout.fragment_my_wallet, container, false);

        initUI();

        walletList = new ArrayList<MyWalletModel>();

        myWalletAdapter = new MyWalletAdapter(walletList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv_my_wallet.setLayoutManager(mLayoutManager);
        rv_my_wallet.setItemAnimator(new DefaultItemAnimator());
        rv_my_wallet.setAdapter(myWalletAdapter);
        my_wallet_api();

        return view;
    }

    private void initUI() {
        mWallet = (TextView) view.findViewById(R.id.txt_wallet_rs);
        rv_my_wallet=(RecyclerView) view.findViewById(R.id.rv_my_wallet);
        rv_my_wallet.setOnClickListener(this);
    }

    private void my_wallet_api() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constant.GET_DRIVER_WALLETE).newBuilder();
        urlBuilder.addQueryParameter("device", "ANDROID");
        urlBuilder.addQueryParameter("lang", "en");
        urlBuilder.addQueryParameter("login_id", Preferences.getValue(getActivity(), Preferences.DRIVER_ID));
        urlBuilder.addQueryParameter("v_token",  Preferences.getValue(getActivity(), Preferences.DRIVER_AUTH_TOKEN));

        String url = urlBuilder.build().toString();
        String newurl = url.replaceAll(" ", "%20");
        okhttp3.Request request = new okhttp3.Request.Builder().url(newurl).build();
        VolleyRequestClass.allRequest(getActivity(), newurl, new RequestInterface() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    int responce_status = response.getInt(VolleyTAG.status);
                    String message = response.getString(VolleyTAG.message);
                    if (responce_status == VolleyTAG.response_status) {
                        JSONObject data = response.getJSONObject("data");
                        mWallet.setText("Rs "+data.getString("wallet_amount"));

                        JSONArray wallet_history = data.getJSONArray("wallet_history");

                        for (int i = 0; i < wallet_history.length(); i++) {
                            walletList.add(new MyWalletModel(wallet_history.getJSONObject(i).getString("message"),
                                                                wallet_history.getJSONObject(i).getString("from")));
                        }
                        myWalletAdapter.notifyDataSetChanged();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
