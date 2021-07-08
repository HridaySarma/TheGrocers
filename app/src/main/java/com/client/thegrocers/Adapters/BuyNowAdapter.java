package com.client.thegrocers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.client.thegrocers.Model.BuyNowClass;
import com.client.thegrocers.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BuyNowAdapter extends RecyclerView.Adapter<BuyNowAdapter.ViewHolder> {


    Context context;
    BuyNowClass buyNowClass;

    public BuyNowAdapter(Context context, BuyNowClass buyNowClass) {
        this.context = context;
        this.buyNowClass = buyNowClass;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.buy_now_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.buyNowName.setText(buyNowClass.getName());
        holder.buyNowPrice.setText(new StringBuilder("Rs ").append(buyNowClass.getPrice()*buyNowClass.getQuantity()));
        holder.buyNowNumberBtn.setNumber(String.valueOf(buyNowClass.getQuantity()));
        holder.buyNowNumberBtn.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                buyNowClass.setQuantity(newValue);
            }
        });
        Glide.with(context).load(buyNowClass.getImage()).into(holder.image);
    }

    public long getQuantity(){
        return buyNowClass.getQuantity();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Unbinder unbinder;
        @BindView(R.id.buy_now_item_name)
        TextView buyNowName;
        @BindView(R.id.buy_now_item_price)
        TextView buyNowPrice;
        @BindView(R.id.buy_now_item_number_btn)
        ElegantNumberButton buyNowNumberBtn;
        @BindView(R.id.buy_now_item_image)
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
