package com.client.thegrocers.home.Login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.yuvraj.thegroceryapp.Common.Common;
import com.yuvraj.thegroceryapp.Common.PrefsUtills;
import com.yuvraj.thegroceryapp.EventBus.LoginStatus;
import com.yuvraj.thegroceryapp.Model.UserModel;
import com.yuvraj.thegroceryapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;

import static android.app.Activity.RESULT_OK;

public class LoginFragment extends Fragment {

    private static int APP_REQUEST_CODE = 7171;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private AlertDialog dialog;
    private CompositeDisposable disposable = new CompositeDisposable();
    private DatabaseReference usersRef;
    private List<AuthUI.IdpConfig> providers;

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    public void onStop() {
        if (listener != null){
            firebaseAuth.removeAuthStateListener(listener);
            disposable.clear();
            super.onStop();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init();
        return view;
    }

    private void init() {
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        usersRef = FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCE);
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null){
                                    CheckUserFromFirebase(user);
                                }else {
                                    phoneLogin();
                                }
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(getActivity(), "Accept permissions to proceed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }
                        }).check();
            }
        };
    }

    private void phoneLogin() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(),APP_REQUEST_CODE);
    }

    private void CheckUserFromFirebase(FirebaseUser user) {
        dialog.show();
        usersRef.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            goToHomeActivity(userModel);
                        }else {
                            showRegisterDialog(user);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), ""+ error.getDetails(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showRegisterDialog(FirebaseUser user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Register");
        builder.setMessage("Please fill information");

        View itemView  = LayoutInflater.from(getContext()).inflate(R.layout.layout_register,null,false);
        final EditText edtName = itemView.findViewById(R.id.edt_name);
        final EditText edt_last_name = itemView.findViewById(R.id.edt_last_name);
        final EditText edt_phone = itemView.findViewById(R.id.edt_phone);
        edt_phone.setText(user.getPhoneNumber());
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(edtName.getText().toString())){
                    Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(edt_last_name.getText().toString())){
                    Toast.makeText(getContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
                    return;
                }
                final UserModel userModel = new UserModel();
                userModel.setUid(user.getUid());
                userModel.setName(edtName.getText().toString());
                userModel.setLastName(edt_last_name.getText().toString());
                userModel.setPhone(edt_phone.getText().toString());

                usersRef.child(user.getUid()).setValue(userModel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialogInterface.dismiss();
                                    goToHomeActivity(userModel);
                                }
                            }
                        });
            }
        });
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void goToHomeActivity(UserModel userModel) {
        Common.currentUser = userModel;
        PrefsUtills.LogInStatus("TRUE",getContext());
        EventBus.getDefault().postSticky(new LoginStatus(true));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            }else {
                Toast.makeText(getContext(), "Failed to signIn", Toast.LENGTH_SHORT).show();
            }
        }
    }


}