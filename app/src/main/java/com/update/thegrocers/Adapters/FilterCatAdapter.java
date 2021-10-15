package com.update.thegrocers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.update.thegrocers.Callbacks.IItemClick;
import com.update.thegrocers.EventBus.FilterCategoryAdded;
import com.update.thegrocers.Model.CategoryModel;
import com.update.thegrocers.R;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilterCatAdapter extends RecyclerView.Adapter<FilterCatAdapter.ViewHolder> {

    List<CategoryModel> categoryModels;
    Context context;
    private List<CategoryModel> tempList;
    private List<CategoryModel> catStorage;

    public FilterCatAdapter(List<CategoryModel> categoryModels, Context context,List<CategoryModel> catStorage) {
        this.categoryModels = categoryModels;
        this.context = context;
        tempList = new ArrayList<>();
        this.catStorage = catStorage;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.filter_cat_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FilterCatAdapter.ViewHolder holder, int position) {
        holder.name.setText(categoryModels.get(position).getName());
        if (catStorage != null ){
            if (catStorage.size()>0){
                for (int i=0;i<catStorage.size();i++){
                    if (categoryModels.get(position).getName().equals(catStorage.get(i).getName())){
                        holder.bg.setBackgroundColor(context.getColor(R.color.pitchColor));
                        tempList.add(categoryModels.get(position));
                        EventBus.getDefault().postSticky(new FilterCategoryAdded(true,tempList));
                    }
                }
            }
        }
        holder.setiItemClickListener((view, position1) -> {
            boolean found = false;
            for (int i =0;i<tempList.size();i++){
                if (tempList.contains(categoryModels.get(position))){
                    found = true;
                    break;
                }
            }
            if (found){
                holder.bg.setBackgroundColor(context.getColor(R.color.white));
                tempList.remove(categoryModels.get(position));
                EventBus.getDefault().postSticky(new FilterCategoryAdded(true,tempList));
            }else {
                holder.bg.setBackgroundColor(context.getColor(R.color.pitchColor));
                tempList.add(categoryModels.get(position));
                EventBus.getDefault().postSticky(new FilterCategoryAdded(true,tempList));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        IItemClick iItemClickListener;
        RelativeLayout bg;

        public void setiItemClickListener(IItemClick iItemClickListener) {
            this.iItemClickListener = iItemClickListener;
        }

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.fil_cat_name_tv);
            bg = itemView.findViewById(R.id.fltr_itm_bg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iItemClickListener.onItemClicked(v,getAdapterPosition());
        }
    }
}
