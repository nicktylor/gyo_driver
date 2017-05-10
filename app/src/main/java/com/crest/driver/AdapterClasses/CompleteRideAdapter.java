package com.crest.driver.AdapterClasses;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crest.driver.ModelClasses.CompleteRideModel;
import com.crest.driver.ModelClasses.GetExtraAmountModel;
import com.crest.driver.ModelClasses.MyWalletModel;
import com.crest.driver.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brittany on 3/25/17.
 */

public class CompleteRideAdapter extends RecyclerView.Adapter<CompleteRideAdapter.MyView> {

    ArrayList<GetExtraAmountModel> list;
    Context context;

    public CompleteRideAdapter(ArrayList<GetExtraAmountModel> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public CompleteRideAdapter.MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complete_ride_list_items, parent, false);
        context = parent.getContext();
        return new CompleteRideAdapter.MyView(view);
    }

    @Override
    public void onBindViewHolder(MyView holder, int position) {


        if(position == getItemCount()-1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.tv_title.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                holder.tv_from.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            }
        }
        holder.tv_title.setText("" + list.get(position).getAmountType());
        holder.tv_from.setText(""  + list.get(position).getAmount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyView extends RecyclerView.ViewHolder {
        TextView tv_title, tv_from;



        public MyView(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_from = (TextView) itemView.findViewById(R.id.tv_from);

        }
    }
}
