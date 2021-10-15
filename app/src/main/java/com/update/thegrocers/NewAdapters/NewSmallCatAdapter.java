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
import com.update.thegrocers.EventBus.CategoryClicked;
import com.update.thegrocers.Model.CategoryModel;
import com.update.thegrocers.R;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewSmallCatAdapter extends RecyclerView.Adapter<NewSmallCatAdapter.ViewHolder> {

    Context context;
    List<CategoryModel> categoryModelList;

    public NewSmallCatAdapter(Context context, List<CategoryModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.new_small_cat_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NewSmallCatAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(categoryModelList.get(position).getIcon()).into(holder.image);
        holder.name.setText(categoryModelList.get(position).getName());
        holder.setItemClickListener((view, position1) -> {
            Common.categorySelected = categoryModelList.get(position1);
            EventBus.getDefault().postSticky(new CategoryClicked(true,categoryModelList.get(position1)));
        });
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView name;
        IItemClick itemClickListener;

        public void setItemClickListener(IItemClick itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.nsci_img);
            name = itemView.findViewById(R.id.nsci_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        itemClickListener.onItemClicked(v,getAdapterPosition());
        }
    }
}
