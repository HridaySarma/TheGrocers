package com.client.thegrocers.home.SingleBuyNow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.yuvraj.thegroceryapp.Adapters.BuyNowAdapter;
import com.yuvraj.thegroceryapp.Common.Common;
import com.yuvraj.thegroceryapp.Model.OrdersModel;
import com.yuvraj.thegroceryapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BuyNowFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.items_by_nw_rv)
    RecyclerView itemsList;
    @BindView(R.id.subtotal_bynw_tv)
    TextView subTotalTv;
    @BindView(R.id.delivery_charges_bynw_tv)
    TextView deliveryChargesTv;
    @BindView(R.id.total_bynw_tv)
    TextView totalTv;
    @BindView(R.id.cod_bynw_rdo_btn)
    RadioButton codBtn;
    @BindView(R.id.pay_nw_bynw_rdo_btn)
    RadioButton payNowBtn;
    private long deliveryCharges;
    @BindView(R.id.place_order_bynw_btn)
    Button placeOrderBtn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_buy_now, container, false);
        unbinder = ButterKnife.bind(this,view);
        initRv();
        return view;
    }



    private void initRv() {
        BuyNowAdapter buyNowAdapter = new BuyNowAdapter(getContext(),Common.buyNowClass);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        itemsList.setLayoutManager(linearLayoutManager);
        itemsList.setAdapter(buyNowAdapter);
        totalTv.setText(new StringBuilder("Rs ").append(Common.buyNowClass.getPrice()*buyNowAdapter.getQuantity()));
        if (Common.buyNowClass.getPrice()*buyNowAdapter.getQuantity() > 1000){
            deliveryCharges = 50;
        }else {
            deliveryCharges = 0;
        }
        deliveryChargesTv.setText(new StringBuilder("Rs ").append(deliveryCharges));
        subTotalTv.setText(new StringBuilder("Rs ").append(deliveryCharges + (Common.buyNowClass.getPrice()*buyNowAdapter.getQuantity()) ));

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (codBtn.isChecked()){
                    startCodeProcess();
                }else if (payNowBtn.isChecked()){
                    startPaymentGateway();
                }else {
                    Snackbar.make(getView(),"Please select a payment method to continue", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void startPaymentGateway() {

    }

    private void startCodeProcess() {
        OrdersModel ordersModel = new OrdersModel();

    }


}

