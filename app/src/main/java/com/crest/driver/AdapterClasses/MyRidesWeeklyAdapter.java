package com.crest.driver.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crest.driver.CustomeViews.CircleImageView;
import com.crest.driver.ModelClasses.MyRidesModel;
import com.crest.driver.ModelClasses.MyRidesWeeklyModel;
import com.crest.driver.MyRidesDetails;
import com.crest.driver.R;
import com.crest.driver.Utils.FileUtils;
import com.crest.driver.Utils.Preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by brittany on 3/25/17.
 */

public class MyRidesWeeklyAdapter extends RecyclerView.Adapter<MyRidesWeeklyAdapter.MyView> {

    List<MyRidesWeeklyModel> list;

    Context context;

    public MyRidesWeeklyAdapter(List<MyRidesWeeklyModel> list) {
        this.list = list;
    }

    @Override
    public MyRidesWeeklyAdapter.MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.may_rides_list_items, parent, false);
        context = parent.getContext();
        return new MyRidesWeeklyAdapter.MyView(view);
    }

    @Override
    public void onBindViewHolder(MyRidesWeeklyAdapter.MyView holder, final int position) {


        holder.tv_name.setText("" + list.get(position).getName());
        holder.tv_time.setText(FileUtils.setDate(context,list.get(position).getDate()));
        holder.tv_payment.setText("₹ " + list.get(position).getPayment());
        holder.tv_payment_type.setText("" + list.get(position).getPayment_type());

        if(list.get(position).getUser_v_image().isEmpty()){
            Glide
                    .with(context)
                    .load(R.drawable.user)
                    .centerCrop()
                    .crossFade()
                    .into(holder.mProfilePic);
        }else {
            Glide
                    .with(context)
                    .load(list.get(position).getUser_v_image())
                    .centerCrop()
                    .crossFade()
                    .into(holder.mProfilePic);
        }

        holder.lay_ride_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyRidesDetails.class);
                Preferences.setValue(context.getApplicationContext(),Preferences.RIDE_ID,list.get(position).getRide_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyView extends RecyclerView.ViewHolder {
        TextView tv_name, tv_time, tv_payment, tv_payment_type;
        LinearLayout lay_ride_list;
        CircleImageView mProfilePic;

        public MyView(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_payment = (TextView) itemView.findViewById(R.id.tv_payment);
            tv_payment_type = (TextView) itemView.findViewById(R.id.tv_payment_type);
            lay_ride_list = (LinearLayout) itemView.findViewById(R.id.lay_ride_list);
            mProfilePic = (CircleImageView)itemView.findViewById(R.id.img_profile);

        }
    }
}
