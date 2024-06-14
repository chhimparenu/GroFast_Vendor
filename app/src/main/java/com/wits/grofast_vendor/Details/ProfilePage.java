package com.wits.grofast_vendor.Details;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierActivitySession;
import com.wits.grofast_vendor.session.SupplierDetailSession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;

public class ProfilePage extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private CircleImageView showProfileImage;
    private AppCompatButton addProfileImage, editProfileImage, changephonenumber, saveButton;
    private MultipartBody.Part image;
    private File imageFile;
    private final String TAG = "EditProfile";
    private RadioButton radioMale, radioFemale, radioOther;
    private TextInputEditText email, storeaddress, name, storename;
    AppCompatSpinner pincode, city, state, country;
    private TextView tvPhone;
    NestedScrollView scrollView;
    private SupplierActivitySession supplierActivitySession;
    private SupplierDetailSession supplierDetailSession;
    LinearLayout loadingOverlay;
    private final int defaultImage = R.drawable.account;
    private boolean isRemoveProfile = false;
    private long COUNTDOWN_TIME_MILLIS = 30000;
    List<String> countrylist = new ArrayList<>(), statelist = new ArrayList<>(), citylist = new ArrayList<>(), pincodelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.edit_profile_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_profile_page);

        supplierDetailSession = new SupplierDetailSession(getApplicationContext());
        supplierActivitySession = new SupplierActivitySession(getApplicationContext());

        showProfileImage = findViewById(R.id.show_profile_image);

        //Button
        addProfileImage = findViewById(R.id.add_profile_image);
        editProfileImage = findViewById(R.id.edit_profile_image);
        saveButton = findViewById(R.id.saveButton);
        changephonenumber = findViewById(R.id.change_phone_number);

        scrollView = findViewById(R.id.nestedScrollView);
        loadingOverlay = findViewById(R.id.loading_edit);

        //radio button
        radioMale = findViewById(R.id.rb_male);
        radioFemale = findViewById(R.id.rb_female);
        radioOther = findViewById(R.id.rb_other);

        //edittext
        storename = findViewById(R.id.edit_store_name);
        storeaddress = findViewById(R.id.edit_store_address);
        name = findViewById(R.id.edit_name);
        email = findViewById(R.id.edit_email);

        //Textview
        tvPhone = findViewById(R.id.show_phone_no);

        //Spinner
        country = findViewById(R.id.edit_profile_country_spinner);
        state = findViewById(R.id.edit_profile_state_spinner);
        city = findViewById(R.id.edit_profile_city_spinner);
        pincode = findViewById(R.id.edit_profile_pincode_pincode);

        name.setText(supplierDetailSession.getName());
        storename.setText(supplierDetailSession.getStorrname());
        storeaddress.setText(supplierDetailSession.getStoreAddress());
        email.setText(supplierDetailSession.getEmail());
        tvPhone.setText(supplierDetailSession.getPhoneNo());
        String image = supplierDetailSession.getImage();
        Glide.with(getApplicationContext()).load(image).placeholder(defaultImage).into(showProfileImage);
        if (Uri.parse(image).getLastPathSegment().equals("null")) {
            showAddProfileButton();
        } else showEditProfileButton();

        countrylist.add(supplierDetailSession.getCountry());
        statelist.add(supplierDetailSession.getState());
        citylist.add(supplierDetailSession.getCity());
        pincodelist.add(supplierDetailSession.getPincode());

        Log.e(TAG, "Gender " + supplierDetailSession.getGender());
        switch (supplierDetailSession.getGender()) {
            case "Male":
                radioMale.setChecked(true);
                break;
            case "Female":
                radioFemale.setChecked(true);
                break;
            case "Other":
                radioOther.setChecked(true);
                break;
        }
        country.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, countrylist));
        city.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, citylist));
        state.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, statelist));
        pincode.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, pincodelist));
    }

    private void showEditProfileButton() {
        addProfileImage.setVisibility(View.GONE);
        editProfileImage.setVisibility(View.VISIBLE);
    }

    private void showAddProfileButton() {
        editProfileImage.setVisibility(View.GONE);
        addProfileImage.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}