package com.client.thegrocers.home.Orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yuvraj.thegroceryapp.Adapters.OrderDetailsAllOrdersAdapter;
import com.yuvraj.thegroceryapp.Common.Common;
import com.yuvraj.thegroceryapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class OrderDetails extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.orderNo_od)
    TextView orderNoTv;
    @BindView(R.id.order_date_od)
    TextView order_date_tv;
    @BindView(R.id.order_total_price_od)
    TextView priceTv;
    @BindView(R.id.order_payment_type_od)
    TextView orderPaymentType;
    @BindView(R.id.order_user_name_od)
    TextView orderUserName;
    @BindView(R.id.order_user_phone)
    TextView orderUserPhone;
    @BindView(R.id.order_status_od)
    TextView orderStatus;
    @BindView(R.id.order_main_address_od)
    TextView order_main_address_tv;
    @BindView(R.id.order_street_address_od)
    TextView order_street_address_tv;
    @BindView(R.id.order_landmark_od)
    TextView order_landmark_tv;
    @BindView(R.id.order_state_od)
    TextView order_state_tv;
    @BindView(R.id.order_pincode_od)
    TextView order_pincode_tv;
    @BindView(R.id.order_city_od)
    TextView order_city_tv;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    @BindView(R.id.all_orders_in_order_details)
    RecyclerView orderDetailsRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        unbinder = ButterKnife.bind(this,view);
        calendar  = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        displayName();
        initRecycler();
        return view;
    }
    private void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        orderDetailsRv.setLayoutManager(linearLayoutManager);
        OrderDetailsAllOrdersAdapter adapter = new OrderDetailsAllOrdersAdapter(Common.orderSelectedForDetails.getCartItemList(),getContext());
        orderDetailsRv.setAdapter(adapter);
    }

    private void displayName() {
        orderNoTv.setText(Common.orderSelectedForDetails.getOrderNumber());
        order_city_tv.setText(Common.orderSelectedForDetails.getAddress().getCity());
        order_landmark_tv.setText(Common.orderSelectedForDetails.getAddress().getLandmark()) ;
        order_main_address_tv.setText(Common.orderSelectedForDetails.getAddress().getAddress());
        order_pincode_tv.setText(Common.orderSelectedForDetails.getAddress().getPincode());
        order_state_tv.setText(Common.orderSelectedForDetails.getAddress().getState());
        order_street_address_tv.setText(Common.orderSelectedForDetails.getAddress().getStreetAddress());
        orderPaymentType.setText(Common.orderSelectedForDetails.getTransactionId());
        orderUserName.setText(Common.orderSelectedForDetails.getUserName());
        orderUserPhone.setText(Common.orderSelectedForDetails.getUserPhone());
        orderStatus.setText(new StringBuilder("").append(Common.convertOrderStatusToText(Common.orderSelectedForDetails.getOrderStatus())));
        calendar.setTimeInMillis(Common.orderSelectedForDetails.getCreateDate());
        Date date = new Date(Common.orderSelectedForDetails.getCreateDate());
        order_date_tv.setText(new StringBuilder(Common.getDateOfWeek(calendar.get(Calendar.DAY_OF_WEEK)))
                .append(" ")
                .append(simpleDateFormat.format(date)));
        priceTv.setText(new StringBuilder("Rs ").append(Common.orderSelectedForDetails.getFinalPayment()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}