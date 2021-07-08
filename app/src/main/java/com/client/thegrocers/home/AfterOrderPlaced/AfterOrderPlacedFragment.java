package com.client.thegrocers.home.AfterOrderPlaced;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.client.thegrocers.Callbacks.ICurrentFragment;
import com.client.thegrocers.Common.Common;
import com.client.thegrocers.Database.CartDataSource;
import com.client.thegrocers.Database.CartDatabase;
import com.client.thegrocers.Database.LocalCartDataSource;
import com.client.thegrocers.EventBus.GoToHomeFragment;
import com.client.thegrocers.EventBus.OnlinePaymentSuccessFull;
import com.client.thegrocers.Model.SMSClientNew;
import com.client.thegrocers.Model.ShipCred;
import com.client.thegrocers.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

public class AfterOrderPlacedFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.continue_shooping_button)
    Button contiue_shooping;
    @BindView(R.id.success_dialog_of_order)
     LinearLayout linearLayoutSuccessDialog;
    @OnClick(R.id.continue_shooping_button)
    public void GoToHome(){
        EventBus.getDefault().postSticky(new GoToHomeFragment(true));
    }
    private CartDataSource cartDataSource;
    AlertDialog alertDialog;

    private String USERNAME,ORDERNUMBER;

    ///// MESSAGE FOR USER //////
    private String Message = "Thank you for shopping with us. Your order has been placed and will be delivered to you soon";
    private String PhoneNumber = Common.currentUser.getPhone();
    private String SenderId = "RANSTP";
    private String RouteId = "3";
    private String SMSContentType = "english";
    private String AuthKey;
    private Response response;
    private String url;
    private OkHttpClient client = new OkHttpClient.Builder().connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT,ConnectionSpec.MODERN_TLS)).build();
    private SMSClientNew smsClient ;
    ///// MESSAGE FOR USER //////

    ///// MESSAGE FOR ADMIN //////
    private String Message2 = "A new order has been placed by "+ Common.currentUser.getPhone();
    private String PhoneNumber2 = "7317092222";
    private String SenderId2 = "RANSTP";
    private String RouteId2 = "3";
    private String SMSContentType2 = "english";
    private String AuthKey2;
    private Response response2;
    private String url2;
    private OkHttpClient client2 =  new OkHttpClient.Builder().connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT,ConnectionSpec.MODERN_TLS)).build();
    private SMSClientNew smsClient2 ;
    ///// MESSAGE FOR ADMIN /////

    /////EMAIL ////
    private String EmailAuthKey;
    //// EMAIL ////

    private String email,password;

    private AlertDialog pdLoading;


    private double PRRRRIIICCEEE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_after_order_placed, container, false);
        unbinder = ButterKnife.bind(this,view);
        alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        smsClient = new SMSClientNew(getContext());
        Common.CurrentFragment = "AfterOrderPlaced";
        ICurrentFragment iCurrentFragment  = (ICurrentFragment) getContext();
        iCurrentFragment.currentFragment("Other");
        EventBus.getDefault().removeStickyEvent(OnlinePaymentSuccessFull.class);
        smsClient2 = new SMSClientNew(getContext());
        pdLoading = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDao());
        if (Common.PaymentGatewayOrderDetails != null){
            PRRRRIIICCEEE = Common.PaymentGatewayOrderDetails.getFinalPayment();
            uploadData();

        }else if (Common.orderPlacedViaCod != null){
            PRRRRIIICCEEE = Common.orderPlacedViaCod.getFinalPayment();
            USERNAME = Common.orderPlacedViaCod.getUserName();
            ORDERNUMBER = Common.orderPlacedViaCod.getOrderNumber();
                FirebaseDatabase.getInstance().getReference("SmsAuthKey")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                AuthKey = snapshot.getValue(String.class);
                                AuthKey2 = snapshot.getValue(String.class);

                                smsClient = new SMSClientNew(getContext());
                                smsClient2 = new SMSClientNew(getContext());
                                pdLoading = new ProgressDialog(getContext());
                                pdLoading.setMessage("Finishing up...");
                                pdLoading.show();
//                                FirebaseDatabase.getInstance().getReference("EmailAuthKey")
//                                        .addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                if (snapshot.exists()){
//                                                    EmailAuthKey = snapshot.getValue(String.class);
//                                                    new SendEmailToUserAndServer().execute();
//                                                }
//                                            }
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                            }
//                                        });
                                if (AuthKey != null && AuthKey2 != null){
                                    new SendSmsToUserAndServer().execute();
                                    new SendSmsToServer().execute();
                                    Common.orderPlacedViaCod =null;
                                }

//                                if (AuthKey != null && AuthKey2 != null){
//                                    pdLoading.setMessage("Finishing up...");
//                                    pdLoading.show();
//                                    new SendSmsToUserAndServer().execute();
//                                    new SendSmsToServer().execute();
//                                }else {
//                                    Toast.makeText(getContext(), "Auth key is null", Toast.LENGTH_SHORT).show();
//                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), ""+error.getDetails(), Toast.LENGTH_SHORT).show();
                            }
                        });
        }
        return view;
    }

    private void uploadData() {
        alertDialog.setMessage("Placing Order ");
        alertDialog.show();
        String ordoNo = Common.createOrderNumber();
        Common.PaymentGatewayOrderDetails.setOrderNumber(ordoNo);
        USERNAME = Common.PaymentGatewayOrderDetails.getUserName();
        ORDERNUMBER = Common.PaymentGatewayOrderDetails.getOrderNumber();
        FirebaseDatabase.getInstance()
                .getReference(Common.ORDER_REF)
                .child(ordoNo)
                .setValue(Common.PaymentGatewayOrderDetails)
                .addOnFailureListener(e -> Timber.e(e)).addOnCompleteListener(task ->
                cartDataSource.cleanCart(Common.currentUser.getUid())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Integer integer) {
                                Toast.makeText(getContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                FirebaseDatabase.getInstance().getReference("SmsAuthKey")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                AuthKey = snapshot.getValue(String.class);
                                                AuthKey2 = snapshot.getValue(String.class);
                                                smsClient = new SMSClientNew(getContext());
                                                smsClient2 = new SMSClientNew(getContext());
                                                pdLoading = new ProgressDialog(getContext());
                                                pdLoading.setMessage("Finishing up...");
                                                pdLoading.show();
                                                if (AuthKey != null && AuthKey2 != null){
                                                    new SendSmsToUserAndServer().execute();
                                                    new SendSmsToServer().execute();
                                                    Common.PaymentGatewayOrderDetails = null;
                                                }
//                                                FirebaseDatabase.getInstance().getReference("EmailAuthKey")
//                                                        .addValueEventListener(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                                if (snapshot.exists()){
//                                                                    EmailAuthKey = snapshot.getValue(String.class);
//                                                                    new SendEmailToUserAndServer().execute();
//                                                                }
//                                                            }
//                                                            @Override
//                                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                                            }
//                                                        });


                                                alertDialog.dismiss();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                alertDialog.dismiss();
                                                Toast.makeText(getContext(), ""+error.getDetails(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                alertDialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                alertDialog.dismiss();
                                Timber.e(e);
                            }
                        } ));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



//



    class SendSmsToUserAndServer extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get the string by calling the get url and add the user info //

            url = "https://www.smsgatewayhub.com/api/mt/SendSMS?APIKey=ubD43nCqMUOXfeLKYV4oLQ&senderid=EDEZIP&channel=2&DCS=0&flashsms=0&number=91"+PhoneNumber+"&text=Dear%20"+USERNAME+",%20"+PhoneNumber+"%20Your%20Order%20has%20been%20Confirmed%20Order%20No%20is%20"+ORDERNUMBER+"%20%26%20you%20would%20be%20receive%20dispatch%20status%20soon.%20Ezip%20Info%20Tech%20Pvt%20Ltd&route=1\n";

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Cache-Control", "no-cache")
                    .build();

            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                Log.e("Error", "", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }

    class SendSmsToServer extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            url = "https://www.smsgatewayhub.com/api/mt/SendSMS?APIKey=ubD43nCqMUOXfeLKYV4oLQ&senderid=EDEZIP&channel=2&DCS=0&flashsms=0&number=91"+PhoneNumber2+"&text=Dear%20"+USERNAME+",%20"+PhoneNumber+"%20Your%20Order%20has%20been%20Confirmed%20Order%20No%20is%20"+ORDERNUMBER+"%20%26%20you%20would%20be%20receive%20dispatch%20status%20soon.%20Ezip%20Info%20Tech%20Pvt%20Ltd&route=1\n";

            Request request2 = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Cache-Control", "no-cache")
                    .build();
            try {
                response2 = client2.newCall(request2).execute();
            } catch (IOException e) {
                Log.e("Error", "", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(getContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
            linearLayoutSuccessDialog.setVisibility(View.VISIBLE);
            pdLoading.dismiss();
        }
    }

}