package com.crest.driver.AdapterClasses;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crest.driver.MapsActivity;
import com.crest.driver.ModelClasses.AddAmountModel;
import com.crest.driver.R;


import java.util.ArrayList;


/**
 * Created by cresttwo on 6/25/2016.
 */
public class AddAmountSpinnerAdapter extends BaseAdapter {
    ArrayList<AddAmountModel> list;
    Context context;
    int flags[];
    String[] countryNames;
    LayoutInflater inflter;

    public AddAmountSpinnerAdapter(MapsActivity mapsActivity, ArrayList<AddAmountModel> list) {
        this.context = mapsActivity;
        this.list=list;
        inflter = (LayoutInflater.from(mapsActivity));
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.add_amount_list_items, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_from = (TextView) view.findViewById(R.id.tv_from);

        tv_title.setText(list.get(position).getTitle());
        tv_from.setText(list.get(position).getFrom());
//        tv_language.setText(countryNames[i]);
//        Picasso.with(context).load(""+list.get(position).getV_l_flag().replaceAll("\'","")).memoryPolicy(MemoryPolicy.NO_CACHE).resize(50, 50).into(viewGroup.iv_flag);

        return view;
    }

}
