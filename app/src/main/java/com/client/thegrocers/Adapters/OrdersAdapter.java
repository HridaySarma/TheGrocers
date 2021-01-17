package com.client.thegrocers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yuvraj.thegroceryapp.Callbacks.IItemClick;
import com.yuvraj.thegroceryapp.Common.Common;
import com.yuvraj.thegroceryapp.EventBus.OrderDetailsClicked;
import com.yuvraj.thegroceryapp.EventBus.OrderRequestForCancel;
import com.yuvraj.thegroceryapp.EventBus.OrderRequestForReturn;
import com.yuvraj.thegroceryapp.Model.Order;
import com.yuvraj.thegroceryapp.R;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private Context context;
    private List<Order> orderList;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    public OrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        calendar  = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_orders_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(orderList.get(position).getCartItemList().get(0).getProductImage())
                .into(holder.img_order);
        if (orderList.get(position).getCartItemList().size() == 1){
            holder.viewInOrder.setVisibility(View.GONE);
            holder.no_of_orders.setVisibility(View.GONE);
        }else {
            holder.viewInOrder.setVisibility(View.VISIBLE);
            holder.no_of_orders.setText(new StringBuilder("+ ").append(orderList.get(position).getCartItemList().size() - 1).append(" more items"));
        }
        calendar.setTimeInMillis(orderList.get(position).getCreateDate());
        Date date = new Date(orderList.get(position).getCreateDate());
        holder.txt_order_date.setText(new StringBuilder(Common.getDateOfWeek(calendar.get(Calendar.DAY_OF_WEEK)))
                .append(" ")
                .append(simpleDateFormat.format(date)));
        holder.txt_order_number.setText(new StringBuilder("").append(orderList.get(position).getOrderNumber()));
        if (orderList.get(position).getAddress() != null){
            holder.txt_order_comment.setText(new StringBuilder("").append(orderList.get(position).getAddress().getAddress()));
        }

        holder.txt_order_status.setText(new StringBuilder("").append(Common.convertOrderStatusToText(orderList.get(position).getOrderStatus())));
        holder.txt_order_price.setText(new StringBuilder("").append(orderList.get(position).getTotalPayment()));
        if (orderList.get(position).getOrderStatus() == 2 && orderList.get(position).getDeliveryDate() != 0){

            long msDiff = Calendar.getInstance().getTimeInMillis() - orderList.get(position).getDeliveryDate();
            long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

            if (daysDiff >= 0 && daysDiff<=3){
                holder.retun_order_btn.setVisibility(View.VISIBLE);
                holder.retun_order_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().postSticky(new OrderRequestForReturn(true,orderList.get(position)));
                    }
                });
            }else {
                holder.retun_order_btn.setVisibility(View.GONE);
            }
            }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new OrderDetailsClicked(true,orderList.get(position)));
            }
        });

        if (orderList.get(position).getOrderStatus() == 0 ){

            holder.cancel_order_btn.setVisibility(View.VISIBLE);
            holder.cancel_order_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().postSticky(new OrderRequestForCancel(true,orderList.get(position)));
                }
            });
        }else {
            holder.cancel_order_btn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        Unbinder unbinder;

        @BindView(R.id.order_status)
        TextView txt_order_status;
        @BindView(R.id.order_address)
        TextView txt_order_comment;
        @BindView(R.id.order_id)
        TextView txt_order_number;
        @BindView(R.id.order_date)
        TextView txt_order_date;
        @BindView(R.id.order_price)
        TextView txt_order_price;
        @BindView(R.id.view_in_order_img)
        View viewInOrder;
        @BindView(R.id.no_of_order_items)
        TextView no_of_orders;

        @BindView(R.id.order_img)
        ImageView img_order;
        @BindView(R.id.return_order_btn)
        Button retun_order_btn;
        @BindView(R.id.cancel_order_btn)
        Button cancel_order_btn;
        IItemClick categoryClick;


        public void setCategoryClick(IItemClick categoryClick) {
            this.categoryClick = categoryClick;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            retun_order_btn.setOnClickListener(this);
            cancel_order_btn .setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            categoryClick.onItemClicked(view,getAdapterPosition());
        }
    }
}
