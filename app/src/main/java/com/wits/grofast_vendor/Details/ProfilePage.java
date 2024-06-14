package com.wits.grofast_vendor.Details;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.textfield.TextInputEditText;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierActivitySession;
import com.wits.grofast_vendor.session.SupplierDetailSession;
import java.io.File;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;

public class ProfilePage extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private CircleImageView showProfileImage;
    private AppCompatButton addProfileImage, editProfileImage, changephonenumber;
    private MultipartBody.Part image;
    private File imageFile;
    private final String TAG = "EditProfile";
    private RadioButton radioMale, radioFemale, radioOther;
    private TextInputEditText email,storeaddress,pincode,city,state,country;
    private TextView tvPhone;
    NestedScrollView scrollView;
    private SupplierActivitySession supplierActivitySession;
    private SupplierDetailSession supplierDetailSession;
    LinearLayout loadingOverlay;
    private final int defaultImage = R.drawable.account;
    private boolean isRemoveProfile = false;
    private long COUNTDOWN_TIME_MILLIS = 30000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.edit_profile_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_profile_page);


    }
}