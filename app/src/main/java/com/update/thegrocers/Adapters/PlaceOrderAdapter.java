package com.update.thegrocers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.update.thegrocers.Database.CartItem;
import com.update.thegrocers.EventBus.UpdateItemInCart;
import com.update.thegrocers.R;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PlaceOrderAdapter extends RecyclerView.Adapter<PlaceOrderAdapter.ViewHolder> {

    Context context;
    List<CartItem> cartItemList;
    private int quanty;


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
        quanty = cartItemList.get(position).getProductQuantity();
        holder.qtyTv.setText(String.valueOf(quanty));
        holder.incrementBtn.setOnClickListener(v -> {
            if (quanty <=9){
                quanty++;
                holder.qtyTv.setText(String.valueOf(quanty));
                cartItemList.get(position).setProductQuantity(quanty);
                EventBus.getDefault().postSticky(new UpdateItemInCart(cartItemList.get(position)));
            }else {
                Snackbar.make(v,"Quantity cannot be greater than 10",Snackbar.LENGTH_SHORT).show();
            }
        });

        holder.decrementBtn.setOnClickListener(v -> {
            if (quanty >1){
                quanty--;
                holder.qtyTv.setText(String.valueOf(quanty));
                cartItemList.get(position).setProductQuantity(quanty);
                EventBus.getDefault().postSticky(new UpdateItemInCart(cartItemList.get(position)));
            }else {
                Snackbar.make(v,"Quantity cannot be greater than 10",Snackbar.LENGTH_SHORT).show();
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

        @BindView(R.id.po_add_num_btn)
        ImageView incrementBtn;

        @BindView(R.id.po_number_tv)
        TextView qtyTv;

        @BindView(R.id.po_decrement_num_btn)
        ImageView decrementBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
