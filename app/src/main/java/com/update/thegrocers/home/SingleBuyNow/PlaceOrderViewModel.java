package com.update.thegrocers.home.SingleBuyNow;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.update.thegrocers.Common.Common;
import com.update.thegrocers.Database.CartDataSource;
import com.update.thegrocers.Database.CartDatabase;
import com.update.thegrocers.Database.CartItem;
import com.update.thegrocers.Database.LocalCartDataSource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PlaceOrderViewModel extends ViewModel {
    private MutableLiveData<List<CartItem>> mutableLiveDataCartItems;
    private CartDataSource cartDataSource;
    private CompositeDisposable compositeDisposable;

    public PlaceOrderViewModel(){
        compositeDisposable = new CompositeDisposable();
    }

    public  void  initCartDataSource(Context context){
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDao());
    }

    public void onStop(){
        compositeDisposable.clear();
    }

    public MutableLiveData<List<CartItem>> getMutableLiveDataCartItems() {
        if (mutableLiveDataCartItems == null){
            mutableLiveDataCartItems = new MutableLiveData<>();
            getAllCartItems();
            return mutableLiveDataCartItems;
        }
        return mutableLiveDataCartItems;
    }

    private void getAllCartItems() {
        compositeDisposable.add(cartDataSource.getAllCart(Common.currentUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CartItem>>() {
                    @Override
                    public void accept(List<CartItem> cartItems) throws Exception {
                        mutableLiveDataCartItems.setValue(cartItems);
                    }
                }));
    }
}
