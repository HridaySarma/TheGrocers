package com.client.thegrocers.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.client.thegrocers.Model.OnboardingModel;
import com.client.thegrocers.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OnboardingAdapter extends LoopingPagerAdapter<OnboardingModel> {

    public OnboardingAdapter(Context context, List<OnboardingModel> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        return LayoutInflater.from(context).inflate(R.layout.onboarding_item,container,false);
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
        ImageView imageView = convertView.findViewById(R.id.onboarding_image);
        TextView textView = convertView.findViewById(R.id.onboarding_title);
        if (listPosition == 1){
            textView.setTextColor(context.getResources().getColor(R.color.white));
        }
        Glide.with(context).load(itemList.get(listPosition).getImage()).skipMemoryCache(false).into(imageView);
        textView.setText(itemList.get(listPosition).getTitle());
    }
}
