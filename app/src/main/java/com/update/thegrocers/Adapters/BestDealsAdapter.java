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
import com.update.thegrocers.EventBus.BestDealItemClick;
import com.update.thegrocers.Model.BestDealModel;
import com.update.thegrocers.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BestDealsAdapter  extends RecyclerView.Adapter<BestDealsAdapter.ViewHolder> {

    Context context;
    List<BestDealModel> bestDealModelList;

    public BestDealsAdapter(Context context, List<BestDealModel> bestDealModelList) {
        this.context = context;
        this.bestDealModelList = bestDealModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.best_deals_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bestDealName.setText(bestDealModelList.get(position).getName());
        Glide.with(context).load(bestDealModelList.get(position).getImage()).into(holder.bestDealImage);
        holder.price.setText(new StringBuilder("Rs ").append(bestDealModelList.get(position).getPrice()));
        holder.setiItemClick((view, position1) -> EventBus.getDefault().postSticky(new BestDealItemClick(true,bestDealModelList.get(position1))));
    }


    @Override
    public int getItemCount() {
        return bestDealModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Unbinder unbinder;
        @BindView(R.id.best_deal_image)
        ImageView bestDealImage;
        @BindView(R.id.best_deal_product_name)
        TextView bestDealName;
        IItemClick iItemClick;
        @BindView(R.id.best_deal_price)
        TextView price;

        public void setiItemClick(IItemClick iItemClick) {
            this.iItemClick = iItemClick;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iItemClick.onItemClicked(view,getAdapterPosition());
        }

    }
}
