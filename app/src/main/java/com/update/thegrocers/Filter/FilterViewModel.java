package com.update.thegrocers.Filter;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.update.thegrocers.Callbacks.IFilterCallbackListener;
import com.update.thegrocers.Common.Common;
import com.update.thegrocers.Model.CategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilterViewModel extends ViewModel implements IFilterCallbackListener {

    private MutableLiveData<List<CategoryModel>> mutableLiveDataCategories;
    private IFilterCallbackListener filterCallbackListener;

    public FilterViewModel() {
        filterCallbackListener = this;
    }

    public MutableLiveData<List<CategoryModel>> getMutableLiveDataCategories() {
        if (mutableLiveDataCategories == null){
            mutableLiveDataCategories= new MutableLiveData<>();
            LoadCategories();
        }
        return mutableLiveDataCategories;
    }

    private void LoadCategories() {
        List<CategoryModel> tempList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.CATEGORIES_REF)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapShot : snapshot.getChildren()){
                            CategoryModel categoryModel = itemSnapShot.getValue(CategoryModel.class);
                            categoryModel.setName(itemSnapShot.getKey());
                            tempList.add(categoryModel);
                        }
                        filterCallbackListener.onCategoryLoadSuccess(tempList);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onCategoryLoadSuccess(List<CategoryModel> categoryModelList) {
        mutableLiveDataCategories.setValue(categoryModelList);
    }
}
