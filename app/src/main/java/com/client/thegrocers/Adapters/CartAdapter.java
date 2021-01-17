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
import com.yuvraj.thegroceryapp.Callbacks.IItemClick;
import com.yuvraj.thegroceryapp.Database.CartDataSource;
import com.yuvraj.thegroceryapp.Database.CartDatabase;
import com.yuvraj.thegroceryapp.Database.CartItem;
import com.yuvraj.thegroceryapp.Database.LocalCartDataSource;
import com.yuvraj.thegroceryapp.EventBus.DeleteCartItem;
import com.yuvraj.thegroceryapp.EventBus.UpdateItemInCart;
import com.yuvraj.thegroceryapp.R;

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
        holder.number_button_cart.setNumber(String.valueOf(Integer.parseInt(String.valueOf(cartItemList.get(position).getProductQuantity()))));
        holder.number_button_cart.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                cartItemList.get(position).setProductQuantity(newValue);
                EventBus.getDefault().postSticky(new UpdateItemInCart(cartItemList.get(position)));
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
        @BindView(R.id.number_button_cart)
        ElegantNumberButton number_button_cart;
        @BindView(R.id.delete_cart_item)
        Button deleteCartItemBtn;
        IItemClick categoryClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            deleteCartItemBtn.setOnClickListener(this);
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
