package com.crest.driver.AdapterClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crest.driver.ModelClasses.CompleteRideModel;
import com.crest.driver.ModelClasses.GetExtraAmountModel;
import com.crest.driver.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brittany on 3/25/17.
 */

public class GetChargesAdapter extends RecyclerView.Adapter<GetChargesAdapter.MyView> {

    ArrayList<GetExtraAmountModel> arrarExtraAmount = new ArrayList<>();
    Context context;

    public GetChargesAdapter(ArrayList<GetExtraAmountModel> arrarExtraAmount) {
        this.arrarExtraAmount = arrarExtraAmount;
    }

    @Override
    public GetChargesAdapter.MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_amount, parent, false);
        context = parent.getContext();
        return new GetChargesAdapter.MyView(view);
    }

    @Override
    public void onBindViewHolder(GetChargesAdapter.MyView holder, int position) {

        holder.txt_amount_type.setText("" + arrarExtraAmount.get(position).getAmountType());
        holder.txt_amount.setText(""  + arrarExtraAmount.get(position).getAmount());
    }

    @Override
    public int getItemCount() {
        return arrarExtraAmount.size();
    }

    public class MyView extends RecyclerView.ViewHolder {
        TextView txt_amount_type, txt_amount;



        public MyView(View itemView) {
            super(itemView);
            txt_amount_type = (TextView) itemView.findViewById(R.id.txt_amount_type);
            txt_amount = (TextView) itemView.findViewById(R.id.txt_amount);



        }
    }
}
