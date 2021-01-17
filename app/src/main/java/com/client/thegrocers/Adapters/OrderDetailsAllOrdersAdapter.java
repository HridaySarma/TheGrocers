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
import com.yuvraj.thegroceryapp.Database.CartItem;
import com.yuvraj.thegroceryapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrderDetailsAllOrdersAdapter extends RecyclerView.Adapter<OrderDetailsAllOrdersAdapter.ViewHolder> {

    List<CartItem> cartItemList;
    Context context;

    public OrderDetailsAllOrdersAdapter(List<CartItem> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_orders_details_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(cartItemList.get(position).getProductImage()).into(holder.prodImage);
        holder.prodName.setText(cartItemList.get(position).getProductName());
        holder.prodSellingPrice.setText(new StringBuilder("Rs ").append(cartItemList.get(position).getProductSellingPrice()));
        holder.prodQuantity.setText(String.valueOf(cartItemList.get(position).getProductQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        Unbinder unbinder;
        @BindView(R.id.aodi_image)
        ImageView prodImage;
        @BindView(R.id.aodi_name)
        TextView prodName;
        @BindView(R.id.aodi_color)
        TextView prodColor;
        @BindView(R.id.aodi_price)
        TextView prodSellingPrice;
        @BindView(R.id.aodi_size)
        TextView prodSize;
        @BindView(R.id.aodi_quantity)
        TextView prodQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
