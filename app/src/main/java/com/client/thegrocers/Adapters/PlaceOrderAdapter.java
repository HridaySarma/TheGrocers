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
import com.yuvraj.thegroceryapp.Database.CartItem;
import com.yuvraj.thegroceryapp.EventBus.UpdateItemInCart;
import com.yuvraj.thegroceryapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PlaceOrderAdapter extends RecyclerView.Adapter<PlaceOrderAdapter.ViewHolder> {

    Context context;
    List<CartItem> cartItemList;

    public PlaceOrderAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_order_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(cartItemList.get(position).getProductImage()).into(holder.img_cart);
        holder.txt_product_name.setText(new StringBuilder(cartItemList.get(position).getProductName()));
        holder.txt_product_price.setText(new StringBuilder("")
                .append(cartItemList.get(position).getProductSellingPrice()));
        holder.number_button_cart.setNumber(String.valueOf(Integer.parseInt(String.valueOf(cartItemList.get(position).getProductQuantity()))));
        holder.number_button_cart.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                cartItemList.get(position).setProductQuantity(newValue);
                EventBus.getDefault().postSticky(new UpdateItemInCart(cartItemList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public CartItem getItemAtPosition(int pos){
        return cartItemList.get(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Unbinder unbinder;
        @BindView(R.id.img_cart_po)
        ImageView img_cart;
        @BindView(R.id.txt_product_price_cart_po)
        TextView txt_product_price;
        @BindView(R.id.txt_product_name_po)
        TextView txt_product_name;
        @BindView(R.id.number_button_po)
        ElegantNumberButton number_button_cart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
