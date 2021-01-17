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
import com.yuvraj.thegroceryapp.EventBus.ProductClickedInAllProducts;
import com.yuvraj.thegroceryapp.Model.SingletonProductModel;
import com.yuvraj.thegroceryapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AnyProdAdapter extends RecyclerView.Adapter<AnyProdAdapter.ViewHolder> {

    Context context;
    List<SingletonProductModel> singletonProductModelList;

    public AnyProdAdapter(Context context, List<SingletonProductModel> singletonProductModelList) {
        this.context = context;
        this.singletonProductModelList = singletonProductModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.any_product_recycler_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(singletonProductModelList.get(position).getImage()).into(holder.prodImage);
        holder.prodName.setText(new StringBuilder(singletonProductModelList.get(position).getName()));
        holder.prodPrice.setText(String.valueOf(singletonProductModelList.get(position).getSellingPrice()));
        holder.mainCatTv.setText(singletonProductModelList.get(position).getCategory_id());
        holder.viewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new ProductClickedInAllProducts(true,singletonProductModelList.get(position)));
            }
        });
        holder.setCategoryClickListener(new IItemClick() {
            @Override
            public void onItemClicked(View view, int position) {
                EventBus.getDefault().postSticky(new ProductClickedInAllProducts(true,singletonProductModelList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return singletonProductModelList.size();
    }

    public List<SingletonProductModel> getListOfProducts() {
        return singletonProductModelList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        Unbinder unbinder;
        @BindView(R.id.any_prod_name)
        TextView prodName;
        @BindView(R.id.any_prod_price)
        TextView prodPrice;
        @BindView(R.id.any_prod_main_cat)
        TextView mainCatTv;
        @BindView(R.id.any_prod_sub_category)
        TextView subCatTv;
        @BindView(R.id.any_prod_view_btn)
        Button viewProductBtn;
        @BindView(R.id.any_prod_image)
        ImageView prodImage;
        IItemClick categoryClickListener;


        public void setCategoryClickListener(IItemClick categoryClickListener) {
            this.categoryClickListener = categoryClickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
            viewProductBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            categoryClickListener.onItemClicked(view,getAdapterPosition());
        }
    }
}
