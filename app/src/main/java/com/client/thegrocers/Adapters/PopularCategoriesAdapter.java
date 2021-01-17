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
import com.yuvraj.thegroceryapp.EventBus.PopularCategoryClick;
import com.yuvraj.thegroceryapp.Model.PopularCategoriesModel;
import com.yuvraj.thegroceryapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PopularCategoriesAdapter extends RecyclerView.Adapter<PopularCategoriesAdapter.ViewHolder> {

    Context context;
    List<PopularCategoriesModel> popularCategoriesModelList;

    public PopularCategoriesAdapter(Context context, List<PopularCategoriesModel> popularCategoriesModelList) {
        this.context = context;
        this.popularCategoriesModelList = popularCategoriesModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_item,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public int getItemViewType(int position) {
        return  super.getItemViewType(position);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(popularCategoriesModelList.get(position).getImage()).into(holder.popularCatImage);
        holder.popularCatName.setText(popularCategoriesModelList.get(position).getName());
        holder.setiItemClickListener(new IItemClick() {
            @Override
            public void onItemClicked(View view, int position) {
                EventBus.getDefault().postSticky(new PopularCategoryClick(true,popularCategoriesModelList.get(position)));
            }
        });

        holder.viewMorePopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new PopularCategoryClick(true,popularCategoriesModelList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularCategoriesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        Unbinder unbinder;
        @BindView(R.id.popular_cat_image)
        ImageView popularCatImage;
        @BindView(R.id.popular_cat_name)
        TextView popularCatName;
        @BindView(R.id.view_more_pop_btn)
        Button viewMorePopBtn;
        IItemClick iItemClickListener;

        public void setiItemClickListener(IItemClick iItemClickListener) {
            this.iItemClickListener = iItemClickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            viewMorePopBtn.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            iItemClickListener.onItemClicked(view,getAdapterPosition());
        }
    }
}
