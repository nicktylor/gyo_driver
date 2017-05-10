package com.crest.driver.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crest.driver.ModelClasses.MyEarningDetail;
import com.crest.driver.ModelClasses.MyEarningsModel;
import com.crest.driver.MyEarningDetails;
import com.crest.driver.R;
import com.crest.driver.Utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brittany on 3/25/17.
 */

public class MyEarningsAdapter extends RecyclerView.Adapter<MyEarningsAdapter.MyView> {

    List<MyEarningsModel> list;
    ArrayList<MyEarningDetail> listEarningDetail = new ArrayList<>();
    Context context;

    public MyEarningsAdapter(List<MyEarningsModel> list,ArrayList<MyEarningDetail> listEarningDetail) {
        this.listEarningDetail = listEarningDetail;
        this.list = list;
    }

    @Override
    public MyEarningsAdapter.MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_earnings_list_items, parent, false);
        context = parent.getContext();
        return new MyEarningsAdapter.MyView(view);
    }

    @Override
    public void onBindViewHolder(MyEarningsAdapter.MyView holder, final int position) {


        holder.tv_title.setText("" + list.get(position).getTitle());
        holder.tv_date.setText("" + list.get(position).getDate());

        holder.lay_my_earnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyEarningDetails.class);
                intent.putExtra(Constant.MY_EARNING_DETAIL,listEarningDetail.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyView extends RecyclerView.ViewHolder {
        TextView tv_title, tv_date;
        LinearLayout lay_my_earnings;


        public MyView(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            lay_my_earnings = (LinearLayout) itemView.findViewById(R.id.lay_my_earnings);


        }
    }
}
