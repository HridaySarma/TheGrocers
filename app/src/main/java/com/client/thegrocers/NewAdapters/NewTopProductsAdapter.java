package com.client.thegrocers.NewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.client.thegrocers.Callbacks.IItemClick;
import com.client.thegrocers.EventBus.BestDealItemClick;
import com.client.thegrocers.Model.BestDealModel;
import com.client.thegrocers.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewTopProductsAdapter extends RecyclerView.Adapter<NewTopProductsAdapter.ViewHolder> {

    Context context;
    List<BestDealModel> bestDealModels;
    public static final int DARK = 0;
    public static final int LIGHT = 1;

    public NewTopProductsAdapter(Context context, List<BestDealModel> bestDealModels) {
        this.context = context;
        this.bestDealModels = bestDealModels;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == DARK){
            View view = LayoutInflater.from(context).inflate(R.layout.dark_top_prods_item,parent, false);
            return new ViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.light_top_prods_item,parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NewTopProductsAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(bestDealModels.get(position).getImage()).into(holder.image);
        holder.name.setText(bestDealModels.get(position).getName());
        holder.price.setText(new StringBuilder("Rs ").append(bestDealModels.get(position).getPrice()));
        holder.catName.setText(bestDealModels.get(position).getCategory_id());
        holder.addToCartBtn.setOnClickListener(v -> {

        });
        holder.setiItemClickListener((view, position1) -> {
            EventBus.getDefault().postSticky(new BestDealItemClick(true,bestDealModels.get(position1)));
        });
    }

    @Override
    public int getItemCount() {
        return bestDealModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (((float) position % 2f) == 0){
            return DARK;
        }else {
            return LIGHT;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        ImageView image;
        TextView name,catName,price;
        FloatingActionButton addToCartBtn;
        IItemClick iItemClickListener;

        public void setiItemClickListener(IItemClick iItemClickListener) {
            this.iItemClickListener = iItemClickListener;
        }

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.dtpt_image);
            name = itemView.findViewById(R.id.dtpi_name);
            catName = itemView.findViewById(R.id.dpti_category);
            price = itemView.findViewById(R.id.dtpi_price);
            addToCartBtn = itemView.findViewById(R.id.dtpi_add_to_cart_btn);
            itemView.setOnClickListener(this);
            addToCartBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iItemClickListener.onItemClicked(v,getAdapterPosition());
        }
    }
}













