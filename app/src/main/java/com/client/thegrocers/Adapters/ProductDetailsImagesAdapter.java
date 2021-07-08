package com.client.thegrocers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.bumptech.glide.Glide;
import com.client.thegrocers.Model.ImageModel;
import com.client.thegrocers.R;

import java.util.List;

public class ProductDetailsImagesAdapter extends LoopingPagerAdapter<ImageModel> {

    public ProductDetailsImagesAdapter(Context context, List<ImageModel> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        return LayoutInflater.from(context).inflate(R.layout.details_image_item,container,false);
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
        ImageView imageView = convertView.findViewById(R.id.details_image_imv);
        Glide.with(context).load(itemList.get(listPosition).getImage()).into(imageView);
    }
}
