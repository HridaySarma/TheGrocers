package com.client.thegrocers.NewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.client.thegrocers.Callbacks.IItemClick;
import com.client.thegrocers.EventBus.TrackOrderClicked;
import com.client.thegrocers.Model.OngoingOrdersModel;
import com.client.thegrocers.R;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OngoingOrdersAdapter extends RecyclerView.Adapter<OngoingOrdersAdapter.ViewHolder> {

    Context context;
    List<OngoingOrdersModel> ongoingOrdersModels;

    public OngoingOrdersAdapter(Context context, List<OngoingOrdersModel> ongoingOrdersModels) {
        this.context = context;
        this.ongoingOrdersModels = ongoingOrdersModels;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ongoing_orders_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OngoingOrdersAdapter.ViewHolder holder, int position) {
        if (ongoingOrdersModels.get(position).getCartItemList().size() <2){
            holder.prodName.setText(new StringBuilder(ongoingOrdersModels.get(position).getCartItemList().get(0).getProductName()));
        }else {
            holder.prodName.setText(new StringBuilder(ongoingOrdersModels.get(position).getCartItemList().get(0).getProductName()).append("...").append(ongoingOrdersModels.get(position).getCartItemList().size()-1).append(" more items"));
        }
        holder.orderId.setText(ongoingOrdersModels.get(position).getOrderNumber());
        holder.totalAmount.setText(String.valueOf(ongoingOrdersModels.get(position).getFinalPayment()));
//        holder.status.setText(ongoingOrdersModels.get(position).getOrderStatus());
        holder.totalItemsTv.setText(String.valueOf(ongoingOrdersModels.get(position).getCartItemList().size()));
        holder.trackOrderBtn.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new TrackOrderClicked(true,ongoingOrdersModels.get(position)));
        });

    }

    @Override
    public int getItemCount() {
        return ongoingOrdersModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView prodName,status,totalAmount,orderId,totalItemsTv;
        Button trackOrderBtn;
        IItemClick iItemClickListener;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            prodName = itemView.findViewById(R.id.oo_p_name);
            status = itemView.findViewById(R.id.oo_status);
            totalAmount = itemView.findViewById(R.id.oo_total_amount);
            orderId= itemView.findViewById(R.id.oo_orderId);
            totalItemsTv = itemView.findViewById(R.id.oo_total_items);
            trackOrderBtn = itemView.findViewById(R.id.oo_track_order_btn);
            trackOrderBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iItemClickListener.onItemClicked(v,getAdapterPosition());
        }
    }
}
