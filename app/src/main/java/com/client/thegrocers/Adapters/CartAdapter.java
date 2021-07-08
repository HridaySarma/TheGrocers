package com.client.thegrocers.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.client.thegrocers.Callbacks.IItemClick;
import com.client.thegrocers.Database.CartDataSource;
import com.client.thegrocers.Database.CartDatabase;
import com.client.thegrocers.Database.CartItem;
import com.client.thegrocers.Database.LocalCartDataSource;
import com.client.thegrocers.EventBus.DeleteCartItem;
import com.client.thegrocers.EventBus.UpdateItemInCart;
import com.client.thegrocers.R;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<CartItem> cartItemList;
    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;
    private int quanty;

    public CartAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDao());
        this.compositeDisposable = new CompositeDisposable();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_cart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(cartItemList.get(position).getProductImage()).into(holder.img_cart);
        holder.txt_product_name.setText(new StringBuilder(cartItemList.get(position).getProductName()));
        holder.txt_product_price.setText(new StringBuilder("")
                .append(cartItemList.get(position).getProductPrice()));
        holder.txt_product_price.setPaintFlags(holder.txt_product_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txt_product_selling_price.setText(new StringBuilder("").append(cartItemList.get(position).getProductSellingPrice()));
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
        holder.deleteCartItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               EventBus.getDefault().postSticky(new DeleteCartItem(true,cartItemList.get(position)));
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Unbinder unbinder;
        @BindView(R.id.img_cart)
        ImageView img_cart;
        @BindView(R.id.txt_product_price_cart)
        TextView txt_product_price;
        @BindView(R.id.txt_product_selling_price_cart)
        TextView txt_product_selling_price;
        @BindView(R.id.txt_product_name)
        TextView txt_product_name;

        @BindView(R.id.delete_cart_item)
        ImageView deleteCartItemBtn;
        IItemClick categoryClickListener;

        @BindView(R.id.card_add_num_btn)
        ImageView incrementBtn;

        @BindView(R.id.cart_number_tv)
        TextView qtyTv;

        @BindView(R.id.cart_decrement_num_btn)
        ImageView decrementBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            deleteCartItemBtn.setOnClickListener(this);
            decrementBtn.setOnClickListener(this);
            incrementBtn.setOnClickListener(this);
        }

        public void setCategoryClickListener(IItemClick categoryClickListener) {
            this.categoryClickListener = categoryClickListener;
        }

        @Override
        public void onClick(View view) {
            categoryClickListener.onItemClicked(view,getAdapterPosition());
        }
    }
}
