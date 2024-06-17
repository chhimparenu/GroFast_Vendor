package com.wits.grofast_vendor.Details;

import static com.wits.grofast_vendor.CommonUtilities.getPathFromUri;
import static com.wits.grofast_vendor.CommonUtilities.handleApiError;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.wits.grofast_vendor.Api.Interface.UserInterface;
import com.wits.grofast_vendor.Api.Model.SupplierModel;
import com.wits.grofast_vendor.Api.Response.EditProfileResponse;
import com.wits.grofast_vendor.Api.Retrofirinstance;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierActivitySession;
import com.wits.grofast_vendor.session.SupplierDetailSession;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Log.e(TAG,"Profile Image"+supplierDetailSession.getImage());
        country.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, countrylist));
        city.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, citylist));
        state.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, statelist));
        pincode.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, pincodelist));

        addProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.edit_profile_image_heading).setItems(new String[]{getString(R.string.change_image), getString(R.string.remove_image)}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openGallery();
                        break;
                    case 1:
                        showProfileImage.setImageResource(R.drawable.account);
                        showAddProfileButton();
                        image = null;
                        isRemoveProfile = true;
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void updateUserProfile() {
        String selectedGender = null;

        if (radioMale.isChecked()) {
            selectedGender = radioMale.getText().toString();
        } else if (radioFemale.isChecked()) {
            selectedGender = radioFemale.getText().toString();
        } else if (radioOther.isChecked()) {
            selectedGender = radioOther.getText().toString();
        }

        String uname = name.getText().toString().trim();
        String uemail = email.getText().toString().trim();

        if (uname.isEmpty()) {
            showToastAndFocus(getString(R.string.toast_message_enter_name), name);
            return;
        }

        if (selectedGender == null) {
            showToastAndFocus(getString(R.string.toast_message_select_gender), radioMale);
            return;
        }

        if (uemail.isEmpty()) {
            showToastAndFocus(getString(R.string.toast_message_enter_email), email);
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(uemail).matches()) {
            showToastAndFocus(getString(R.string.toast_message_enter_valid_email), email);
            return;
        }


        RequestBody phoneNo = RequestBody.create(MediaType.parse("text/plain"), tvPhone.getText().toString());
        RequestBody name1 = RequestBody.create(MediaType.parse("text/plain"), name.getText().toString());
        RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), email.getText().toString());

        if (selectedGender != null) {
            RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), selectedGender);
            Call<EditProfileResponse> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(UserInterface.class).updateProfile(phoneNo, name1, email1, gender, image);
            scrollView.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
            loadingOverlay.setVisibility(View.VISIBLE);

//            removeProfile();

            call.enqueue(new Callback<EditProfileResponse>() {
                @Override
                public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                    scrollView.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.VISIBLE);
                    loadingOverlay.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        EditProfileResponse editProfileResponse = response.body();
                        SupplierModel userModel = editProfileResponse.getUserProfile();

                        if (userModel != null) {
                            supplierDetailSession.setImage(userModel.getImage());
                            supplierDetailSession.setName(userModel.getName());
                            supplierDetailSession.setEmail(userModel.getEmail());
                            supplierDetailSession.setGender(userModel.getGender());
                        }
                        Toast.makeText(ProfilePage.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else handleApiError(TAG, response, getApplicationContext());
                }

                @Override
                public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                    t.printStackTrace();
                    scrollView.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.VISIBLE);
                    loadingOverlay.setVisibility(View.GONE);
                }
            });
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                showProfileImage.setImageBitmap(bitmap);
                showEditProfileButton();
                imageFile = new File(getPathFromUri(getApplicationContext(), data.getData()));
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                image = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showToastAndFocus(String message, View view) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        view.requestFocus();
        showKeyboard(view);
    }

    private void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
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