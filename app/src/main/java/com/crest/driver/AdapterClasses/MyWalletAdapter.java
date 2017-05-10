package com.crest.driver.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crest.driver.ModelClasses.MyEarningsModel;
import com.crest.driver.ModelClasses.MyWalletModel;
import com.crest.driver.MyEarningDetails;
import com.crest.driver.R;

import java.util.List;

/**
 * Created by brittany on 3/25/17.
 */

public class MyWalletAdapter extends RecyclerView.Adapter<MyWalletAdapter.MyView> {

    List<MyWalletModel> list;
    Context context;

    public MyWalletAdapter(List<MyWalletModel> list) {
        this.list = list;
    }

    @Override
    public MyWalletAdapter.MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_wallet_list_items, parent, false);
        context = parent.getContext();
        return new MyWalletAdapter.MyView(view);
    }

    @Override
    public void onBindViewHolder(MyWalletAdapter.MyView holder, int position) {


        holder.tv_title.setText("" + list.get(position).getTitle());
        holder.tv_from.setText("" + "From :- " + list.get(position).getFrom());


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
