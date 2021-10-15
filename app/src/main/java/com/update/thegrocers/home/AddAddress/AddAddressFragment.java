package com.update.thegrocers.home.AddAddress;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.update.thegrocers.Callbacks.ICurrentFragment;
import com.update.thegrocers.Common.Common;
import com.update.thegrocers.EventBus.CurrentLocationClickedToGetAddress;
import com.update.thegrocers.EventBus.LocationAddedNowBackToAddressList;
import com.update.thegrocers.Model.AddressModel;
import com.update.thegrocers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class AddAddressFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.save_address_btn)
    Button save_address_button;
    @BindView(R.id.select_current_location_btn)
    Button select_current_location_btn;
    @BindView(R.id.recepient_name)
    MaterialEditText receipent_name_edt;
    @BindView(R.id.address_name)
    MaterialEditText address_edt;
    @BindView(R.id.street_address_name)
    MaterialEditText street_address_edt;
    @BindView(R.id.landmark_name)
    MaterialEditText landmark_edt;
    @BindView(R.id.state_name)
    MaterialEditText state_edt;
    @BindView(R.id.pincode_name)
    MaterialEditText pincode_edt;
    @BindView(R.id.city_name)
    MaterialEditText city_name_edt;
    private AlertDialog dialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);
        unbinder = ButterKnife.bind(this,view);
        Common.CurrentFragment = "AddAddress";
        ICurrentFragment iCurrentFragment  = (ICurrentFragment) getContext();
        iCurrentFragment.currentFragment("Other");
        if (Common.AddressToBeSelected != null){
            initViews(Common.AddressToBeSelected);
        }else {
            initViews();
        }
        setClickers();
        return view;
    }

    private void initViews() {
        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
    }

    private void setClickers() {
        save_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(address_edt.getText().toString()) || TextUtils.isEmpty(street_address_edt.getText().toString())|| TextUtils.isEmpty(landmark_edt.getText().toString())|| TextUtils.isEmpty(pincode_edt.getText().toString())|| TextUtils.isEmpty(state_edt.getText().toString()) || TextUtils.isEmpty(receipent_name_edt.getText().toString()) || TextUtils.isEmpty(city_name_edt.getText().toString())){
                    Toast.makeText(getContext(), "Fill all the information to continue", Toast.LENGTH_SHORT).show();
                }else {
                    SaveTheAddress();
                }
            }
        });

        select_current_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new CurrentLocationClickedToGetAddress(true));
            }
        });
    }

    private void SaveTheAddress() {
        dialog.show();

        AddressModel addressModel = new AddressModel();
        addressModel.setAddress(address_edt.getText().toString());
        addressModel.setLandmark(landmark_edt.getText().toString());
        addressModel.setName(receipent_name_edt.getText().toString());
        addressModel.setStreetAddress(street_address_edt.getText().toString());
        addressModel.setPincode(pincode_edt.getText().toString());
        addressModel.setState(state_edt.getText().toString());
//        addressModel.setLatitude(Common.AddressToBeSelected.getLatitude());
//        addressModel.setLongitude(Common.AddressToBeSelected.getLongitude());
        addressModel.setCity(city_name_edt.getText().toString());
        Common.AddressToBeSelected = null;
        Random random = new Random();
        int i = random.nextInt(999999999);

        FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCE)
                .child(Common.currentUser.getUid())
                .child("Addresses")
                .child(String.valueOf(i))
                .setValue(addressModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(),"Address has been added successfully",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        EventBus.getDefault().postSticky(new LocationAddedNowBackToAddressList(true));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Failed to add location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews(AddressModel addressToBeSelected) {
        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        address_edt.setText(Common.AddressToBeSelected.getAddress());
        state_edt.setText(Common.AddressToBeSelected.getState());
        pincode_edt.setText(Common.AddressToBeSelected.getPincode());
        receipent_name_edt.setText(Common.currentUser.getName());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}