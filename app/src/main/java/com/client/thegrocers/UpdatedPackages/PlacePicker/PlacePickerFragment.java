package com.client.thegrocers.UpdatedPackages.PlacePicker;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.client.thegrocers.R;
import com.client.thegrocers.databinding.FragmentPlacePickerBinding;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


public class PlacePickerFragment extends Fragment {

    private FragmentPlacePickerBinding binding;
    ActivityResultLauncher<Intent> placePickerIntentLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlacePickerBinding.inflate(getLayoutInflater());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_place_picker);


        placePickerIntentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                 result -> {
                     if (result.getResultCode() == Activity.RESULT_OK) {
                         // Here, no request code
                         Intent data = result.getData();
                         Place place = Autocomplete.getPlaceFromIntent(data);
                     }
                 });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding.searchCvPlacePicker.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(getContext());
            placePickerIntentLauncher.launch(intent);
        });
    }
}