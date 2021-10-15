package com.update.thegrocers.NewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.update.thegrocers.Model.ImageModel;
import com.update.thegrocers.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CompxletedOrdersImageAdapter extends RecyclerView.Adapter<CompxletedOrdersImageAdapter.ViewHolder> {

    Context context;
    List<ImageModel> ongoingOrdersModelList;

    public CompxletedOrdersImageAdapter(Context context, List<ImageModel> ongoingOrdersModelList) {
        this.context = context;
        this.ongoingOrdersModelList = ongoingOrdersModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.completed_order_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CompxletedOrdersImageAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(ongoingOrdersModelList.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return ongoingOrdersModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.co_rv_img_item);
        }
    }
}
