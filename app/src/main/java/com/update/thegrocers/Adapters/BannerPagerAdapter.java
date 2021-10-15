package com.update.thegrocers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.bumptech.glide.Glide;
import com.update.thegrocers.Model.BannerModel;
import com.update.thegrocers.R;

import java.util.List;

public class BannerPagerAdapter extends LoopingPagerAdapter<BannerModel> {


    public BannerPagerAdapter(Context context, List<BannerModel> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item,container,false);
        return view;
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
        ImageView imageView = convertView.findViewById(R.id.banner_item);
        Glide.with(context).load(itemList.get(listPosition).getImage()).into(imageView);
    }
}
