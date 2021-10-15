package com.update.thegrocers.NewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.bumptech.glide.Glide;
import com.update.thegrocers.Model.CouponModel;
import com.update.thegrocers.R;

import java.util.List;

public class CouponCodesAdapter extends LoopingPagerAdapter<CouponModel> {

    public CouponCodesAdapter(Context context, List<CouponModel> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        View view = LayoutInflater.from(context).inflate(R.layout.coupon_codes_image_item,container,false);
        return view;
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
        ImageView image = convertView.findViewById(R.id.coupon_imv);
        Glide.with(context).load(itemList.get(listPosition).getImage()).into(image);
    }
}
