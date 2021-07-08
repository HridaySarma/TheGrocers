package com.client.thegrocers.UpdatedPackages.NewProductDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.client.thegrocers.Common.Common;
import com.client.thegrocers.R;
import com.client.thegrocers.databinding.FragmentReviewBottomSheetDialogBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ReviewBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private FragmentReviewBottomSheetDialogBinding binding;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentReviewBottomSheetDialogBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding.publishBtnRv.setOnClickListener(v -> {
            if (!binding.feedbackEdt.getText().toString().isEmpty() && binding.rbsRatingBar.getRating() > 0 ){
                TreeMap<String,Object> ratingMap = new TreeMap<>();
                ratingMap.put("ratingValue",String.valueOf(binding.rbsRatingBar.getRating()));
                ratingMap.put("ratingDesc",binding.feedbackEdt.getText().toString());
                addRatinng(ratingMap);
            }else if (binding.rbsRatingBar.getRating() > 0){
                TreeMap<String,Object> ratingMap = new TreeMap<>();
                ratingMap.put("ratingValue",String.valueOf(binding.rbsRatingBar.getRating()));
                ratingMap.put("ratingDesc","No review");
                addRatinng(ratingMap);
            }else {
                Snackbar.make(v,"Please provide a rating to continue",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void addRatinng(TreeMap<String, Object> ratingMap) {
        FirebaseDatabase.getInstance().getReference(Common.CATEGORIES_REF)
                .child(Common.categorySelected.getName())
                .child("products")
                .child(String.valueOf(Common.selectedProduct.getKey()))
                .child("ratings")
                .child(Common.currentUser.getPhone())
                .updateChildren(ratingMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()){
                            if (Common.selectedProduct.getTotalRating() != null){
                                float rating = Float.parseFloat(Common.selectedProduct.getTotalRating()) + binding.rbsRatingBar.getRating();
                                int count = Integer.parseInt(Common.selectedProduct.getRatingCounter()) + 1;
                                Map<String ,Object> updateMap =  new HashMap<>();
                                updateMap.put("totalRating",String.valueOf(rating));
                                updateMap.put("ratingCounter",String.valueOf(count));
                                FirebaseDatabase.getInstance().getReference(Common.CATEGORIES_REF)
                                        .child(Common.categorySelected.getName())
                                        .child("products")
                                        .child(String.valueOf(Common.selectedProduct.getKey()))
                                        .updateChildren(updateMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(getContext(), "Review added successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }else {
                                Map<String ,Object> updateMap =  new HashMap<>();
                                updateMap.put("totalRating",String.valueOf(binding.rbsRatingBar.getRating()));
                                updateMap.put("ratingCounter", "1");
                                FirebaseDatabase.getInstance().getReference(Common.CATEGORIES_REF)
                                        .child(Common.categorySelected.getName())
                                        .child("products")
                                        .child(String.valueOf(Common.selectedProduct.getKey()))
                                        .updateChildren(updateMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(getContext(), "Review added successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });

    }


}



















