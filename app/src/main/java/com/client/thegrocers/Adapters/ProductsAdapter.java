package com.client.thegrocers.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuvraj.thegroceryapp.Callbacks.IItemClick;
import com.yuvraj.thegroceryapp.Common.Common;
import com.yuvraj.thegroceryapp.Database.CartDataSource;
import com.yuvraj.thegroceryapp.Database.CartDatabase;
import com.yuvraj.thegroceryapp.Database.CartItem;
import com.yuvraj.thegroceryapp.Database.LocalCartDataSource;
import com.yuvraj.thegroceryapp.EventBus.CounterCartEvent;
import com.yuvraj.thegroceryapp.EventBus.NoAccountButWantToAddToCart;
import com.yuvraj.thegroceryapp.EventBus.ProductClicked;
import com.yuvraj.thegroceryapp.Model.ProductModel;
import com.yuvraj.thegroceryapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    Context context;
    List<ProductModel> productModelList;
    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;

    public ProductsAdapter(Context context, List<ProductModel> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
        this.compositeDisposable = new CompositeDisposable();
        this.cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDao());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.products_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(productModelList.get(position).getImage()).into(holder.productImage);
        holder.productName.setText(productModelList.get(position).getName());
        holder.productSellingPrice.setText(new StringBuilder("Rs ").append(productModelList.get(position).getSellingPrice()));
        if (productModelList.get(position).getQuantityType().toUpperCase().contains("GM")){
            float ps = productModelList.get(position).getPackageSize()/1000f;
            holder.quantityDetailsProduct.setText(new StringBuilder().append(ps).append(" Kg"));
        }else {
            float ps = productModelList.get(position).getPackageSize()/1000f;
            holder.quantityDetailsProduct.setText(new StringBuilder().append(ps).append(" L"));
        }

        if (productModelList.get(position).getPrice() == productModelList.get(position).getSellingPrice()){
            holder.productPrice.setVisibility(View.GONE);
        }else {
            holder.productPrice.setText(new StringBuilder("Rs ").append(productModelList.get(position).getPrice()));
            holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.setiItemClickListener(new IItemClick() {
            @Override
            public void onItemClicked(View view, int position) {
                Common.selectedProduct = productModelList.get(position);
                EventBus.getDefault().postSticky(new ProductClicked(true));
            }
        });
        holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.currentUser == null){
                    EventBus.getDefault().postSticky(new NoAccountButWantToAddToCart(true,false));
                }else {
                    CartItem cartItem = new CartItem();
                    cartItem.setUid(Common.currentUser.getUid());
                    cartItem.setUserPhone(Common.currentUser.getPhone());
                    cartItem.setProductId(productModelList.get(position).getId());
                    cartItem.setProductName(productModelList.get(position).getName());
                    cartItem.setProductImage(productModelList.get(position).getImage());
                    if (productModelList.get(position).getBreadth() != 0){
                        cartItem.setBreadth(productModelList.get(position).getBreadth());
                        cartItem.setHeight(productModelList.get(position).getHeight());
                        cartItem.setLength(productModelList.get(position).getLength());
                        cartItem.setPackageSize((float) productModelList.get(position).getPackageSize());
                    }
                    cartItem.setProductPrice(Double.valueOf(String.valueOf(productModelList.get(position).getPrice())));
                    cartItem.setProductSellingPrice(Double.valueOf(String.valueOf(productModelList.get(position).getSellingPrice())));
                    cartItem.setProductQuantity(1);

                    cartDataSource.getItemWithAllOptionsInCart(Common.currentUser.getUid(),
                            cartItem.getProductId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<CartItem>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @SuppressLint("LogNotTimber")
                                @Override
                                public void onSuccess(CartItem cartItemFromDb) {
                                    if (cartItemFromDb.equals(cartItem)){
                                        cartItemFromDb.setProductQuantity(cartItemFromDb.getProductQuantity() + cartItem.getProductQuantity());
                                        cartDataSource.updateCartItems(cartItemFromDb)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new SingleObserver<Integer>() {
                                                    @Override
                                                    public void onSubscribe(Disposable d) {

                                                    }

                                                    @Override
                                                    public void onSuccess(Integer integer) {
                                                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                                                        EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Log.e("CART ERROR","[Update Cart] " + e.getMessage());
                                                    }
                                                });
                                    }else {
                                        compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() ->{
                                                            Toast.makeText(context, "Added To Cart Successfully", Toast.LENGTH_SHORT).show();
                                                            EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                                        },throwable ->{
                                                            Log.e("CART ERROR","[CART ERROR]" + throwable.getMessage());

                                                        }

                                                )
                                        );
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (e.getMessage().contains("empty")){
                                        compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() ->{
                                                            Toast.makeText(context, "Added To Cart Successfully", Toast.LENGTH_SHORT).show();
                                                            EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                                        },throwable ->{
                                                            Log.e("CART ERROR","[CART ERROR]" + throwable.getMessage());
                                                        }

                                                )
                                        );
                                    }
                                    Log.e("CART ERROR","[GET CART] " + e.getMessage());
                                }
                            });
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Unbinder unbinder;
        @BindView(R.id.product_name)
        TextView productName;
        @BindView(R.id.product_price)
        TextView productPrice;
        @BindView(R.id.product_image)
        ImageView productImage;
        @BindView(R.id.product_selling_price)
        TextView productSellingPrice;
        @BindView(R.id.addToCartProdBtn)
        FloatingActionButton addToCartBtn;
        IItemClick iItemClickListener;
        @BindView(R.id.quantity_details_product)
        TextView quantityDetailsProduct;

        public void setiItemClickListener(IItemClick iItemClickListener) {
            this.iItemClickListener = iItemClickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
            addToCartBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iItemClickListener.onItemClicked(view,getAdapterPosition());
        }
    }
}
