package com.client.thegrocers.home.Orders;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuvraj.thegroceryapp.Adapters.OrdersAdapter;
import com.yuvraj.thegroceryapp.Callbacks.ILoadOrderCallbackListener;
import com.yuvraj.thegroceryapp.Common.Common;
import com.yuvraj.thegroceryapp.EventBus.OrderRequestForCancel;
import com.yuvraj.thegroceryapp.EventBus.OrderRequestForReturn;
import com.yuvraj.thegroceryapp.Model.Order;
import com.yuvraj.thegroceryapp.Model.SMSClientNew;
import com.yuvraj.thegroceryapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrdersFragment extends Fragment implements ILoadOrderCallbackListener {

    @BindView(R.id.recycler_view_order)
    RecyclerView recycler_orders;

    private Unbinder unbinder;
    private OrdersViewModel ordersViewModel;
    private AlertDialog dialog;
    private ILoadOrderCallbackListener loadOrderCallbackListener;
    private boolean cameFromChild = false;
    @BindView(R.id.orders_empty_info)
    RelativeLayout empty_anims;
    private String Message;
    private String PhoneNumber ;
    private String SenderId = "RANSTP";
    private String RouteId = "3";
    private String SMSContentType = "english";
    private String AuthKey;
    private Response response;
    private String url;
    private OkHttpClient client = new OkHttpClient();
    private SMSClientNew smsClient ;
    private AlertDialog cancelDialog;
    private AlertDialog returnDialog;
    private String Message2;
    private String PhoneNumber2;
    private String SenderId2 = "RANSTP";
    private String RouteId2 = "3";
    private String SMSContentType2 = "english";
    private String AuthKey2;
    private Response response2;
    private String url2;
    private OkHttpClient client2 = new OkHttpClient();
    private SMSClientNew smsClient2 ;

    private String Message2A;
    private String PhoneNumber2A = "9897455133";
    private String SenderId2A = "RANSTP";
    private String RouteId2A = "3";
    private String SMSContentType2A = "english";
    private Response response2A;
    private String url2A;
    private OkHttpClient client2A = new OkHttpClient();
    private SMSClientNew smsClient2A ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        ordersViewModel = ViewModelProviders.of(this).get(OrdersViewModel.class);
        Common.CurrentFragment = "Orders";
        unbinder = ButterKnife.bind(this,view);
        initViews(view);
        if (Common.currentUser != null){
            PhoneNumber = Common.currentUser.getPhone();
            PhoneNumber2 = Common.currentUser.getPhone();
            loadOrdersFromFirebase();
        }

        if (Common.currentUser != null){
            ordersViewModel.getMutableLiveDataOrderList().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
                @Override
                public void onChanged(List<Order> orders) {
                    if (orders != null || !orders.isEmpty()){
                        recycler_orders.setVisibility(View.VISIBLE);
                        empty_anims.setVisibility(View.GONE);
                        OrdersAdapter adapter = new OrdersAdapter(getContext(),orders);
                        recycler_orders.setAdapter(adapter);
                        recycler_orders.smoothScrollToPosition(orders.size()-1);
                    }else {
                        recycler_orders.setVisibility(View.GONE);
                        empty_anims.setVisibility(View.VISIBLE);
                    }

                }
            });
        }else {
            recycler_orders.setVisibility(View.GONE);
            empty_anims.setVisibility(View.VISIBLE);
        }
        return view;
    }


    private void loadOrdersFromFirebase() {
        List<Order> orderList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.ORDER_REF)
                .orderByChild("userId")
                .equalTo(Common.currentUser.getUid())
                .limitToLast(100)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                            Order order = itemSnapshot.getValue(Order.class);
                            order.setOrderNumber(itemSnapshot.getKey());
                            orderList.add(order);
                        }
                        loadOrderCallbackListener.onLoadOrderSucess(orderList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadOrderCallbackListener.onLoadOrderFailed(error.getMessage());
                    }
                });
    }

    private void initViews(View view) {
        smsClient = new SMSClientNew(getContext());
        smsClient2 = new SMSClientNew(getContext());
        smsClient2A  = new SMSClientNew(getContext());
        loadOrderCallbackListener = this;
        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        cancelDialog = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        returnDialog = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        recycler_orders.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,true);
        recycler_orders.setLayoutManager(layoutManager);

        recycler_orders.addItemDecoration(new DividerItemDecoration(getContext(),layoutManager.getOrientation()));


    }

    @Override
    public void onLoadOrderSucess(List<Order> orderList) {
        dialog.dismiss();
        ordersViewModel.setMutableLiveDataOrderList(orderList);
    }

    @Override
    public void onLoadOrderFailed(String message) {
        dialog.dismiss();
        Toast.makeText(getContext(), "Loading Failed", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onReturnPlaced(OrderRequestForReturn event){
        if (event.isSuccess()){
            returnDialog.setMessage("Processing Request ...");
            returnDialog.show();
            FirebaseDatabase.getInstance().getReference(Common.ORDER_REF)
                    .child(event.getOrder().getOrderNumber())
                    .child("orderStatus")
                    .setValue(4)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            returnDialog.dismiss();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Message2 =  "Dear "+Common.currentUser.getName() +" your order "+event.getOrder().getOrderNumber()+" has been requested for return";
                        FirebaseDatabase.getInstance().getReference("SmsAuthKey")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        AuthKey2 = snapshot.getValue(String.class);

                                            new SendReturnSmsToUser().execute();
                                            loadOrdersFromFirebase();
                                            returnDialog.dismiss();
                                            EventBus.getDefault().removeStickyEvent(OrderRequestForReturn.class);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getContext(), ""+error.getDetails(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onOrderCancelled(OrderRequestForCancel event){
        if (event.isSuccess()){
            cancelDialog.setMessage("Processing Request ...");
            cancelDialog.show();
            FirebaseDatabase.getInstance().getReference(Common.ORDER_REF)
                    .child(event.getOrder().getOrderNumber())
                    .child("orderStatus")
                    .setValue(-1)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Message =  "Dear "+Common.currentUser.getName() +" your order "+event.getOrder().getOrderNumber()+" has been cancelled";
                        FirebaseDatabase.getInstance().getReference("SmsAuthKey")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        AuthKey = snapshot.getValue(String.class);
                                            new SendCancelSmsToUser().execute();
                                            loadOrdersFromFirebase();

                                        EventBus.getDefault().removeStickyEvent(OrderRequestForCancel.class);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getContext(), ""+error.getDetails(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        }
    }

    class  SendReturnSmsToUser extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            url2 = smsClient2.getUrl(AuthKey2,Message2,SenderId2,PhoneNumber2,RouteId2,SMSContentType2);
            Request request2 = new Request.Builder()
                    .url(url2)
                    .get()
                    .addHeader("Cache-Control", "no-cache")
                    .build();

            try {
                response2 = client2.newCall(request2).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new SendReturnSmsToAdmin().execute();

        }
    }


    class SendReturnSmsToAdmin extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Message2A = "Order Return Request :  by "+ Common.currentUser.getPhone();
            url2A = smsClient2A.getUrl(AuthKey2,Message2A,SenderId2A,PhoneNumber2A,RouteId2A,SMSContentType2A);
            Request request2A = new Request.Builder()
                    .url(url2A)
                    .get()
                    .addHeader("Cache-Control", "no-cache")
                    .build();

            try {
                response2A = client2A.newCall(request2A).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getContext(), "Order requested for return successfully", Toast.LENGTH_SHORT).show();
            returnDialog.dismiss();
        }
    }


    class SendCancelSmsToUser extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            url = smsClient.getUrl(AuthKey,Message,SenderId,PhoneNumber,RouteId,SMSContentType);
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Cache-Control", "no-cache")
                    .build();

            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getContext(), "Order Cancelled Successfully", Toast.LENGTH_SHORT).show();
            cancelDialog.dismiss();
        }
    }

}