package com.update.thegrocers.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.update.thegrocers.Callbacks.IAddressItemClick;
import com.update.thegrocers.EventBus.AddressSelected;
import com.update.thegrocers.Model.AddressModel;
import com.update.thegrocers.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    Context context;
    List<AddressModel> addressModelList;
    private int lastCheckedPosition = -1;

    public AddressAdapter(Context context, List<AddressModel> addressModelList) {
        this.context = context;
        this.addressModelList = addressModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delivery_address_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

       holder.addressTv.setText(addressModelList.get(position).getAddress());
       holder.streetAddressTv.setText(addressModelList.get(position).getStreetAddress());
       holder.stateTv.setText(addressModelList.get(position).getState());
       holder.landmarkTv.setText(addressModelList.get(position).getLandmark());
       holder.pincodeTv.setText(addressModelList.get(position).getPincode());
       holder.userNameTv.setText(addressModelList.get(position).getName());
       holder.cityNameTv.setText(addressModelList.get(position).getCity());

        holder.selectedAddressBtn.setChecked(position == lastCheckedPosition);

       holder.setiAddressItemClickListener1(new IAddressItemClick() {
           @Override
           public void onAddressItemClickListener(View view, int pos) {
               int copyOfLastCheckedPosition = lastCheckedPosition;
               lastCheckedPosition = position;
               notifyItemChanged(copyOfLastCheckedPosition);
               notifyItemChanged(lastCheckedPosition);
               EventBus.getDefault().postSticky(new AddressSelected(true,addressModelList.get(pos)));
           }
       });
    }

    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        Unbinder unbinder;
        @BindView(R.id.selected_location_radio_btn)
        RadioButton selectedAddressBtn;

        @BindView(R.id.userName_di)
        TextView userNameTv;

        @BindView(R.id.city_di)
        TextView cityNameTv;

        @BindView(R.id.address_di)
        TextView addressTv;

        @BindView(R.id.street_address_di)
        TextView streetAddressTv;

        @BindView(R.id.landmark_di)
        TextView landmarkTv;

        @BindView(R.id.state_di)
        TextView stateTv;

        @BindView(R.id.pincode_di)
        TextView pincodeTv;

        IAddressItemClick iAddressItemClickListener1;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            unbinder = ButterKnife.bind(this,itemView);
            selectedAddressBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int copyOfLastCheckedPosition = lastCheckedPosition;
                    lastCheckedPosition = getAdapterPosition();
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(lastCheckedPosition);

                }
            });
        }

        public void setiAddressItemClickListener1(IAddressItemClick iAddressItemClickListener1) {
            this.iAddressItemClickListener1 = iAddressItemClickListener1;
        }

        @Override
        public void onClick(View view) {
            iAddressItemClickListener1.onAddressItemClickListener(view,getAdapterPosition());
        }
    }
}
