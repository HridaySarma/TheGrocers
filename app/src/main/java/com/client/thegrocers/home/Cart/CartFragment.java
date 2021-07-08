package com.client.thegrocers.home.Cart;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.client.thegrocers.Adapters.CartAdapter;
import com.client.thegrocers.Callbacks.ICurrentFragment;
import com.client.thegrocers.Common.Common;
import com.client.thegrocers.Database.CartDataSource;
import com.client.thegrocers.Database.CartDatabase;
import com.client.thegrocers.Database.CartItem;
import com.client.thegrocers.Database.LocalCartDataSource;
import com.client.thegrocers.EventBus.CounterCartEvent;
import com.client.thegrocers.EventBus.DeleteCartItem;
import com.client.thegrocers.EventBus.HideFABCart;
import com.client.thegrocers.EventBus.PlaceOrderClicked;
import com.client.thegrocers.EventBus.UpdateItemInCart;
import com.client.thegrocers.R;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartFragment extends Fragment {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private CartViewModel cartViewModel;

    private CartDataSource cartDataSource;

    private Parcelable recyclerViewState;
    private CartAdapter adapter;

    Unbinder unbinder;
    @BindView(R.id.recycler_cart)
    RecyclerView recycler_cart;
    @BindView(R.id.txt_total_price)
    TextView txt_total_price;
    @BindView(R.id.group_place_holder)
    CardView group_place_holder;
    @BindView(R.id.txt_empty_cart)
    TextView txt_empty_cart;
    @BindView(R.id.empty_cart_anim)
    LottieAnimationView emptyCartAnim;
    private boolean isCartAvailable = false;

    @OnClick(R.id.btn_place_order)
    void PlaceOrderClicked(){
        if (isCartAvailable){
            EventBus.getDefault().postSticky(new PlaceOrderClicked(true));
        }else {
            Snackbar.make(getView(),"Add items to cart to continue", Snackbar.LENGTH_SHORT).show();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        Common.CurrentFragment = "Cart";
        ICurrentFragment iCurrentFragment  = (ICurrentFragment) getContext();
        iCurrentFragment.currentFragment("Other");
        unbinder = ButterKnife.bind(this,view);
//        EventBus.getDefault().postSticky(new CurrentFragment(R.id.nav_cart));
        cartViewModel.initCartDataSource(getContext());
        cartViewModel.getMutableLiveDataCartItems().observe(getViewLifecycleOwner(), new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                if (cartItems == null || cartItems.isEmpty()){
                    recycler_cart.setVisibility(View.GONE);
                    group_place_holder.setVisibility(View.GONE);
                    isCartAvailable = false;
                    txt_empty_cart.setVisibility(View.VISIBLE);
                    emptyCartAnim.setVisibility(View.VISIBLE);

                }else {
                    recycler_cart.setVisibility(View.VISIBLE);
                    group_place_holder.setVisibility(View.VISIBLE);
                    txt_empty_cart.setVisibility(View.GONE);
                    isCartAvailable = true;
                    emptyCartAnim.setVisibility(View.GONE);
                    adapter = new CartAdapter(getContext(),cartItems);
                    recycler_cart.setAdapter(adapter);
                }
            }
        });
        if (Common.currentUser != null){
            initViews();
        }

        return view;
    }

    private void initViews() {
        setHasOptionsMenu(true);
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDao());
        EventBus.getDefault().postSticky(new HideFABCart(true));
        sumAllItemInCart();
        recycler_cart.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_cart.setLayoutManager(layoutManager);
        recycler_cart.addItemDecoration(new DividerItemDecoration(getContext(),layoutManager.getOrientation()));
        sumAllItemInCart();
    }

    private void sumAllItemInCart() {

            cartDataSource.sumPriceInCart(Common.currentUser.getUid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Double>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Double aDouble) {
                            txt_total_price.setText(new StringBuilder("Total: Rs ").append(aDouble));
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!e.getMessage().contains("Query returned empty")){

                            }
//                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                        }
                    }) ;
        }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cart_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_clear_cart){
            cartDataSource.cleanCart(Common.currentUser.getUid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Integer integer) {
                            Toast.makeText(getContext(), "Cart Cleared", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().postSticky(new CounterCartEvent(true));
                        }

                        @Override
                        public void onError(Throwable e) {
//                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onStop() {
        EventBus.getDefault().postSticky(new HideFABCart(false));
        cartViewModel.onStop();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
            compositeDisposable.clear();
        }
        super.onStop();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onUpdateItemInCartEvent(UpdateItemInCart event){
        if (event.getCartItem() != null){
            recyclerViewState =recycler_cart.getLayoutManager().onSaveInstanceState();
            cartDataSource.updateCartItems(event.getCartItem())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Integer integer) {
                            calculateTotalPrice();
                            recycler_cart.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                        }

                        @Override
                        public void onError(Throwable e) {
//                            Toast.makeText(getContext(), "[Update Cart]", Toast.LENGTH_SHORT).show();
                        }
                    } );
        }
    }

    private void calculateTotalPrice() {
        cartDataSource.sumPriceInCart(Common.currentUser.getUid())
                .subscribeOn(  Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Double>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Double aLong) {
                        txt_total_price.setText(new StringBuilder("Total : Rs")
                                .append(Common.formatPrice(aLong)));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }  );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void deleteCardItemClicked(DeleteCartItem event){
        if (event.isSuccess()){
            cartDataSource.deleteCartItem(event.getCartItem())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Integer integer) {
                            calculateTotalPrice();
                            Toast.makeText(getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }
    }

}