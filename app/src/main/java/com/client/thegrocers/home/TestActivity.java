package com.client.thegrocers.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.client.thegrocers.Model.Order;
import com.client.thegrocers.R;
import com.razorpay.Checkout;

import org.json.JSONObject;

public class TestActivity extends AppCompatActivity {

    Checkout checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        checkout = new Checkout();
        checkout.setKeyID("<YOUR_KEY_ID>");

        createOrder();
    }

    private void createOrder() {
        try {  JSONObject orderRequest = new JSONObject();  orderRequest.put("amount", 50000);
            // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_11");
            Order order = razorpay.Orders.create(orderRequest);
        } catch (RazorpayException e) {

        } // Handle Exception  System.out.println(e.getMessage());}
    }
}