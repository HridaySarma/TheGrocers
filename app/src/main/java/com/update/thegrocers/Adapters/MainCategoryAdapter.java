package com.update.thegrocers.Adapters;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {

    Context context;
    List<CategoryModel> categoryModelList;

    public MainCategoryAdapter(Context context, List<CategoryModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_cat_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(categoryModelList.get(position).getImage()).into(holder.mainCatImageView);
        holder.mainCatTextView.setText(categoryModelList.get(position).getName());
        holder.setiItemClickListener(new IItemClick() {
            @Override
            public void onItemClicked(View view, int position) {
                Common.categorySelected = categoryModelList.get(position);
                EventBus.getDefault().postSticky(new CategoryClicked(true,categoryModelList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Unbinder unbinder;
        @BindView(R.id.mainCatImgvw)
        ImageView mainCatImageView;
        @BindView(R.id.mainCatName)
        TextView mainCatTextView;
        IItemClick iItemClickListener;

        public void setiItemClickListener(IItemClick iItemClickListener) {
            this.iItemClickListener = iItemClickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            iItemClickListener.onItemClicked(view,getAdapterPosition());
        }
    }
}
