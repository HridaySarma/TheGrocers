package com.client.thegrocers.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuvraj.thegroceryapp.Callbacks.IBannerCallback;
import com.yuvraj.thegroceryapp.Callbacks.IBestDealsCallback;
import com.yuvraj.thegroceryapp.Callbacks.ICategoryCallback;
import com.yuvraj.thegroceryapp.Callbacks.IPopularCategoriesCallback;
import com.yuvraj.thegroceryapp.Common.Common;
import com.yuvraj.thegroceryapp.Model.BannerModel;
import com.yuvraj.thegroceryapp.Model.BestDealModel;
import com.yuvraj.thegroceryapp.Model.CategoryModel;
import com.yuvraj.thegroceryapp.Model.PopularCategoriesModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel implements ICategoryCallback, IBannerCallback, IPopularCategoriesCallback, IBestDealsCallback {

    MutableLiveData<List<CategoryModel>> mutableLiveDataCategories;
    MutableLiveData<List<BannerModel>> mutableLiveDataBanners;
    MutableLiveData<List<PopularCategoriesModel>> mutableLiveDataPopularCategories;
    MutableLiveData<List<BestDealModel>> mutableLiveDataBestDeals;
    MutableLiveData<String> messageError;
    private ICategoryCallback categoryCallbackListener;
    private IBestDealsCallback bestDealsCallbackListener;
    private IBannerCallback bannerCallbackListener;
    private IPopularCategoriesCallback popularCategoriesCallbackListener;

    public HomeViewModel() {
        categoryCallbackListener = this;
        bannerCallbackListener = this;
        popularCategoriesCallbackListener = this;
        bestDealsCallbackListener = this;
    }

    public MutableLiveData<List<CategoryModel>> getMutableLiveDataCategories() {
        if (mutableLiveDataCategories == null){
            mutableLiveDataCategories = new MutableLiveData<>();
            LoadCategories();
        }
        return mutableLiveDataCategories;
    }

    public MutableLiveData<String> getMessageError() {
        if (messageError == null){
            messageError = new MutableLiveData<>();
        }
        return messageError;
    }

    public MutableLiveData<List<BannerModel>> getMutableLiveDataBanners() {
        if (mutableLiveDataBanners == null){
            mutableLiveDataBanners = new MutableLiveData<>();
            LoadBanners();
        }
        return mutableLiveDataBanners;
    }

    public MutableLiveData<List<PopularCategoriesModel>> getMutableLiveDataPopularCategories() {
        if (mutableLiveDataPopularCategories == null){
            mutableLiveDataPopularCategories = new MutableLiveData<>();
            LoadPopularCategories();
        }
        return mutableLiveDataPopularCategories;
    }

    public MutableLiveData<List<BestDealModel>> getMutableLiveDataBestDeals() {
        if (mutableLiveDataBestDeals == null){
            mutableLiveDataBestDeals = new MutableLiveData<>();
            LoadBestDeals();
        }
        return mutableLiveDataBestDeals;
    }

    private void LoadBestDeals() {
        List<BestDealModel> bestDealModelstt = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.BEST_DEALS_REF)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot :snapshot.getChildren()){
                            BestDealModel bestDealModel = itemSnapshot.getValue(BestDealModel.class);
                            bestDealModelstt.add(bestDealModel);
                        }
                        bestDealsCallbackListener.onBestDealLoadSucccess(bestDealModelstt);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void LoadPopularCategories() {
        List<PopularCategoriesModel> tempPopular = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.POPULAR_CATEGORIES_REF)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapShot : snapshot.getChildren()){
                            PopularCategoriesModel popularCategoriesModel = itemSnapShot.getValue(PopularCategoriesModel.class);
                            tempPopular.add(popularCategoriesModel);
                        }
                        popularCategoriesCallbackListener.onPopularCategoriesLoadSuccess(tempPopular);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        popularCategoriesCallbackListener.onPopularCategoriesLoadFailed(error.getMessage());
                    }
                });
    }

    public void LoadBanners() {
        List<BannerModel> bannerModelList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.BANNERS_REF)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                            BannerModel bannerModel = itemSnapshot.getValue(BannerModel.class);
                            bannerModelList.add(bannerModel);
                        }
                        bannerCallbackListener.onBannerLoadSuccess(bannerModelList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        bannerCallbackListener.onBannerLoadFailed(error.getMessage());
                    }
                });
    }

    public void LoadCategories() {
        List<CategoryModel> categoryModelList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.CATEGORIES_REF)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot :snapshot.getChildren()){
                            CategoryModel categoryModel = itemSnapshot.getValue(CategoryModel.class);
                            categoryModel.setName(itemSnapshot.getKey());
                            categoryModelList.add(categoryModel);
                        }
                        categoryCallbackListener.onCategoryLoadSuccess(categoryModelList);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        categoryCallbackListener.onCategoryLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    public void onCategoryLoadSuccess(List<CategoryModel> categoryModelList) {
        mutableLiveDataCategories.setValue(categoryModelList);
    }

    @Override
    public void onCategoryLoadFailed(String message) {
        messageError.setValue(message);
    }

    @Override
    public void onBannerLoadSuccess(List<BannerModel> bannerModelList) {
        mutableLiveDataBanners.setValue(bannerModelList);
    }

    @Override
    public void onBannerLoadFailed(String message) {
        messageError.setValue(message);
    }

    @Override
    public void onPopularCategoriesLoadSuccess(List<PopularCategoriesModel> popularCategoriesModelList) {
        mutableLiveDataPopularCategories.setValue(popularCategoriesModelList);
    }

    @Override
    public void onPopularCategoriesLoadFailed(String message) {
        messageError.setValue(message);
    }

    @Override
    public void onBestDealLoadSucccess(List<BestDealModel> bestDealModelList) {
        mutableLiveDataBestDeals.setValue(bestDealModelList);
    }
}
