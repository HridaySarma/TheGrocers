package com.client.thegrocers.home.ProductDetails;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.client.thegrocers.Adapters.ProductDetailsImagesAdapter;
import com.client.thegrocers.Common.Common;
import com.client.thegrocers.Database.CartDataSource;
import com.client.thegrocers.Database.CartDatabase;
import com.client.thegrocers.Database.CartItem;
import com.client.thegrocers.Database.LocalCartDataSource;
import com.client.thegrocers.EventBus.BuyNowClicked;
import com.client.thegrocers.EventBus.CounterCartEvent;
import com.client.thegrocers.EventBus.NoAccountButWantToAddToCart;
import com.client.thegrocers.Model.BuyNowClass;
import com.client.thegrocers.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailsFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.product_details_pager)
    LoopingViewPager productImagesPager;
    @BindView(R.id.product_name_details)
    TextView productName;
    @BindView(R.id.product_selling_price_details)
    TextView productSellingPrice;
    @BindView(R.id.product_price_details)
    TextView productPrice;
    @BindView(R.id.product_description_details)
    TextView productDescription;
    @BindView(R.id.product_quantity_details)
    TextView productQuantity;
    @BindView(R.id.buy_now_btn)
    Button buyNowBtn;
    @BindView(R.id.btnCartDetails)
    FloatingActionButton addToCartBtn;
    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;
    @BindView(R.id.number_button_product_details)
    ElegantNumberButton numberButton;
    @BindView(R.id.product_quantity_details_details)
    TextView product_quantity_details_details;
    @BindView(R.id.product_category_name_details)
    TextView productCategoryName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        unbinder = ButterKnife.bind(this,view);
        Common.CurrentFragment = "ProductDetails";
        compositeDisposable = new CompositeDisposable();
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDao());
        initViews();
        return view;
    }

    private void initViews() {
        productDescription.setText(Common.selectedProduct.getDescription());
        productName.setText(Common.selectedProduct.getName());
        productCategoryName.setText(Common.categorySelected.getName());
        productPrice.setText(new StringBuilder("Rs ").append(Common.selectedProduct.getPrice()));
        productPrice.setPaintFlags(productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        productSellingPrice.setText(new StringBuilder("Rs ").append(Common.selectedProduct.getSellingPrice()));
        product_quantity_details_details.setText(new StringBuilder().append(Common.selectedProduct.getPackageSize()).append(" ").append(Common.selectedProduct.getQuantityType()));
        productQuantity.setText(new StringBuilder("").append(Common.selectedProduct.getQuantity()).append(" Left"));
        ProductDetailsImagesAdapter productDetailsImagesAdapter = new ProductDetailsImagesAdapter(getContext(),Common.selectedProduct.getImageModelList(),true);
        productImagesPager.setAdapter(productDetailsImagesAdapter);

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.currentUser == null){
                    EventBus.getDefault().postSticky(new NoAccountButWantToAddToCart(true,false));
                }else {
                    CartItem cartItem = new CartItem();
                    cartItem.setUid(Common.currentUser.getUid());
                    cartItem.setUserPhone(Common.currentUser.getPhone());
                    cartItem.setProductId(Common.selectedProduct.getId());
                    cartItem.setProductName(Common.selectedProduct.getName());
                    cartItem.setProductImage(Common.selectedProduct.getImage());
                    cartItem.setProductPrice(Double.valueOf(String.valueOf(Common.selectedProduct.getPrice())));
                    cartItem.setProductSellingPrice(Double.valueOf(String.valueOf(Common.selectedProduct.getSellingPrice())));
                    cartItem.setProductQuantity(Integer.parseInt(numberButton.getNumber()));

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
                                                        Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
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
                                                            Toast.makeText(getContext(), "Added To Cart Successfully", Toast.LENGTH_SHORT).show();
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
                                                            Toast.makeText(getContext(), "Added To Cart Successfully", Toast.LENGTH_SHORT).show();
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

//                startBuyingProcess();

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.currentUser == null){
                    EventBus.getDefault().postSticky(new NoAccountButWantToAddToCart(true,false));
                }else {
                    CartItem cartItem = new CartItem();
                    cartItem.setUid(Common.currentUser.getUid());
                    cartItem.setUserPhone(Common.currentUser.getPhone());
                    cartItem.setProductId(Common.selectedProduct.getId());
                    cartItem.setProductName(Common.selectedProduct.getName());
                    cartItem.setProductImage(Common.selectedProduct.getImage());
                    cartItem.setProductPrice(Double.valueOf(String.valueOf(Common.selectedProduct.getPrice())));
                    cartItem.setProductSellingPrice(Double.valueOf(String.valueOf(Common.selectedProduct.getSellingPrice())));
                    cartItem.setProductQuantity(Integer.parseInt(numberButton.getNumber()));

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
                                                        Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
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
                                                            Toast.makeText(getContext(), "Added To Cart Successfully", Toast.LENGTH_SHORT).show();
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
                                                            Toast.makeText(getContext(), "Added To Cart Successfully", Toast.LENGTH_SHORT).show();
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

    private void startBuyingProcess() {
        if (Common.currentUser != null){
            Common.buyNowClass  = new BuyNowClass();
            Common.buyNowClass.setQuantity(Integer.parseInt(numberButton.getNumber()));
            Common.buyNowClass.setName(Common.selectedProduct.getName());
            Common.buyNowClass.setPrice(Common.selectedProduct.getSellingPrice());
            Common.buyNowClass.setImage(Common.selectedProduct.getImage());
            EventBus.getDefault().postSticky(new BuyNowClicked(true));
        }else {
            EventBus.getDefault().postSticky(new NoAccountButWantToAddToCart(true,false));
        }

    }
}