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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuvraj.thegroceryapp.Common.Common;
import com.yuvraj.thegroceryapp.Database.CartDataSource;
import com.yuvraj.thegroceryapp.Database.CartDatabase;
import com.yuvraj.thegroceryapp.Database.LocalCartDataSource;
import com.yuvraj.thegroceryapp.EventBus.GoToHomeFragment;
import com.yuvraj.thegroceryapp.EventBus.OnlinePaymentSuccessFull;
import com.yuvraj.thegroceryapp.Model.SMSClientNew;
import com.yuvraj.thegroceryapp.Model.ShipCred;
import com.yuvraj.thegroceryapp.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    ///// MESSAGE FOR USER //////
    private String Message = "Thank you for shopping with us. Your order has been placed and will be delivered to you soon";
    private String PhoneNumber = Common.currentUser.getPhone();
    private String SenderId = "RANSTP";
    private String RouteId = "3";
    private String SMSContentType = "english";
    private String AuthKey;
    private Response response;
    private String url;
    private OkHttpClient client = new OkHttpClient();
    private SMSClientNew smsClient ;
    ///// MESSAGE FOR USER //////

    ///// MESSAGE FOR ADMIN //////
    private String Message2 = "A new order has been placed by "+ Common.currentUser.getPhone();
    private String PhoneNumber2 = "6000280524";
    private String SenderId2 = "RANSTP";
    private String RouteId2 = "3";
    private String SMSContentType2 = "english";
    private String AuthKey2;
    private Response response2;
    private String url2;
    private OkHttpClient client2 = new OkHttpClient();
    private SMSClientNew smsClient2 ;
    ///// MESSAGE FOR ADMIN /////

    private String email,password;

    private AlertDialog pdLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_after_order_placed, container, false);
        unbinder = ButterKnife.bind(this,view);
        alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        smsClient = new SMSClientNew(getContext());
        Common.CurrentFragment = "AfterOrderPlaced";
        EventBus.getDefault().removeStickyEvent(OnlinePaymentSuccessFull.class);
        smsClient2 = new SMSClientNew(getContext());
        pdLoading = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDao());
        if (Common.PaymentGatewayOrderDetails != null){
            uploadData();
        }else if (Common.orderPlacedViaCod != null){
                FirebaseDatabase.getInstance().getReference("SmsAuthKey")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                AuthKey = snapshot.getValue(String.class);
                                AuthKey2 = snapshot.getValue(String.class);
                                Common.orderPlacedViaCod =null;
                                if (AuthKey != null && AuthKey2 != null){
                                    pdLoading.setMessage("Finishing up...");
                                    pdLoading.show();
                                    new SendSmsToUserAndServer().execute();
                                    new SendSmsToServer().execute();
                                }else {
                                    Toast.makeText(getContext(), "Auth key is null", Toast.LENGTH_SHORT).show();
                                }
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
        FirebaseDatabase.getInstance()
                .getReference(Common.ORDER_REF)
                .child(Common.createOrderNumber())
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
                                                FirebaseDatabase.getInstance().getReference("ShiproketCred")
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                for (DataSnapshot item :snapshot.getChildren()){
                                                                    ShipCred cred = item.getValue(ShipCred.class);
                                                                    email = cred.getEmail();
                                                                    password = cred.getPassword();
                                                                }
                                                                if (email!= null && password != null){
                                                                        new SendOrderToShipRocket().execute();
                                                                        pdLoading.setMessage("Finishing up...");
                                                                        pdLoading.show();
                                                                        new SendSmsToUserAndServer().execute();
                                                                        new SendSmsToServer().execute();
                                                                    }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                alertDialog.dismiss();
                                                            }
                                                        });
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

    private  class SendOrderToShipRocket extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JSONObject jsonObjectServ = new JSONObject();
            try {
                jsonObjectServ.put("email",email);
                jsonObjectServ.put("password",password);
                String token ;
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(jsonObjectServ.toString(),mediaType);
                Request request = new Request.Builder()
                        .url("https://apiv2.shiprocket.in/v1/external/auth/login")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String resStr = response.body().string();
                    JSONObject json = new JSONObject(resStr);
                    token = json.getString("token");
                    Log.d("DATTFDGDHKSHVJD",json.toString());

                    if (token != null){
                        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                        int currentYear = calendar.get(Calendar.YEAR);
                        int currentMonth = calendar.get(Calendar.MONTH ) + 1;
                        if (currentMonth <=9)
                            currentMonth = Integer.parseInt("0"+currentMonth);

                        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                        if (currentDay <=9)
                            currentDay = Integer.parseInt("0" + currentDay);
                        String todaysDate = currentDay+"-"+currentMonth+"-"+currentYear;
                        JSONObject jsonObject = new JSONObject();
                        Random random = new Random();
                        int theorderId = random.nextInt(999999);
                        String orderId = String.valueOf(theorderId);
                        try {
                            jsonObject.put("order_id", orderId);
                            jsonObject.put("order_date", todaysDate);
                            jsonObject.put("pickup_location", "Store");
                            jsonObject.put("channel_id", "");
                            jsonObject.put("comment", "");
                            jsonObject.put("reseller_name", "Yuvraj Grocery");
                            jsonObject.put("company_name", "");
                            jsonObject.put("billing_customer_name", Common.currentUser.getName());
                            jsonObject.put("billing_last_name", Common.currentUser.getLastName());
                            jsonObject.put("billing_address", Common.PaymentGatewayOrderDetails.getAddress().getAddress());
                            jsonObject.put("billing_address_2", Common.PaymentGatewayOrderDetails.getAddress().getStreetAddress());
                            jsonObject.put("billing_isd_code", "");
                            jsonObject.put("billing_city", Common.PaymentGatewayOrderDetails.getAddress().getCity());
                            jsonObject.put("billing_pincode", Common.PaymentGatewayOrderDetails.getAddress().getPincode());
                            jsonObject.put("billing_state", Common.PaymentGatewayOrderDetails.getAddress().getState());
                            jsonObject.put("billing_country", "India");
                            jsonObject.put("billing_email", "hridaysarma8@gmail.com");
                            String subS = Common.PaymentGatewayOrderDetails.getUserPhone().substring(Common.PaymentGatewayOrderDetails.getUserPhone().length() - 10);
                            jsonObject.put("billing_phone", subS);
                            jsonObject.put("billing_alternate_phone", "");
                            jsonObject.put("shipping_is_billing", "1");
                            jsonObject.put("shipping_customer_name", "");
                            jsonObject.put("shipping_last_name", "");
                            jsonObject.put("shipping_address", "");
                            jsonObject.put("shipping_address_2", "");
                            jsonObject.put("shipping_city", "");
                            jsonObject.put("shipping_pincode", "");
                            jsonObject.put("shipping_country", "");
                            jsonObject.put("shipping_state", "");
                            jsonObject.put("shipping_email", "");
                            jsonObject.put("shipping_phone", "");
                            JSONArray jsonArray = new JSONArray();
                            for (int i = 0;i<Common.PaymentGatewayOrderDetails.getCartItemList().size() ; i++){
                                Random random1 = new Random();
                                String randSKu = String.valueOf(random1.nextInt(99999) +i);
                                JSONObject orderJson = new JSONObject();
                                orderJson.put("name",Common.PaymentGatewayOrderDetails.getCartItemList().get(i).getProductName());
                                orderJson.put("sku",randSKu+Common.PaymentGatewayOrderDetails.getCartItemList().get(i).getProductId());
                                orderJson.put("units",Common.PaymentGatewayOrderDetails.getCartItemList().get(i).getProductQuantity());
                                orderJson.put("selling_price",Common.PaymentGatewayOrderDetails.getCartItemList().get(i).getProductSellingPrice());
                                orderJson.put("discount","0");
                                orderJson.put("tax","0");
                                orderJson.put("hsn","");
                                jsonArray.put(orderJson);
                            }
                            jsonObject.put("order_items", jsonArray);
                            jsonObject.put("payment_method", "Prepaid");
                            jsonObject.put("giftwrap_charges", "0");
                            jsonObject.put("transaction_charges", "0");
                            jsonObject.put("total_discount", "0");
                            jsonObject.put("sub_total", Common.PaymentGatewayOrderDetails.getTotalPayment());
                            jsonObject.put("length", String.valueOf(Common.PaymentGatewayOrderDetails.getFinalLength()));
                            jsonObject.put("breadth", String.valueOf(Common.PaymentGatewayOrderDetails.getFinalBreadth()));
                            jsonObject.put("height", String.valueOf(Common.PaymentGatewayOrderDetails.getFinalHeight()));
                            jsonObject.put("weight", String.valueOf(Common.PaymentGatewayOrderDetails.getFinalWeight()/1000f));
                            jsonObject.put("ewaybill_no", "");
                            jsonObject.put("customer_gstin", "");

                            Log.d("THE OBJECT",jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        OkHttpClient client2 = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType2 = MediaType.parse("application/json");
                        RequestBody body2 = RequestBody.create(jsonObject.toString(),mediaType2);
                        Request request2 = new Request.Builder()
                                .url("https://apiv2.shiprocket.in/v1/external/orders/create/adhoc")
                                .method("POST", body2)
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Authorization", "Bearer "+token)
                                .build();
                        try {
                            Response response2= client2.newCall(request2).execute();
                            String resStr2 = response2.body().string();
                            JSONObject json2 = new JSONObject(resStr2);
                            Log.d("AAAAAAAAAAAAAAAAA",json2.toString());
                            Common.PaymentGatewayOrderDetails = null;
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                        ///// Send Order To ShipRocket /////
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            alertDialog.dismiss();
        }
    }

    class SendSmsToUserAndServer extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get the string by calling the get url and add the user info //

            url = smsClient.getUrl(AuthKey,Message,SenderId,PhoneNumber,RouteId,SMSContentType);
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

            url2 = smsClient2.getUrl(AuthKey2,Message2,SenderId2,PhoneNumber2,RouteId2,SMSContentType2);
            Request request2 = new Request.Builder()
                    .url(url2)
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