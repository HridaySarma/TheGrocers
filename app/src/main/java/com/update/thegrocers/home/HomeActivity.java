package com.update.thegrocers.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.update.thegrocers.Callbacks.ICurrentFragment;
import com.update.thegrocers.Common.Common;
import com.update.thegrocers.Database.CartDataSource;
import com.update.thegrocers.Database.CartDatabase;
import com.update.thegrocers.Database.CartItem;
import com.update.thegrocers.Database.LocalCartDataSource;
import com.update.thegrocers.EventBus.AddNewAddressClicke;
import com.update.thegrocers.EventBus.AddToCartBestDealItemClicked;
import com.update.thegrocers.EventBus.AfterOrderPlaced;
import com.update.thegrocers.EventBus.BestDealItemClick;
import com.update.thegrocers.EventBus.BuyNowClicked;
import com.update.thegrocers.EventBus.CategoryClicked;
import com.update.thegrocers.EventBus.CounterCartEvent;
import com.update.thegrocers.EventBus.CurrentLocationClickedToGetAddress;
import com.update.thegrocers.EventBus.GoToHomeFragment;
import com.update.thegrocers.EventBus.LocationAddedNowBackToAddressList;
import com.update.thegrocers.EventBus.LoginStatus;
import com.update.thegrocers.EventBus.NewLocationUpdatedNowPlaceToAddNewLocation;
import com.update.thegrocers.EventBus.NewSearchClicked;
import com.update.thegrocers.EventBus.NoAccountButWantToAddToCart;
import com.update.thegrocers.EventBus.OnlinePaymentSuccessFull;
import com.update.thegrocers.EventBus.OrderDetailsClicked;
import com.update.thegrocers.EventBus.PlaceOrderClicked;
import com.update.thegrocers.EventBus.PopularCategoryClick;
import com.update.thegrocers.EventBus.ProceedToCheckoutClicked;
import com.update.thegrocers.EventBus.ProductClicked;
import com.update.thegrocers.EventBus.ProductClickedInAllProducts;
import com.update.thegrocers.EventBus.TrackOrderClicked;
import com.update.thegrocers.EventBus.ViewAllCatsClicked;
import com.update.thegrocers.LiveTraking.LiveTrackingActivity;
import com.update.thegrocers.Model.CategoryModel;
import com.update.thegrocers.Model.Order;
import com.update.thegrocers.Model.ProductModel;
import com.update.thegrocers.Model.SingletonProductModel;
import com.update.thegrocers.Model.UserModel;
import com.update.thegrocers.R;
import com.update.thegrocers.UpdatedPackages.NewHome.NewHomeFragment.NewHomeFragment;
import com.update.thegrocers.UpdatedPackages.NewOrders.BaseOrdersFragment;
import com.update.thegrocers.UpdatedPackages.NewProductDetails.NewProductDetailsFragment;
import com.update.thegrocers.home.AddAddress.AddAddressFragment;
import com.update.thegrocers.home.AdressList.AddressListFragment;
import com.update.thegrocers.home.AfterOrderPlaced.AfterOrderPlacedFragment;
import com.update.thegrocers.home.Cart.CartFragment;
import com.update.thegrocers.home.Categories.CategoriesFragment;
import com.update.thegrocers.home.Login.LoginFragment;
import com.update.thegrocers.home.MAP.CurrentLocationPickFragment;
import com.update.thegrocers.home.Orders.OrderDetails;
import com.update.thegrocers.home.Orders.OrdersFragment;
import com.update.thegrocers.home.Orders.OrdersViewModel;
import com.update.thegrocers.home.PlaceOrder.PlaceOrderFragment;
import com.update.thegrocers.home.Products.ProductsFragment;
import com.update.thegrocers.home.Search.SearchFragment;
import com.update.thegrocers.home.SingleBuyNow.BuyNowFragment;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.ibrahimsn.lib.NiceBottomBar;
import me.ibrahimsn.lib.OnItemSelectedListener;

public class HomeActivity extends AppCompatActivity implements ICurrentFragment {

    Unbinder unbinder;
    @BindView(R.id.bottomBar)
    NiceBottomBar bottomBar;
    private CartDataSource cartDataSource;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;
    private FirebaseAuth.AuthStateListener listener;
    private CompositeDisposable disposable = new CompositeDisposable();
    @BindView(R.id.cart_btn_home)
    ImageView cartBtn;
    OrdersViewModel ordersViewModel;
    private CompositeDisposable compositeDisposable;
    @OnClick(R.id.cart_btn_home)
    void changeFrag(){
        CartFragment cartFragment = new CartFragment();
        changeFragment(cartFragment,false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        unbinder = ButterKnife.bind(this);
        usersRef = FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCE);
        firebaseAuth = FirebaseAuth.getInstance();
        compositeDisposable = new CompositeDisposable();
        ordersViewModel = ViewModelProviders.of(this).get(OrdersViewModel.class);
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(this).cartDao());
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyD8KM8oPRgz9rIM_STxjNB4lDisvlcmIWU");
        }
        NewHomeFragment homeFragment = new NewHomeFragment();
        changeFragment(homeFragment,true);


        if (Common.currentUser != null){
            List<Order> orderList = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference(Common.ORDER_REF)
                    .orderByChild("userId")
                    .equalTo(Common.currentUser.getUid())
                    .limitToLast(100)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.exists()) {
                                bottomBar.setBadge(3);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }



        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelect(int pos) {
                switch (pos){
                    case 0:
                        NewHomeFragment homeFragment = new NewHomeFragment();
                        changeFragment(homeFragment,true);
                        break;
                    case 1:
                        CategoriesFragment categoriesFragment = new CategoriesFragment();
                        changeFragment(categoriesFragment,true);
                        break;
                    case 2:

                        SearchFragment searchFragment = new SearchFragment();
                        changeFragment(searchFragment,true);
                        break;
                    case 3:
                        OrdersFragment ordersFragment = new OrdersFragment();
                        changeFragment(ordersFragment,true);
                        break;
                    case 4:
                        BaseOrdersFragment baseOrdersFragment = new BaseOrdersFragment();
                        changeFragment(baseOrdersFragment,true);
                        break;
                }
            }
        });


        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                Dexter.withActivity(HomeActivity.this)
                        .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION
                                ,Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()){
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null) {
                                        CheckUserFromFirebase(user);
                                    }
                                }else {
                                    Toast.makeText(HomeActivity.this, "Need permissions to continue", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            }


                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                            }
                        }).check();
            }
//            }
        };


    }

    private void CheckUserFromFirebase(final FirebaseUser user) {
        /// check if user exists on firebase database ///

        usersRef.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            /// if exists get the user data and store it into local storage ///
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            Common.currentUser = userModel;
                            /// if exists get the user data and store it into local storage ///
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HomeActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                    }
                });

        /// check if user exists on firebase database ///
    }



    private void changeFragment(Fragment fragment, boolean backStack){
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
        if (!fragmentPopped) { //fragment not in back stack, create it.
            final FragmentTransaction ft = manager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            ft.replace(R.id.homeFrag, fragment);
            if (backStack){
                ft.addToBackStack(backStateName);
            }else {
                ft.addToBackStack(null);
            }
            ft.commit();

        }
    }

    @Override
    public void onBackPressed() {
        if (Common.CurrentFragment.equals("Home")){
            finish();
        }else if (Common.CurrentFragment.equals("ProductDetails")){
            ProductsFragment productsFragment = new ProductsFragment();
            changeFragment(productsFragment,false);
        }else
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        if (listener !=null){
            firebaseAuth.removeAuthStateListener(listener);
            disposable.clear();
            super.onStop();
        }
        super.onStop();
    }



    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onCategoryClicked(CategoryClicked event){
        if (event.isSuccess()){
            ProductsFragment productsFragment = new ProductsFragment();
            changeFragment(productsFragment,true);
            EventBus.getDefault().removeStickyEvent(CategoryClicked.class);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onProductClicked(ProductClicked event){
        if (event.isSuccess()){
            NewProductDetailsFragment productDetailsFragment = new NewProductDetailsFragment();
            changeFragment(productDetailsFragment,true);
            EventBus.getDefault().removeStickyEvent(ProductClicked.class);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onCartClickedButNoLogin(NoAccountButWantToAddToCart event){
        if (event.isCategorySuccess() || event.isDetailsSuccess()){
            LoginFragment loginFragment = new LoginFragment();
            changeFragment(loginFragment,false);
            EventBus.getDefault().removeStickyEvent(NoAccountButWantToAddToCart.class);


        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onLoggedInSuccessFully(LoginStatus event){
        if (event.isSuccess()){
            NewHomeFragment homeFragment = new NewHomeFragment();
            changeFragment(homeFragment,true);
            EventBus.getDefault().removeStickyEvent(LoginStatus.class);
//            if (Common.selectedProduct != null){
//                ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
//                changeFragment(productDetailsFragment,true);
//                EventBus.getDefault().removeStickyEvent(LoginStatus.class);
//            }else if (Common.categorySelected != null){
//                CategoriesFragment categoriesFragment = new CategoriesFragment();
//                changeFragment(categoriesFragment,true);
//                EventBus.getDefault().removeStickyEvent(LoginStatus.class);
//            }
//            else {
//                HomeFragment homeFragment = new HomeFragment();
//                changeFragment(homeFragment,true);
//                EventBus.getDefault().removeStickyEvent(LoginStatus.class);
//            }
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onBuyNowClicked(BuyNowClicked event){
        if (event.isSuccess()){
            BuyNowFragment buyNowFragment = new BuyNowFragment();
            changeFragment(buyNowFragment,false);
            EventBus.getDefault().removeStickyEvent(BuyNowClicked.class);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onPlaceOrderClicked(PlaceOrderClicked event){
        if (event.isSuccess()){
            AddressListFragment addressListFragment = new AddressListFragment();
            changeFragment(addressListFragment,false);
            EventBus.getDefault().removeStickyEvent(PlaceOrderClicked.class);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void OnAddressPickedAndNowSetup(NewLocationUpdatedNowPlaceToAddNewLocation event){
        if (event.isSuccess()){
            Common.AddressToBeSelected = event.getAddressModel();
            AddAddressFragment addAddressFragment = new AddAddressFragment();
            changeFragment(addAddressFragment,false);
            EventBus.getDefault().removeStickyEvent(NewLocationUpdatedNowPlaceToAddNewLocation.class);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onAddNewAddressClicked(AddNewAddressClicke event){
        if (event.isSuccess()){
            AddAddressFragment addAddressFragment = new AddAddressFragment();
            changeFragment(addAddressFragment,false);
            EventBus.getDefault().removeStickyEvent(AddNewAddressClicke.class);
        }
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onLocationAdded(LocationAddedNowBackToAddressList event){
        if (event.isSuccess()){
            AddressListFragment addressListFragment = new AddressListFragment();
            changeFragment(addressListFragment,false);
            EventBus.getDefault().removeStickyEvent(LocationAddedNowBackToAddressList.class);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void proceedToCheckOut(ProceedToCheckoutClicked event){
        if (event.isSuccess()){
            PlaceOrderFragment placeOrderFragment = new PlaceOrderFragment();
            Common.addressSelectedForDelivery = event.getAddressModel();
            changeFragment(placeOrderFragment,false);
            EventBus.getDefault().removeStickyEvent(ProceedToCheckoutClicked.class);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void afterOrderPlaced(AfterOrderPlaced event){
        if(event.isSuccess()){
            AfterOrderPlacedFragment afterOrderPlaced = new AfterOrderPlacedFragment();
            changeFragment(afterOrderPlaced,false);
            EventBus.getDefault().removeStickyEvent(AfterOrderPlaced.class);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void GoToHomeFragment(GoToHomeFragment event){
        if(event.isSuccess()){
            NewHomeFragment homeFragment = new NewHomeFragment();
            changeFragment(homeFragment,true);
            EventBus.getDefault().removeStickyEvent(GoToHomeFragment.class);
        }
    }
//
//    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
//    public void goToPaymentGateway(StartPaymentGateway event){
//        if (event.isSuccess()){
//            Common.PaymentGatewayOrderDetails = event.getOrder();
//            if (event.getOrder().getCreateDate() != null){
//                PaymentGatewayFragment paymentGatewayFragment = new PaymentGatewayFragment();
//                changeFragment(paymentGatewayFragment,false);
//            }else {
//                Toast.makeText(this, "Error creating payment please try later again", Toast.LENGTH_SHORT).show();
//            }
//
//            EventBus.getDefault().removeStickyEvent(StartPaymentGateway.class);
//        }
//    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onSelectCurrentLocationClicked(CurrentLocationClickedToGetAddress event){
        if(event.isSuccess()){
            CurrentLocationPickFragment currentLocationPickFragment = new CurrentLocationPickFragment();
            changeFragment(currentLocationPickFragment,false);
            EventBus.getDefault().removeStickyEvent(CurrentLocationClickedToGetAddress.class);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onBestDealApparelItemClick(BestDealItemClick event){
        if (event.isSuccess() && event.getBestDealModel() != null){
            AlertDialog alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(HomeActivity.this).build();
            alertDialog.setMessage("Loading items....");
            alertDialog.show();
                FirebaseDatabase.getInstance().getReference(Common.CATEGORIES_REF)
                        .child(event.getBestDealModel().getCategory_id())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    Common.categorySelected = snapshot.getValue(CategoryModel.class);
                                    Common.categorySelected.setName(event.getBestDealModel().getCategory_id());
                                    FirebaseDatabase.getInstance().getReference(Common.CATEGORIES_REF)
                                            .child(event.getBestDealModel().getCategory_id())
                                            .child("products")
                                            .orderByChild("id")
                                            .equalTo(event.getBestDealModel().getProduct_id())
                                            .limitToLast(1)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()){
                                                        for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                                                            Common.selectedProduct = itemSnapshot.getValue(ProductModel.class);
                                                            Common.selectedProduct.setKey(Integer.parseInt(itemSnapshot.getKey()));
                                                        }
                                                        NewProductDetailsFragment productDetailsFragment = new NewProductDetailsFragment();
                                                        changeFragment(productDetailsFragment,true);
                                                        alertDialog.dismiss();
                                                    }else {
                                                        alertDialog.dismiss();
                                                        Toast.makeText(HomeActivity.this, "Item doesn't exist", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    alertDialog.dismiss();
                                                    Toast.makeText(HomeActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else {
                                    alertDialog.dismiss();
                                    Toast.makeText(HomeActivity.this, "Product Sold out", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(HomeActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
            }

    }


    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onTopProdAddToCart(AddToCartBestDealItemClicked event){
        if (event.isSuccess() && event.getBestDealModel() != null){
            AlertDialog alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(HomeActivity.this).build();
            alertDialog.setMessage("Adding to cart");
            alertDialog.show();
            FirebaseDatabase.getInstance().getReference(Common.CATEGORIES_REF)
                    .child(event.getBestDealModel().getCategory_id())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Common.categorySelected = snapshot.getValue(CategoryModel.class);
                                Common.categorySelected.setName(event.getBestDealModel().getCategory_id());
                                FirebaseDatabase.getInstance().getReference(Common.CATEGORIES_REF)
                                        .child(event.getBestDealModel().getCategory_id())
                                        .child("products")
                                        .orderByChild("id")
                                        .equalTo(event.getBestDealModel().getProduct_id())
                                        .limitToLast(1)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                                                        Common.selectedProduct = itemSnapshot.getValue(ProductModel.class);
                                                        Common.selectedProduct.setKey(Integer.parseInt(itemSnapshot.getKey()));
                                                    }
//                                                    NewProductDetailsFragment productDetailsFragment = new NewProductDetailsFragment();
//                                                    changeFragment(productDetailsFragment,true);
                                                    CartItem cartItem = new CartItem();
                                                    cartItem.setUid(Common.currentUser.getUid());
                                                    cartItem.setUserPhone(Common.currentUser.getPhone());
                                                    cartItem.setProductId(Common.selectedProduct.getId());
                                                    cartItem.setProductName(Common.selectedProduct.getName());
                                                    cartItem.setProductImage(Common.selectedProduct.getImage());

                                                    cartItem.setProductPrice(Double.valueOf(String.valueOf(Common.selectedProduct.getPrice())));
                                                    cartItem.setProductSellingPrice(Double.valueOf(String.valueOf(Common.selectedProduct.getSellingPrice())));
                                                    cartItem.setProductQuantity(Common.selectedProduct.getQuantity());

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
                                                                                        Toast.makeText(HomeActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                                                        alertDialog.dismiss();
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
                                                                                            Toast.makeText(HomeActivity.this, "Added To Cart Successfully", Toast.LENGTH_SHORT).show();
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
                                                                                            Toast.makeText(HomeActivity.this, "Added To Cart Successfully", Toast.LENGTH_SHORT).show();
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
                                                    alertDialog.dismiss();
                                                }else {
                                                    alertDialog.dismiss();
                                                    Toast.makeText(HomeActivity.this, "Item doesn't exist", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                alertDialog.dismiss();
                                                Toast.makeText(HomeActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }else {
                                alertDialog.dismiss();
                                Toast.makeText(HomeActivity.this, "Product Sold out", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(HomeActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    });
        }

    }

    /// when a best deal item is clicked he will be sent back to the respected product ///


    /// when a popular category is clicked he will be sent back to the respected category ///
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onPopularCategoryClicked(PopularCategoryClick event){
        if (event.isSuccess() && event.getPopularCategoriesModel() != null){
            AlertDialog alertDialog = new SpotsDialog.Builder().setContext(HomeActivity.this).setCancelable(false).build();
            alertDialog.setMessage("Loading items...");
            alertDialog.show();
                FirebaseDatabase.getInstance().getReference(Common.CATEGORIES_REF)
                        .child(event.getPopularCategoriesModel().getCategory_id())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    Common.categorySelected = snapshot.getValue(CategoryModel.class);
                                    Common.categorySelected.setName(event.getPopularCategoriesModel().getCategory_id());
                                    ProductsFragment categoriesFragment = new ProductsFragment();
                                    changeFragment(categoriesFragment,true);
                                    alertDialog.dismiss();
                                }else {
                                    alertDialog.dismiss();
                                    Toast.makeText(HomeActivity.this, "This is just a prop click other for info", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                alertDialog.dismiss();
                                Toast.makeText(HomeActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                EventBus.getDefault().removeStickyEvent(PopularCategoryClick.class);
            }

    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void productClickedOnAllProductsSearch(ProductClickedInAllProducts event){
        if (event.isSuccess()){

                GoToProductsDetails(event.getSingletonProductModel());

        }
    }
    /// When the search results are got and the user the clicking on the product will be redirected to the respected product details ///

    private void GoToProductsDetails(SingletonProductModel singletonProductModel) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.CATEGORIES_REF);
        AlertDialog alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(HomeActivity.this).build();
        alertDialog.setMessage("Loading product ...");
        alertDialog.show();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot itemSnapshot :snapshot.getChildren()){
                        CategoryModel categoryModel = itemSnapshot.getValue(CategoryModel.class);
                        categoryModel.setName(itemSnapshot.getKey());
                        if (categoryModel.getName().equals(singletonProductModel.getCategory_id())){
                            Common.categorySelected = categoryModel;
                            for (int i = 0;i<Common.categorySelected.getProducts().size();i++){
                                ProductModel productModel;
                                productModel = Common.categorySelected.getProducts().get(i);
                                if (productModel.getId().equals(singletonProductModel.getProduct_id())){
                                    Common.selectedProduct = productModel;
                                    Common.selectedProduct.setKey(i);
                                    NewProductDetailsFragment productsFragment = new NewProductDetailsFragment();
                                    changeFragment(productsFragment,true);
                                    EventBus.getDefault().removeStickyEvent(ProductClickedInAllProducts.class);
                                    alertDialog.dismiss();
                                }else {
                                    alertDialog.dismiss();
                                }
                            }
                        }else {
                            alertDialog.dismiss();
                        }
                    }
                }else {
                    alertDialog.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertDialog.dismiss();
                Toast.makeText(HomeActivity.this, ""+error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onOnlinePaymentSuccessfull(OnlinePaymentSuccessFull event){
        if (event.isSuccess()){
            AfterOrderPlacedFragment placeOrderFragment = new AfterOrderPlacedFragment();
            changeFragment(placeOrderFragment,true);
            EventBus.getDefault().removeStickyEvent(OnlinePaymentSuccessFull.class);
        }
        }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void OnOrderDetailsClicked(OrderDetailsClicked event){
        if (event.isSuccess()){
            Common.orderSelectedForDetails = event.getOrder();
            OrderDetails orderDetails = new OrderDetails();
            changeFragment(orderDetails,true);
            EventBus.getDefault().removeStickyEvent(OrderDetailsClicked.class);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onSearchClickedNew(NewSearchClicked event){
        if (event.isSuccess()){
            bottomBar.setActiveItem(2);
            SearchFragment searchFragment = new SearchFragment();
            changeFragment(searchFragment,false);
            EventBus.getDefault().removeStickyEvent(NewSearchClicked.class);
        }
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onViewAllCatsClicked(ViewAllCatsClicked event){
        if (event.isSuccess()){
            bottomBar.setActiveItem(1);
            CategoriesFragment categoriesFragment = new CategoriesFragment();
            changeFragment(categoriesFragment,false);
            EventBus.getDefault().removeStickyEvent(ViewAllCatsClicked.class);
        }
    }


    @Override
    public void currentFragment(String currentFragmentName) {
        Common.CurrentFragment = currentFragmentName;
        if (currentFragmentName.equals("Home") || currentFragmentName.equals("Search") || currentFragmentName.equals("Categories") || currentFragmentName.equals("Orders")){
            bottomBar.setVisibility(View.VISIBLE);

        }else {
            bottomBar.setVisibility(View.GONE);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void OnTrackOrderClicked(TrackOrderClicked event){
        if (event.isSuccess()){
            Intent intent = new Intent(HomeActivity.this, LiveTrackingActivity.class);
            intent.putExtra("OrderData", event.getOngoingOrdersModel());
            startActivity(intent);
        }
    }

}