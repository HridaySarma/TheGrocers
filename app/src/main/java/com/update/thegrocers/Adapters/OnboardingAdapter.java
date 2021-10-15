package com.update.thegrocers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.update.thegrocers.Model.OnboardingModel;
import com.update.thegrocers.R;

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
        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .transform(new RoundedCorners(5))
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                .override(imageView.getWidth(),imageView.getHeight());// Overrides size of downloaded image and converts it's bitmaps to your desired image size;

        Glide.with(context).load(itemList.get(listPosition).getImage()).apply(reqOpt).into(imageView);
        textView.setText(itemList.get(listPosition).getTitle());
    }
}
