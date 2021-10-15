package com.update.thegrocers.home.AdressList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.update.thegrocers.Adapters.AddressAdapter;
import com.update.thegrocers.Callbacks.ICurrentFragment;
import com.update.thegrocers.Common.Common;
import com.update.thegrocers.EventBus.AddNewAddressClicke;
import com.update.thegrocers.EventBus.AddressSelected;
import com.update.thegrocers.EventBus.ProceedToCheckoutClicked;
import com.update.thegrocers.Mapbox.MapboxKey;
import com.update.thegrocers.Model.AddressModel;
import com.update.thegrocers.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddressListFragment extends Fragment {

    private MapboxKey mapboxKey;
    Unbinder unbinder;
    @BindView(R.id.addresses_rv_list)
    RecyclerView address_rv;
    @BindView(R.id.add_new_address_tv)
    TextView add_new_address_tv;
    @BindView(R.id.proceed_to_checkout_btn)
    Button proceed_to_checkout_btn;
    private AddressViewModel addressViewModel;
    private AddressAdapter addressAdapter;
    private AddressModel addressSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);
        View view = inflater.inflate(R.layout.fragment_address_list, container, false);
        unbinder = ButterKnife.bind(this,view);
        Common.CurrentFragment = "AddressList";
        ICurrentFragment iCurrentFragment  = (ICurrentFragment) getContext();
        iCurrentFragment.currentFragment("Other");
        mapboxKey= new MapboxKey();
        if (Common.currentUser != null){
            initViews();
            setClickListeners();
        }else {
            Toast.makeText(getContext(), "No user", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void setClickListeners() {
        proceed_to_checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressSelected != null){
                    EventBus.getDefault().postSticky(new ProceedToCheckoutClicked(true,addressSelected));
                }else {
                    Toast.makeText(getContext(), "Please select an address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        add_new_address_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new AddNewAddressClicke(true));
            }
        });
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        address_rv.setLayoutManager(layoutManager);

        addressViewModel.getMutableLiveDataAddressModel().observe(getViewLifecycleOwner(), new Observer<List<AddressModel>>() {
            @Override
            public void onChanged(List<AddressModel> addressModels) {
                addressAdapter = new AddressAdapter(getContext(),addressModels);
                address_rv.setAdapter(addressAdapter);
                address_rv.addItemDecoration(new DividerItemDecoration(getContext(),layoutManager.getOrientation()));
            }
        });

        addressViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), " "+ s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mapboxKey.run();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void  onAddressSelected(AddressSelected event){
        if (event.isSuccess()){
            addressSelected = event.getAddressModel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}