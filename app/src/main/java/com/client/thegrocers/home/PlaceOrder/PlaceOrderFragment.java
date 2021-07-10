package com.client.thegrocers.home.PlaceOrder;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.client.thegrocers.Adapters.PlaceOrderAdapter;
import com.client.thegrocers.Callbacks.ICurrentFragment;
import com.client.thegrocers.Callbacks.ILoadTimeFromFirebaseListener;
import com.client.thegrocers.Common.Common;
import com.client.thegrocers.Database.CartDataSource;
import com.client.thegrocers.Database.CartDatabase;
import com.client.thegrocers.Database.CartItem;
import com.client.thegrocers.Database.LocalCartDataSource;
import com.client.thegrocers.EventBus.AfterOrderPlaced;
import com.client.thegrocers.EventBus.CartClearedInPlaceOrder;
import com.client.thegrocers.EventBus.CounterCartEvent;
import com.client.thegrocers.EventBus.HideFABCart;
import com.client.thegrocers.EventBus.UpdateItemInCart;
import com.client.thegrocers.Model.CouponModel;
import com.client.thegrocers.Model.Order;
import com.client.thegrocers.Model.ShipCred;
import com.client.thegrocers.Payment.PaymentGatewayActivity;
import com.client.thegrocers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

public class PlaceOrderFragment extends Fragment implements ILoadTimeFromFirebaseListener {

    Unbinder unbinder;
    @BindView(R.id.items_list_rv_po)
    RecyclerView itemsListRv;
    @BindView(R.id.delete_all_items_civ_po)
    CircleImageView deleteAllItemsCiv;
    @OnClick(R.id.delete_all_items_civ_po)
    void deleteItems(){
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
                        EventBus.getDefault().postSticky(new CartClearedInPlaceOrder(true));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }
    @BindView(R.id.subtotal_tv_po)
    TextView subtotalTv;
    @BindView(R.id.delivery_charges_tv_po)
    TextView deliveryChargesTv;
    @BindView(R.id.total_tv_po)
    TextView totalTv;
    @BindView(R.id.cod_radio_btn_po)
    RadioButton codBtn;
    @BindView(R.id.pay_now_radio_btn_po)
    RadioButton payNowBtn;
    private String total,deliveryCharges;
    @BindView(R.id.no_of_items_tv_po)
    TextView no_of_items_tv;

    @BindView(R.id.edt_special_instruction)
    EditText edtSpecialInstruction;

    @BindView(R.id.apply_coupon_edt)
    MaterialEditText applyCouponEdt;

    @BindView(R.id.apply_coupon_btn)
    Button applyCouponBtn;

    @BindView(R.id.discount_tv_po)
    TextView discountTv;

    double priceAfterAppliedCoupon;

    private String email,password;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PlaceOrderViewModel placeOrderViewModel;

    private CartDataSource cartDataSource;
    private Parcelable recyclerViewState;
    private PlaceOrderAdapter adapter;
    private ILoadTimeFromFirebaseListener listener;

    private AlertDialog placingOrderDialog;

    private CouponModel appliedCoupon;

    @BindView(R.id.place_order_btn_po)
    Button placeOrderBtn;
    @OnClick(R.id.place_order_btn_po)
    void PlaceOrder(){
        CheckProcess();
    }

    private void CheckProcess() {
        if (codBtn.isChecked()){
            startPlaceOrderWithCod();
        }else if (payNowBtn.isChecked()){
            startOnlinePaymentProcess();
        }else {
            Toast.makeText(getContext(), "Please select a payment option ", Toast.LENGTH_SHORT).show();
        }
    }

    private void startOnlinePaymentProcess() {
        placingOrderDialog.show();
        compositeDisposable.add(cartDataSource.getAllCart(Common.currentUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems ->
                        cartDataSource.sumPriceInCart(Common.currentUser.getUid())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Double>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Double totalPrice) {

                                Order order = new Order();
                                order.setUserId(Common.currentUser.getUid());
                                order.setUserName(Common.currentUser.getName());
                                order.setUserPhone(Common.currentUser.getPhone());
                                order.setUserEmail(Common.currentUser.getEmail());
                                order.setCartItemList(cartItems);
                                if (appliedCoupon != null){
                                    order.setTotalPayment(priceAfterAppliedCoupon);
                                    order.setFinalPayment(priceAfterAppliedCoupon);
                                }else
                                if (totalPrice < 1000){
                                    deliveryCharges = "50";
                                    order.setTotalPayment(totalPrice + Double.parseDouble(deliveryCharges));
                                    order.setFinalPayment(totalPrice + Double.parseDouble(deliveryCharges));
                                }else {
                                    order.setTotalPayment(totalPrice);
                                    order.setFinalPayment(totalPrice);
                                }

                                order.setDiscount(0);
                                order.setAddress(Common.addressSelectedForDelivery);
                                order.setCod(false);
                                order.setTransactionId("Prepaid");

                                order.setSpecialInstructions(edtSpecialInstruction.getText().toString());
                                syncLocalTimeWithGlobalTimeForOnlinePayment(order);

                            }
                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e);
                            }
                        }), Timber::e
                ));
    }

    private void syncLocalTimeWithGlobalTimeForOnlinePayment(Order order) {
        final DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        offsetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long offset = snapshot.getValue(Long.class);
                long estimatedServerTimeMs = System.currentTimeMillis() + offset;
                order.setCreateDate(estimatedServerTimeMs);
                order.setOrderStatus(0);
                placingOrderDialog.dismiss();
//                EventBus.getDefault().postSticky(new StartPaymentGateway(true,order));
                Common.PaymentGatewayOrderDetails = order;
                if (order.getCreateDate() != null){
                    startActivity(new Intent(getContext(), PaymentGatewayActivity.class));
                }else {
                    Toast.makeText(getContext(), "Error creating payment please try later again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to get time , Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startPlaceOrderWithCod() {
        placingOrderDialog.show();
        compositeDisposable.add(cartDataSource.getAllCart(Common.currentUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CartItem>>() {
                               @Override
                               public void accept(List<CartItem> cartItems) throws Exception {
                                   cartDataSource.sumPriceInCart(Common.currentUser.getUid())
                                           .subscribeOn(Schedulers.io())
                                           .observeOn(AndroidSchedulers.mainThread())
                                           .subscribe(new SingleObserver<Double>() {
                                               @Override
                                               public void onSubscribe(Disposable d) {

                                               }

                                               @Override
                                               public void onSuccess(Double totalPrice) {
                                                   Order order = new Order();
                                                   order.setUserId(Common.currentUser.getUid());
                                                   order.setUserName(Common.currentUser.getName());
                                                   order.setUserPhone(Common.currentUser.getPhone());
                                                   order.setCartItemList(cartItems);
                                                   if (appliedCoupon != null){
                                                       order.setTotalPayment(priceAfterAppliedCoupon);
                                                       order.setFinalPayment(priceAfterAppliedCoupon);
                                                   }else
                                                   if (totalPrice < 1000){
                                                       deliveryCharges = "50";
                                                       order.setTotalPayment(totalPrice + Double.parseDouble(deliveryCharges));
                                                       order.setFinalPayment(totalPrice + Double.parseDouble(deliveryCharges));
                                                   }else {
                                                       order.setTotalPayment(totalPrice);
                                                       order.setFinalPayment(totalPrice);
                                                   }


                                                   if (!edtSpecialInstruction.getText().toString().equals("")){
                                                       order.setSpecialInstructions(edtSpecialInstruction.getText().toString());
                                                   }

                                                   order.setDiscount(0);
                                                   order.setAddress(Common.addressSelectedForDelivery);
                                                   order.setCod(true);
                                                   order.setTransactionId("Cash On Delivery");
                                                   Common.orderPlacedViaCod = order;
                                                   syncLocalTimeWithGlobalTime(order);
                                               }


                                               @Override
                                               public void onError(Throwable e) {
                                                   Timber.e(e);
                                               }
                                           });
                               }
                           },throwable ->{
                             Timber.e(throwable);
                        }
                ));
    }

    private void syncLocalTimeWithGlobalTime(Order order) {
        final DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        offsetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long offset = snapshot.getValue(Long.class);
                long estimatedServerTimeMs = System.currentTimeMillis() + offset;
//                SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy HH:mm");
//                Date resultDate = new Date(estimatedServerTimeMs);

                listener.onLoadTImeSuccess(order,estimatedServerTimeMs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onLoadTimeFailed(error.getMessage());
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        placeOrderViewModel = ViewModelProviders.of(this).get(PlaceOrderViewModel.class);
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);
        unbinder = ButterKnife.bind(this,view);
        Common.CurrentFragment = "PlaceOrder";
        ICurrentFragment iCurrentFragment  = (ICurrentFragment) getContext();
        iCurrentFragment.currentFragment("Other");
        listener = this;
        initViews();
        initButtons();
        return view;
    }

    private void initButtons() {
        applyCouponBtn.setOnClickListener(v -> {
            if (applyCouponEdt.getText().toString().equals("")){
                Snackbar.make(v,"Enter coupon code to apply",Snackbar.LENGTH_SHORT).show();
            }else {
                FirebaseDatabase.getInstance().getReference(Common.COUPON_CODES_REF)
                        .child(applyCouponEdt.getText().toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    appliedCoupon = snapshot.getValue(CouponModel.class);
                                    calculateTotalPrice();
                                }else {
                                    Snackbar.make(v,"Invalid coupon code",Snackbar.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
            }
        });
    }


    private void initViews() {
        placingOrderDialog = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        placingOrderDialog.setMessage("Placing order please wait...");
        placeOrderViewModel.initCartDataSource(getContext());
        placeOrderViewModel.getMutableLiveDataCartItems().observe(getViewLifecycleOwner(), new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                    adapter = new PlaceOrderAdapter(getContext(),cartItems);
                    itemsListRv.setAdapter(adapter);
                    no_of_items_tv.setText(new StringBuilder("").append(cartItems.size()).append(" items"));
            }
        });

        setHasOptionsMenu(true);
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDao());
        calculateTotalPrice();
        itemsListRv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        itemsListRv.setLayoutManager(layoutManager);
        itemsListRv.addItemDecoration(new DividerItemDecoration(getContext(),layoutManager.getOrientation()));
    }

    private void writeOrderToFirebase(Order order) {
        FirebaseDatabase.getInstance()
                .getReference(Common.ONGOING_ORDERS_REF)
                .child(Common.createOrderNumber())
                .setValue(order)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.e(e);
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                cartDataSource.cleanCart(Common.currentUser.getUid())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Integer integer) {
                                placingOrderDialog.dismiss();
                                EventBus.getDefault().postSticky(new AfterOrderPlaced(true));
                                EventBus.getDefault().postSticky(new CounterCartEvent(true));
                            }

                            @Override
                            public void onError(Throwable e) {
                            Timber.e(e);
                            }
                        } ) ;
            }
        });
    }

    @Override
    public void onLoadTImeSuccess(Order order, long estimateTimeInMs) {
        order.setCreateDate(estimateTimeInMs);
        order.setOrderStatus(0);
        writeOrderToFirebase(order);
    }

    @Override
    public void onLoadTimeFailed(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        placeOrderViewModel.onStop();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
            compositeDisposable.clear();
        }
        super.onStop();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onUpdateItemInCartEvent(UpdateItemInCart event){
        if (event.getCartItem() != null){
            recyclerViewState =itemsListRv.getLayoutManager().onSaveInstanceState();
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
                            itemsListRv.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e);
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
                        subtotalTv.setText(new StringBuilder("")
                                .append(Common.formatPrice(aLong)));
                        if (aLong < 1000){
                            deliveryCharges = "50";
                            double atotal = aLong + Double.parseDouble(deliveryCharges);
                            if (appliedCoupon != null){
                                double p = Double.parseDouble(String.valueOf(appliedCoupon.getPercent()));
                                discountTv.setText(new StringBuilder("-").append((p*atotal)/100));
                                atotal = atotal - ((p*atotal)/100);
                                priceAfterAppliedCoupon =atotal;
                            }
                            total = String.valueOf(atotal);
                        }else {
                            deliveryCharges = "0";
                            double atotal = aLong + Double.parseDouble(deliveryCharges);
                            if (appliedCoupon != null){
                                double p = Double.parseDouble(String.valueOf(appliedCoupon.getPercent()));
                                discountTv.setText(new StringBuilder("-").append((p*atotal)/100));
                                atotal = atotal - ((p*atotal)/100);
                                priceAfterAppliedCoupon= atotal;
                            }
                            total = String.valueOf(atotal);
                        }

                        totalTv.setText(total);
                        deliveryChargesTv.setText(deliveryCharges);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                }  );
    }

//    private  class SendOrderToShipRocket extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            JSONObject jsonObjectServ = new JSONObject();
//            try {
//                jsonObjectServ.put("email",email);
//                jsonObjectServ.put("password",password);
//                String token ;
//                OkHttpClient client = new OkHttpClient().newBuilder()
//                        .build();
//                MediaType mediaType = MediaType.parse("application/json");
//                RequestBody body = RequestBody.create(jsonObjectServ.toString(),mediaType);
//                Request request = new Request.Builder()
//                        .url("https://apiv2.shiprocket.in/v1/external/auth/login")
//                        .method("POST", body)
//                        .addHeader("Content-Type", "application/json")
//                        .build();
//                try {
//                    Response response = client.newCall(request).execute();
//                    String resStr = response.body().string();
//                    JSONObject json = new JSONObject(resStr);
//                    token = json.getString("token");
//                    Log.d("DATTFDGDHKSHVJD",json.toString());
//
//                    if (token != null){
//                        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
//
//                        int currentYear = calendar.get(Calendar.YEAR);
//                        int currentMonth = calendar.get(Calendar.MONTH ) + 1;
//                        if (currentMonth <=9)
//                            currentMonth = Integer.parseInt("0"+currentMonth);
//
//                        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
//                        if (currentDay <=9)
//                            currentDay = Integer.parseInt("0" + currentDay);
//                        String todaysDate = currentDay+"-"+currentMonth+"-"+currentYear;
//                        JSONObject jsonObject = new JSONObject();
//                        Random random = new Random();
//                        int theorderId = random.nextInt(999999);
//                        String orderId = String.valueOf(theorderId);
//                        try {
//                            jsonObject.put("order_id", orderId);
//                            jsonObject.put("order_date", todaysDate);
//                            jsonObject.put("pickup_location", "Store");
//                            jsonObject.put("channel_id", "");
//                            jsonObject.put("comment", "");
//                            jsonObject.put("reseller_name", "Yuvraj Grocery");
//                            jsonObject.put("company_name", "");
//                            jsonObject.put("billing_customer_name", Common.orderPlacedViaCod.getUserName());
//                            jsonObject.put("billing_last_name", Common.currentUser.getLastName());
//                            jsonObject.put("billing_address", Common.orderPlacedViaCod.getAddress().getAddress());
//                            jsonObject.put("billing_address_2", Common.orderPlacedViaCod.getAddress().getStreetAddress());
//                            jsonObject.put("billing_isd_code", "");
//                            jsonObject.put("billing_city", Common.orderPlacedViaCod.getAddress().getCity());
//                            jsonObject.put("billing_pincode", Common.orderPlacedViaCod.getAddress().getPincode());
//                            jsonObject.put("billing_state", Common.orderPlacedViaCod.getAddress().getState());
//                            jsonObject.put("billing_country", "India");
//                            jsonObject.put("billing_email", "yuvrajgrocery@gmail.com");
//                            String subS = Common.orderPlacedViaCod.getUserPhone().substring(Common.orderPlacedViaCod.getUserPhone().length() - 10);
//                            jsonObject.put("billing_phone", subS);
//                            jsonObject.put("billing_alternate_phone", "");
//                            jsonObject.put("shipping_is_billing", "1");
//                            jsonObject.put("shipping_customer_name", "");
//                            jsonObject.put("shipping_last_name", "");
//                            jsonObject.put("shipping_address", "");
//                            jsonObject.put("shipping_address_2", "");
//                            jsonObject.put("shipping_city", "");
//                            jsonObject.put("shipping_pincode", "");
//                            jsonObject.put("shipping_country", "");
//                            jsonObject.put("shipping_state", "");
//                            jsonObject.put("shipping_email", "");
//                            jsonObject.put("shipping_phone", "");
//                            JSONArray jsonArray = new JSONArray();
//
//                            for (int i = 0;i<Common.orderPlacedViaCod.getCartItemList().size() ; i++){
//                                Random random1 = new Random();
//                                String randSKu = String.valueOf(random1.nextInt(99999) +i);
//                                JSONObject orderJson = new JSONObject();
//                                orderJson.put("name",Common.orderPlacedViaCod.getCartItemList().get(i).getProductName());
//                                orderJson.put("sku",randSKu+Common.orderPlacedViaCod.getCartItemList().get(i).getProductId());
//                                orderJson.put("units",Common.orderPlacedViaCod.getCartItemList().get(i).getProductQuantity());
//                                orderJson.put("selling_price",Common.orderPlacedViaCod.getCartItemList().get(i).getProductSellingPrice());
//                                orderJson.put("discount","0");
//                                orderJson.put("tax","0");
//                                orderJson.put("hsn","");
//                                jsonArray.put(orderJson);
//                            }
//                            jsonObject.put("order_items", jsonArray);
//                            jsonObject.put("payment_method", "COD");
//                            jsonObject.put("giftwrap_charges", "0");
//                            jsonObject.put("transaction_charges", "0");
//                            jsonObject.put("total_discount", "0");
//                            jsonObject.put("sub_total", Common.orderPlacedViaCod.getTotalPayment());
//                            jsonObject.put("length", String.valueOf(Common.orderPlacedViaCod.getFinalLength()));
//                            jsonObject.put("breadth", String.valueOf(Common.orderPlacedViaCod.getFinalBreadth()));
//                            jsonObject.put("height", String.valueOf(Common.orderPlacedViaCod.getFinalHeight()));
//                            jsonObject.put("weight", String.valueOf(Common.orderPlacedViaCod.getFinalWeight()/1000f));
//                            jsonObject.put("ewaybill_no", "");
//                            jsonObject.put("customer_gstin", "");
//
//                            Log.d("THE OBJECT",jsonObject.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        OkHttpClient client2 = new OkHttpClient().newBuilder()
//                                .build();
//                        MediaType mediaType2 = MediaType.parse("application/json");
//                        RequestBody body2 = RequestBody.create(jsonObject.toString(),mediaType2);
//                        Request request2 = new Request.Builder()
//                                .url("https://apiv2.shiprocket.in/v1/external/orders/create/adhoc")
//                                .method("POST", body2)
//                                .addHeader("Content-Type", "application/json")
//                                .addHeader("Authorization", "Bearer "+token)
//                                .build();
//                        try {
//                            Response response2= client2.newCall(request2).execute();
//                            String resStr2 = response2.body().string();
//                            JSONObject json2 = new JSONObject(resStr2);
//                            Log.d("DATTFDGDHKSHVJD",json2.toString());
//                            if (json2 != null){
//
//                            }
//
//                        } catch (IOException | JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        ///// Send Order To ShipRocket /////
//                    }
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            placingOrderDialog.dismiss();
//        }
//    }



}