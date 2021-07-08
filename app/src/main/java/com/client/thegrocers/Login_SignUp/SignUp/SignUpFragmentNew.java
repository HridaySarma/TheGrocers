package com.client.thegrocers.Login_SignUp.SignUp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.client.thegrocers.R;
import com.client.thegrocers.UpdatedPackages.CommonNew.NewUserMode;
import com.client.thegrocers.UpdatedPackages.EventBus.GoToOtpFrag;
import com.client.thegrocers.databinding.FragmentSignUpNewBinding;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragmentNew extends Fragment {

    private FragmentSignUpNewBinding binding;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpNewBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        initButtons();
    }

    private void initButtons() {
        binding.signUpBtn.setOnClickListener(v->{
            String name,phone,email,password;
            name = binding.nameEdtS.getText().toString();
            phone = binding.phoneEdtS.getText().toString();
            email = binding.emailEdtS.getText().toString();
            password = binding.passwordEdtS.getText().toString();
            if (name.equals("") || name.length() <= 4){
                Snackbar.make(v,"Name should be more than 4 letters long",Snackbar.LENGTH_SHORT).show();
            }else if (phone.equals("") || phone.length() != 10){
                Snackbar.make(v,"Phone number should be 10 digits long",Snackbar.LENGTH_SHORT).show();
            }else if (email.equals("") || !validate(email)){
                Snackbar.make(v,"Enter a valid email address",Snackbar.LENGTH_SHORT).show();
            }else if (password.equals("") || password.length() <8 ){
                Snackbar.make(v,"Password must be more than 8 characters long",Snackbar.LENGTH_SHORT).show();
            }else {
                sendOtp(name,phone,email,password);
            }
        });
    }

    private void sendOtp(String name,String phone,String email,String password){
        NewUserMode newUserMode = new NewUserMode(name,email,phone,password);
        EventBus.getDefault().postSticky(new GoToOtpFrag(true,newUserMode));
    }



    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}

