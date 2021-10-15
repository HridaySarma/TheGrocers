package com.update.thegrocers.NewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.update.thegrocers.Callbacks.IItemClick;
import com.update.thegrocers.Model.ImageModel;
import com.update.thegrocers.Model.OngoingOrdersModel;
import com.update.thegrocers.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompletedOrdersAdapter extends RecyclerView.Adapter<CompletedOrdersAdapter.ViewHolder> {

    Context context;
    List<OngoingOrdersModel> ordersModels;

    public CompletedOrdersAdapter(Context context, List<OngoingOrdersModel> ordersModels) {
        this.context = context;
        this.ordersModels = ordersModels;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.completed_orders_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CompletedOrdersAdapter.ViewHolder holder, int position) {
        if (ordersModels.get(position).getCartItemList().size() <2){
            holder.prodName.setText(new StringBuilder(ordersModels.get(position).getCartItemList().get(0).getProductName()));
        }else {
            holder.prodName.setText(new StringBuilder(ordersModels.get(position).getCartItemList().get(0).getProductName()).append("...").append(ordersModels.get(position).getCartItemList().size()-1).append(" more items"));
        }
        holder.finalPrice.setText(new StringBuilder("Rs ").append(ordersModels.get(position).getFinalPayment()));
        holder.orderId.setText(ordersModels.get(position).getOrderNumber());
        Glide.with(context).load(ordersModels.get(position).getCartItemList().get(0).getProductImage()).into(holder.mainImage);
        if (ordersModels.get(position).getCartItemList().size()>1){
            List<ImageModel> tempImages = new ArrayList<>();
            for (int i=1;i<ordersModels.get(position).getCartItemList().size();i++){
                ImageModel img = new ImageModel(ordersModels.get(position).getCartItemList().get(i).getProductImage());
                tempImages.add(img);
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            holder.imagesRv.setLayoutManager(linearLayoutManager);
            CompxletedOrdersImageAdapter imageAdapter = new CompxletedOrdersImageAdapter(context,tempImages);
            holder.imagesRv.setAdapter(imageAdapter);
        }


    }

    @Override
    public int getItemCount() {
        return ordersModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView prodName,finalPrice,orderId,orderDate;
        Button repeatOrderBtn;
        RecyclerView imagesRv;
        CircleImageView mainImage;
        IItemClick iItemClickListener;

        public void setiItemClickListener(IItemClick iItemClickListener) {
            this.iItemClickListener = iItemClickListener;
        }

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            repeatOrderBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iItemClickListener.onItemClicked(v,getAdapterPosition());
        }
    }
}
