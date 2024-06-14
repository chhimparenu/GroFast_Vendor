package com.wits.grofast_vendor.Homepage;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.wits.grofast_vendor.Details.Notification;
import com.wits.grofast_vendor.Details.Notification_setting;
import com.wits.grofast_vendor.Details.ProfilePage;
import com.wits.grofast_vendor.Details.Settings;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierDetailSession;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_page extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private DrawerLayout drawerLayout;
    private View headerView;
    private TextView userName, userPhoneNo;
    private CircleImageView userProfile;
    NavigationView navigationView;
    ImageView menu;
    SupplierDetailSession session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_page);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.venfragmentn);
        menu = findViewById(R.id.menu_bar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        session = new SupplierDetailSession(getApplicationContext());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.venhome) {
                    loadFragment(new Home_Fragment(), true);
                } else if (id == R.id.venproduct) {
                    loadFragment(new Product_Fragment(), false);
                } else if (id == R.id.venorder) {
                    loadFragment(new Order_Fragment(), false);
                }
                return true;
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        if (navigationView != null) {
            headerView = navigationView.getHeaderView(0);
        } else {
            Log.e("Home_page", "NavigationView is null");
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_edit_profile) {
                    startActivity(new Intent(Home_page.this, ProfilePage.class));
                } else if (id == R.id.menu_notification) {
                    startActivity(new Intent(Home_page.this, Notification.class));
                } else if (id == R.id.menu_notification_setting) {
                    startActivity(new Intent(Home_page.this, Notification_setting.class));
                } else if (id == R.id.menu_setting) {
                    startActivity(new Intent(Home_page.this, Settings.class));
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        // Set default fragment when activity is first created
        bottomNavigationView.setSelectedItemId(R.id.venhome);
    }
    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.venfragmentn, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
    @Override
    protected void onStart() {
        super.onStart();
        userName = headerView.findViewById(R.id.user_name);
        userPhoneNo = headerView.findViewById(R.id.user_phone_no);
        userProfile = headerView.findViewById(R.id.user_profile);

        userPhoneNo.setText(session.getPhoneNo());
        String name = session.getName();
        if (name == null || name.isEmpty()) {
            name = getString(R.string.your_name);
            userName.setTextColor(getResources().getColor(R.color.default_color));
        } else {
            userName.setTextColor(getResources().getColor(R.color.white));
        }
        userName.setText(name);
        Glide.with(getApplicationContext()).load(session.getImage()).placeholder(R.drawable.account).into(userProfile);
    }
}
