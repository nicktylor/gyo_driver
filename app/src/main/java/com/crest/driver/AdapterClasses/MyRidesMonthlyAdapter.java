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
import com.crest.driver.ModelClasses.MyRidesMonthlyModel;
import com.crest.driver.MyRidesDetails;
import com.crest.driver.R;
import com.crest.driver.Utils.FileUtils;
import com.crest.driver.Utils.Preferences;

import java.util.List;

/**
 * Created by brittany on 3/25/17.
 */

public class MyRidesMonthlyAdapter extends RecyclerView.Adapter<MyRidesMonthlyAdapter.MyView> {

    List<MyRidesMonthlyModel> list;
    Context context;

    public MyRidesMonthlyAdapter(List<MyRidesMonthlyModel> list) {
        this.list = list;
    }

    @Override
    public MyRidesMonthlyAdapter.MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.may_rides_list_items, parent, false);
        context = parent.getContext();
        return new MyRidesMonthlyAdapter.MyView(view);
    }

    @Override
    public void onBindViewHolder(MyRidesMonthlyAdapter.MyView holder, final int position) {

        holder.tv_name.setText("" + list.get(position).getName());
        holder.tv_time.setText("" + FileUtils.setDate(context,list.get(position).getDate()));
        holder.tv_payment.setText("₹ " + list.get(position).getPayment());
        holder.tv_payment_type.setText("" + list.get(position).getPayment_type());

        holder.lay_ride_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyRidesDetails.class);
                Preferences.setValue(context.getApplicationContext(),Preferences.RIDE_ID,list.get(position).getRide_id());
                context.startActivity(intent);
            }
        });

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
