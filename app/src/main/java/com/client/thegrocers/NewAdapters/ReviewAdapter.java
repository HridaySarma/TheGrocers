package com.client.thegrocers.NewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.client.thegrocers.Model.ReviewModel;
import com.client.thegrocers.R;
import com.willy.ratingbar.ScaleRatingBar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    Context context;
    List<ReviewModel> reviewModels;

    public ReviewAdapter(Context context, List<ReviewModel> reviewModels) {
        this.context = context;
        this.reviewModels = reviewModels;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReviewAdapter.ViewHolder holder, int position) {
        holder.ratingBar.setRating(reviewModels.get(position).getRating());
        holder.ratingNum.setText(String.valueOf(reviewModels.get(position).getRating()));
        holder.userName.setText(reviewModels.get(position).getUserModel().getName());
        holder.text.setText(reviewModels.get(position).getDescription());
        holder.date.setText(new StringBuilder("").append(reviewModels.get(position).getDay()).append("/").append(reviewModels.get(position).getMonth())
        .append("/").append(reviewModels.get(position).getYear()));
    }

    @Override
    public int getItemCount() {
        return reviewModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text,userName,date,ratingNum;
        ScaleRatingBar ratingBar;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.rv_itm_rating_bar);
            text = itemView.findViewById(R.id.r_itm_description);
            userName= itemView.findViewById(R.id.r_itm_userName);
            date = itemView.findViewById(R.id.r_itm_date);
            ratingNum = itemView.findViewById(R.id.rating_tv_whole);

        }
    }
}
