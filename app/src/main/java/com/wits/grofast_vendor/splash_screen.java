package com.wits.grofast_vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.wits.grofast_vendor.Homepage.Home_page;
import com.wits.grofast_vendor.session.SupplierActivitySession;

public class splash_screen extends AppCompatActivity {
    private static final int SPLASH_SCREEN = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.splash_screen);

        SupplierActivitySession session = new SupplierActivitySession(getApplicationContext());
        Intent in;
        if (session.isUserLoggedIn()) {
            in = new Intent(getApplicationContext(), Home_page.class);
        } else {
            in = new Intent(getApplicationContext(), Login_page.class);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
            }
        }, SPLASH_SCREEN);
    }
}