package com.client.thegrocers.home;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

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
import com.yuvraj.thegroceryapp.Common.Common;
import com.yuvraj.thegroceryapp.Database.CartDataSource;
import com.yuvraj.thegroceryapp.Database.CartDatabase;
import com.yuvraj.thegroceryapp.Database.LocalCartDataSource;
import com.yuvraj.thegroceryapp.EventBus.AddNewAddressClicke;
import com.yuvraj.thegroceryapp.EventBus.AfterOrderPlaced;
import com.yuvraj.thegroceryapp.EventBus.BestDealItemClick;
import com.yuvraj.thegroceryapp.EventBus.BuyNowClicked;
import com.yuvraj.thegroceryapp.EventBus.CategoryClicked;
import com.yuvraj.thegroceryapp.EventBus.CurrentLocationClickedToGetAddress;
import com.yuvraj.thegroceryapp.EventBus.GoToHomeFragment;
import com.yuvraj.thegroceryapp.EventBus.LocationAddedNowBackToAddressList;
import com.yuvraj.thegroceryapp.EventBus.LoginStatus;
import com.yuvraj.thegroceryapp.EventBus.NewLocationUpdatedNowPlaceToAddNewLocation;
import com.yuvraj.thegroceryapp.EventBus.NoAccountButWantToAddToCart;
import com.yuvraj.thegroceryapp.EventBus.OnlinePaymentSuccessFull;
import com.yuvraj.thegroceryapp.EventBus.OrderDetailsClicked;
import com.yuvraj.thegroceryapp.EventBus.PlaceOrderClicked;
import com.yuvraj.thegroceryapp.EventBus.PopularCategoryClick;
import com.yuvraj.thegroceryapp.EventBus.ProceedToCheckoutClicked;
import com.yuvraj.thegroceryapp.EventBus.ProductClicked;
import com.yuvraj.thegroceryapp.EventBus.ProductClickedInAllProducts;
import com.yuvraj.thegroceryapp.Model.CategoryModel;
import com.yuvraj.thegroceryapp.Model.Order;
import com.yuvraj.thegroceryapp.Model.ProductModel;
import com.yuvraj.thegroceryapp.Model.SingletonProductModel;
import com.yuvraj.thegroceryapp.Model.UserModel;
import com.yuvraj.thegroceryapp.R;
import com.yuvraj.thegroceryapp.home.AddAddress.AddAddressFragment;
import com.yuvraj.thegroceryapp.home.AdressList.AddressListFragment;
import com.yuvraj.thegroceryapp.home.AfterOrderPlaced.AfterOrderPlacedFragment;
import com.yuvraj.thegroceryapp.home.Cart.CartFragment;
import com.yuvraj.thegroceryapp.home.Categories.CategoriesFragment;
import com.yuvraj.thegroceryapp.home.HomeFragment.HomeFragment;
import com.yuvraj.thegroceryapp.home.Login.LoginFragment;
import com.yuvraj.thegroceryapp.home.MAP.CurrentLocationPickFragment;
import com.yuvraj.thegroceryapp.home.Orders.OrderDetails;
import com.yuvraj.thegroceryapp.home.Orders.OrdersFragment;
import com.yuvraj.thegroceryapp.home.Orders.OrdersViewModel;
import com.yuvraj.thegroceryapp.home.PlaceOrder.PlaceOrderFragment;
import com.yuvraj.thegroceryapp.home.ProductDetails.ProductDetailsFragment;
import com.yuvraj.thegroceryapp.home.Products.ProductsFragment;
import com.yuvraj.thegroceryapp.home.Search.SearchFragment;
import com.yuvraj.thegroceryapp.home.SingleBuyNow.BuyNowFragment;

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
import io.reactivex.disposables.CompositeDisposable;
import me.ibrahimsn.lib.NiceBottomBar;
import me.ibrahimsn.lib.OnItemSelectedListener;

public class HomeActivity extends AppCompatActivity {

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
        ordersViewModel = ViewModelProviders.of(this).get(OrdersViewModel.class);
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(this).cartDao());
        HomeFragment homeFragment = new HomeFragment();
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
                        HomeFragment homeFragment = new HomeFragment();
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
            ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
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
            HomeFragment homeFragment = new HomeFragment();
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
            HomeFragment homeFragment = new HomeFragment();
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
                                                        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
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
                                    ProductDetailsFragment productsFragment = new ProductDetailsFragment();
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


}