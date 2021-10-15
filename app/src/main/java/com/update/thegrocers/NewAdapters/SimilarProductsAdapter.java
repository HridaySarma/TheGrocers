package com.update.thegrocers.NewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.update.thegrocers.Callbacks.IItemClick;
import com.update.thegrocers.Common.Common;
import com.update.thegrocers.EventBus.ProductClicked;
import com.update.thegrocers.Model.ProductModel;
import com.update.thegrocers.R;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimilarProductsAdapter extends RecyclerView.Adapter<SimilarProductsAdapter.ViewHolder> {

    Context context;
    List<ProductModel> productModelList;

    public SimilarProductsAdapter(Context context, List<ProductModel> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.similar_products_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SimilarProductsAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(productModelList.get(position).getImage()).into(holder.imageView);
        holder.name.setText(productModelList.get(position).getName());
        holder.price.setText(new StringBuilder("Rs ").append(productModelList.get(position).getSellingPrice()));
        holder.setItemClickListener((view, position1) -> {
            Common.selectedProduct = productModelList.get(position);
            EventBus.getDefault().postSticky(new ProductClicked(true));
        });
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView name,price;
        private IItemClick itemClickListener;

        public void setItemClickListener(IItemClick itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sim_prods_imv);
            name = itemView.findViewById(R.id.similar_prods_name);
            price = itemView.findViewById(R.id.similar_prods_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v,getAdapterPosition());
        }
    }
}













