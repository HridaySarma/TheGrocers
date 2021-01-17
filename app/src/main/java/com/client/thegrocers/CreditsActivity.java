
package com.client.thegrocers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.yuvraj.thegroceryapp.home.HomeActivity;

public class CreditsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(com.yuvraj.thegroceryapp.CreditsActivity.this, HomeActivity.class));
        finish();
        super.onBackPressed();
    }
}