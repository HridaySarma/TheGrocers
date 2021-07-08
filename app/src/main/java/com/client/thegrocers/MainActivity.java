package com.client.thegrocers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.client.thegrocers.Adapters.OnboardingAdapter;
import com.client.thegrocers.Common.PrefsUtills;
import com.client.thegrocers.Model.OnboardingModel;
import com.client.thegrocers.home.HomeActivity;
import com.github.ybq.android.spinkit.SpinKitView;

import net.khirr.android.privacypolicy.PrivacyPolicyDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.onboardingPager)
    LoopingViewPager onboardingPager;
    @BindView(R.id.splash_screen)
    ImageView splashScreen;
    OnboardingViewModel onboardingViewModel;
    @BindView(R.id.nextBtn_onboarding)
    Button nextBtn;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    @BindView(R.id.pager_layout)
    LinearLayout pagerLayout;
    @BindView(R.id.spin_kit_main)
    SpinKitView loadingMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onboardingViewModel = ViewModelProviders.of(this).get(OnboardingViewModel.class);
        unbinder = ButterKnife.bind(this);
        handler.postDelayed(runnable,2000);

    }

    private void initOnBoarding() {
//        AlertDialog alertDialog = new SpotsDialog.Builder().setContext(MainActivity.this).setCancelable(false).build();
//        alertDialog.setMessage("Checking user info...");
//        alertDialog.show();
        loadingMain.setVisibility(View.VISIBLE);
        onboardingViewModel.getMutableLiveDataOnboarding().observe(this, new Observer<List<OnboardingModel>>() {
            @Override
            public void onChanged(List<OnboardingModel> onboardingModelList) {
                OnboardingAdapter onboardingAdapter = new OnboardingAdapter(MainActivity.this,onboardingModelList,false);
                onboardingPager.setAdapter(onboardingAdapter);
                loadingMain.setVisibility(View.INVISIBLE);
                onboardingPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        int adapterFinalPos = onboardingModelList.size() -1;
                        if (position == adapterFinalPos){
                            nextBtn.setText("GET STARTED");
                        }else {
                            nextBtn.setText("NEXT");
                        }

                        switch (position){
                            case 0:
                                mainLayout.setBackgroundColor(Color.parseColor("#DF7070"));
                                break;
                            case 1:
                                mainLayout.setBackgroundColor(Color.parseColor("#010313"));
                                break;
                            case 2:
                                mainLayout.setBackgroundColor(Color.parseColor("#3CDE42"));
                                break;
                        }
                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onboardingPager.getCurrentItem() != onboardingModelList.size() -1){
                            onboardingPager.setCurrentItem(onboardingPager.getCurrentItem() + 1);
                        }else {
                            showPrivacyDialog();
                        }
                    }
                });
            }
        });
    }

    private void showPrivacyDialog() {
        PrivacyPolicyDialog dialog = new PrivacyPolicyDialog(this,
                "https://thegrocers101.github.io/Shipping-And-Delivery-Policy/",
                "https://thegrocers101.github.io/PrivacyPolicy/");
        dialog.addPoliceLine("This application uses location services for detecting the country code for sending OTP message and for setting up the delivery address if the user wants to");
        dialog.addPoliceLine("This application requires internet access to receive and send data");
        dialog.addPoliceLine("This application send you text messages regarding the order placed");
        dialog.addPoliceLine("All details about the use of data are available in our Privacy Policies, as well as all Terms of Service links below.");
        dialog.setOnClickListener(new PrivacyPolicyDialog.OnClickListener() {
            @Override
            public void onAccept(boolean isFirstTime) {
                PrefsUtills.saveFirstTimeUser("YES", MainActivity.this);
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Policies Not Accepted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        dialog.setTitleTextColor(Color.parseColor("#222222"));
        dialog.setAcceptButtonColor(ContextCompat.getColor(this, R.color.colorAccent));

        //  Title
        dialog.setTitle("Terms of Service");

        //  {terms}Terms of Service{/terms} is replaced by a link to your terms
        //  {privacy}Privacy Policy{/privacy} is replaced by a link to your privacy policy
        dialog.setTermsOfServiceSubtitle("If you click on {accept}, you acknowledge that it makes the content present and all the content of our {terms}Terms of Service{/terms} and implies that you have read our {privacy}Privacy Policy{privacy}.");
        dialog.show();
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (PrefsUtills.getUserStats(MainActivity.this)!= null){
                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                finish();
            }else {
                initOnBoarding();
                splashScreen.setVisibility(View.GONE);
                pagerLayout.setVisibility(View.VISIBLE);

            }
        }
    };
}