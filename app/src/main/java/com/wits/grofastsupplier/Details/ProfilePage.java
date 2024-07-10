package com.wits.grofastsupplier.Details;


import static com.wits.grofastsupplier.CommonUtilities.getPathFromUri;
import static com.wits.grofastsupplier.CommonUtilities.getSelectedSpinnerItemPosition;
import static com.wits.grofastsupplier.CommonUtilities.handleApiError;
import static com.wits.grofastsupplier.CommonUtilities.setEditTextListeners;
import static com.wits.grofastsupplier.CommonUtilities.startCountdown;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.wits.grofastsupplier.Adapter.CustomSpinnerAdapter;
import com.wits.grofastsupplier.Api.Interface.AddressInterface;
import com.wits.grofastsupplier.Api.Interface.UserInterface;
import com.wits.grofastsupplier.Api.Model.SpinnerItemModel;
import com.wits.grofastsupplier.Api.Model.SpinnerModel;
import com.wits.grofastsupplier.Api.Model.SupplierModel;
import com.wits.grofastsupplier.Api.Response.LoginResponse;
import com.wits.grofastsupplier.Api.Response.SupplierProfileResponse;
import com.wits.grofastsupplier.Api.Retrofirinstance;
import com.wits.grofastsupplier.R;
import com.wits.grofastsupplier.session.SupplierActivitySession;
import com.wits.grofastsupplier.session.SupplierDetailSession;

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
    private TextInputEditText email, storeaddress, name, storename, description;
    private AppCompatSpinner pincodeSpinner, citySpinner, stateSpinner, countrySpinner;
    private CustomSpinnerAdapter countryAdapter, stateAdapter, cityAdapter, pincodeAdapter;
    private TextView tvPhone;
    NestedScrollView scrollView;
    private SupplierActivitySession supplierActivitySession;
    private SupplierDetailSession supplierDetailSession;
    LinearLayout loadingOverlay;
    private final int defaultImage = R.drawable.account;
    private boolean isRemoveProfile = false;
    private final long COUNTDOWN_TIME_MILLIS = 30000;
    private String selectedCountry, selectedState, selectedCity, selectedPincode;
    private List<SpinnerItemModel> countryList, stateList, citylist, pincodeList;
    List<SpinnerModel> countrySpinnerList = new ArrayList<>();
    List<SpinnerModel> stateSpinnerList = new ArrayList<>();
    List<SpinnerModel> citySpinnerList = new ArrayList<>();
    List<SpinnerModel> pincodeSpinnerList = new ArrayList<>();
    private boolean isFirstTimeAutoset = true;

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
        description = findViewById(R.id.edit_description);


        //Textview
        tvPhone = findViewById(R.id.show_phone_no);

        //Spinner
        countrySpinner = findViewById(R.id.edit_profile_country_spinner);
        stateSpinner = findViewById(R.id.edit_profile_state_spinner);
        citySpinner = findViewById(R.id.edit_profile_city_spinner);
        pincodeSpinner = findViewById(R.id.edit_profile_pincode_pincode);

        name.setText(supplierDetailSession.getName());
        storename.setText(supplierDetailSession.getStoreName());
        storeaddress.setText(supplierDetailSession.getStoreAddress());
        email.setText(supplierDetailSession.getEmail());
        tvPhone.setText(supplierDetailSession.getPhoneNo());
        description.setText(supplierDetailSession.getDescription());
        String image = supplierDetailSession.getImage();
        Glide.with(getApplicationContext()).load(image).placeholder(defaultImage).into(showProfileImage);
        if (Uri.parse(image).getLastPathSegment().equals("null")) {
            showAddProfileButton();
        } else showEditProfileButton();

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
        Log.e(TAG, "Profile Image" + supplierDetailSession.getImage());

        getCountries();
        countryAdapter = new CustomSpinnerAdapter(getApplicationContext(), countrySpinnerList, getString(R.string.hint_select_country));
        stateAdapter = new CustomSpinnerAdapter(getApplicationContext(), stateSpinnerList, getString(R.string.hint_select_state));
        cityAdapter = new CustomSpinnerAdapter(getApplicationContext(), citySpinnerList, getString(R.string.hint_select_city));
        pincodeAdapter = new CustomSpinnerAdapter(getApplicationContext(), pincodeSpinnerList, getString(R.string.hint_select_pincode));

        countrySpinner.setAdapter(countryAdapter);
        stateSpinner.setAdapter(stateAdapter);
        citySpinner.setAdapter(cityAdapter);
        pincodeSpinner.setAdapter(pincodeAdapter);


        addProfileImage.setOnClickListener(v -> openGallery());

        editProfileImage.setOnClickListener(v -> showDialog());

        saveButton.setOnClickListener(v -> updateUserProfile());
        changephonenumber.setOnClickListener(v -> OpenChangePhoneNumberDialog());

        countrySpinner.setOnItemSelectedListener(getCountrySpinnerListner());
        stateSpinner.setOnItemSelectedListener(getStateSpinnerListner());
        citySpinner.setOnItemSelectedListener(getCitySpinnerListner());
        pincodeSpinner.setOnItemSelectedListener(getPincodeSpinnerListner());

    }

    private void OpenChangePhoneNumberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.change_phone_number_layout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        ImageView close = dialogView.findViewById(R.id.close_change_phone_number);
        EditText phone = dialogView.findViewById(R.id.edit_phone_no);
        AppCompatButton changenumber = dialogView.findViewById(R.id.change_number);
        ProgressBar progressBar = dialogView.findViewById(R.id.loader_edit_phone_number);
        String currentPhoneNumber = supplierDetailSession.getPhoneNo();
        phone.setText(currentPhoneNumber);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        changenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPhoneNumber = phone.getText().toString().trim();
                if (newPhoneNumber.equals(currentPhoneNumber)) {
                    Toast.makeText(ProfilePage.this, getString(R.string.toast_message_new_phone), Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    changenumber.setVisibility(View.GONE);
                    sendOtp(newPhoneNumber, dialog);

                }
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dailogbox_background);
        }
        dialog.show();
    }

    private void openOtpPage(String phone) {
        EditText digit1, digit2, digit3, digit4;
        AppCompatButton resentOtp, continueButton;
        TextView phoneNo, countDownTimer;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.otp_page, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        ImageView close_change_phone_number;


        close_change_phone_number = dialogView.findViewById(R.id.close_change_phone_number);
        close_change_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        phoneNo = dialogView.findViewById(R.id.otp_phone_no);
        phoneNo.setText(phone);

        digit1 = dialogView.findViewById(R.id.otp_digit1);
        digit2 = dialogView.findViewById(R.id.otp_digit2);
        digit3 = dialogView.findViewById(R.id.otp_digit3);
        digit4 = dialogView.findViewById(R.id.otp_digit4);

        resentOtp = dialogView.findViewById(R.id.resend_otp_button);
        continueButton = dialogView.findViewById(R.id.Continue_otp_page);
        countDownTimer = dialogView.findViewById(R.id.countdown_timer);

        startCountdown(resentOtp, countDownTimer, getApplicationContext(), COUNTDOWN_TIME_MILLIS);
        setEditTextListeners(digit1, digit2, digit3, digit4);

        resentOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer.getText().toString().equals("00:00")) {
                    loadingOverlay.setVisibility(View.VISIBLE);
                    startCountdown(resentOtp, countDownTimer, getApplicationContext(), COUNTDOWN_TIME_MILLIS);
                    sendOtp(phone, null);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_message_resend_otp), Toast.LENGTH_SHORT).show();
                }
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userOtp = digit1.getText().toString() + digit2.getText().toString() + digit3.getText().toString() + digit4.getText().toString();
                verifyOtp(phone, userOtp, dialog);
            }
        });
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dailogbox_background);
        }
        dialog.show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.edit_profile_image_heading).setItems(new String[]{getString(R.string.change_image), getString(R.string.remove_image)}, (dialog, which) -> {
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
        String udescription = description.getText().toString().trim();

        String uestorename = storename.toString().trim();
        String uestoreaddress = storeaddress.toString().trim();

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

        if (udescription.isEmpty()) {
            showToastAndFocus(getString(R.string.toast_message_enter_description), description);
            return;
        }

        if (uestoreaddress.isEmpty()) {
            showToastAndFocus(getString(R.string.toast_message_enter_storeaddress), storeaddress);
            return;
        }

        if (uestorename.isEmpty()) {
            showToastAndFocus(getString(R.string.toast_message_enter_storename), countrySpinner);
            return;
        }

        if (!validateAllSpinners()) {
            Log.e(TAG, "updateUserProfile: spinners not validated");
            return;
        }
        RequestBody evphoneno = RequestBody.create(MediaType.parse("text/plain"), tvPhone.getText().toString());
        RequestBody evname = RequestBody.create(MediaType.parse("text/plain"), name.getText().toString());
        RequestBody evdescription = RequestBody.create(MediaType.parse("text/plain"), description.getText().toString());
        RequestBody evemail = RequestBody.create(MediaType.parse("text/plain"), email.getText().toString());
        RequestBody evstorename = RequestBody.create(MediaType.parse("text/plain"), storename.getText().toString());
        RequestBody evstoreaddress = RequestBody.create(MediaType.parse("text/plain"), storeaddress.getText().toString());

//        RequestBody evpincode = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(uepincode.));
            RequestBody evcountry = RequestBody.create(MediaType.parse("text/plain"), selectedCountry);
            RequestBody evstate = RequestBody.create(MediaType.parse("text/plain"), selectedState);
            RequestBody evcity = RequestBody.create(MediaType.parse("text/plain"), selectedCity);
            RequestBody evpincode = RequestBody.create(MediaType.parse("text/plain"), selectedPincode);


        if (selectedGender != null) {
            RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), selectedGender);
            Call<SupplierProfileResponse> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(UserInterface.class).updateProfile(evname, evemail, evphoneno, evdescription, evstoreaddress, evpincode, evcity, evstate, evcountry, evstorename, gender,image);
            scrollView.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
            loadingOverlay.setVisibility(View.VISIBLE);

//            removeProfile();

            call.enqueue(new Callback<SupplierProfileResponse>() {
                @Override
                public void onResponse(Call<SupplierProfileResponse> call, Response<SupplierProfileResponse> response) {
                    scrollView.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.VISIBLE);
                    loadingOverlay.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        SupplierProfileResponse supplierProfileResponse = response.body();
                        SupplierModel supplierModel = supplierProfileResponse.getSupplierprofile();
                        Log.d(TAG, supplierProfileResponse.getMessage());
                        Log.d(TAG, "" + supplierProfileResponse.getStatus());

                        if (supplierModel != null) {
                            supplierDetailSession.setId(supplierDetailSession.getId());
                            supplierDetailSession.setImage(supplierModel.getImage());
                            supplierDetailSession.setName(supplierModel.getName());
                            supplierDetailSession.setEmail(supplierModel.getEmail());
                            supplierDetailSession.setGender(supplierModel.getGender());
                            supplierDetailSession.setDescription(supplierModel.getDescription());
                            supplierDetailSession.setCountry(supplierModel.getCountry());
                            supplierDetailSession.setState(supplierModel.getState());
                            supplierDetailSession.setCiy(supplierModel.getCity());
                            supplierDetailSession.setPincode(supplierModel.getPin_code());
                            supplierDetailSession.setStoreAddress(supplierModel.getStore_address());
                            supplierDetailSession.setStoreName(supplierModel.getStore_name());
                            supplierDetailSession.setPhoneNo(supplierModel.getMobile_number());
                        }
                        Toast.makeText(ProfilePage.this, supplierProfileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else handleApiError(TAG, response, getApplicationContext());
                }

                @Override
                public void onFailure(Call<SupplierProfileResponse> call, Throwable t) {
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

    @SuppressLint("ShowToast")
    private boolean validateSpinner(AppCompatSpinner spinner, int message) {
        SpinnerModel model = (SpinnerModel) spinner.getSelectedItem();
        if (model == null || model.getName() == null) {
            Toast.makeText(getApplicationContext(), getString(message), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateAllSpinners() {
        return validateSpinner(countrySpinner, R.string.toast_select_country) && validateSpinner(stateSpinner, R.string.toast_select_state) && validateSpinner(citySpinner, R.string.toast_select_city) && validateSpinner(pincodeSpinner, R.string.toast_select_pincode);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCountries() {
        Call<List<SpinnerItemModel>> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(AddressInterface.class).getCountries();

        call.enqueue(new Callback<List<SpinnerItemModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerItemModel>> call, Response<List<SpinnerItemModel>> response) {
                if (response.isSuccessful()) {
                    countryList = response.body();

                    countrySpinnerList.clear();
                    for (SpinnerItemModel model : countryList) {
                        countrySpinnerList.add(new SpinnerModel(model.getName(), model.getId()));
                    }

                    countryAdapter.notifyDataSetChanged();
                    if (isFirstTimeAutoset)
                        countrySpinner.setSelection(getSelectedSpinnerItemPosition(countrySpinnerList, supplierDetailSession.getCountry()));

                    if (countrySpinnerList.isEmpty()) {
                        stateSpinnerList.clear();
                        citySpinnerList.clear();
                        pincodeSpinnerList.clear();

                        stateSpinner.setSelection(0);
                        citySpinner.setSelection(0);
                        pincodeSpinner.setSelection(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerItemModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getStates(int countryId) {
        Call<List<SpinnerItemModel>> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(AddressInterface.class).getStates(countryId);

        call.enqueue(new Callback<List<SpinnerItemModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerItemModel>> call, Response<List<SpinnerItemModel>> response) {
                if (response.isSuccessful()) {
                    stateList = response.body();

                    stateSpinnerList.clear();
                    for (SpinnerItemModel model : stateList) {
                        stateSpinnerList.add(new SpinnerModel(model.getName(), model.getId()));
                    }

                    stateAdapter.notifyDataSetChanged();
                    if (isFirstTimeAutoset)
                        stateSpinner.setSelection(getSelectedSpinnerItemPosition(stateSpinnerList, supplierDetailSession.getState()));

                    if (stateSpinnerList.isEmpty()) {
                        citySpinnerList.clear();
                        pincodeSpinnerList.clear();

                        citySpinner.setSelection(0);
                        pincodeSpinner.setSelection(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerItemModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getCities(int stateId) {
        Call<List<SpinnerItemModel>> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(AddressInterface.class).getCities(stateId);

        call.enqueue(new Callback<List<SpinnerItemModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerItemModel>> call, Response<List<SpinnerItemModel>> response) {
                if (response.isSuccessful()) {
                    citylist = response.body();

                    citySpinnerList.clear();
                    for (SpinnerItemModel model : citylist) {
                        citySpinnerList.add(new SpinnerModel(model.getName(), model.getId()));
                    }

                    cityAdapter.notifyDataSetChanged();
                    if (isFirstTimeAutoset)
                        citySpinner.setSelection(getSelectedSpinnerItemPosition(citySpinnerList, supplierDetailSession.getCity()));

                    if (citySpinnerList.isEmpty()) {
                        pincodeSpinnerList.clear();
                        pincodeSpinner.setSelection(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerItemModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getPincodes(int cityId) {
        Call<List<SpinnerItemModel>> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(AddressInterface.class).getPincodes(cityId);

        call.enqueue(new Callback<List<SpinnerItemModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerItemModel>> call, Response<List<SpinnerItemModel>> response) {
                if (response.isSuccessful()) {
                    pincodeList = response.body();


                    pincodeSpinnerList.clear();
                    for (SpinnerItemModel model : pincodeList) {
                        pincodeSpinnerList.add(new SpinnerModel(model.getCode(), model.getId()));
                    }

                    pincodeAdapter.notifyDataSetChanged();
                    if (isFirstTimeAutoset) {
                        pincodeSpinner.setSelection(getSelectedSpinnerItemPosition(pincodeSpinnerList, supplierDetailSession.getPincode()));
                        isFirstTimeAutoset = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerItemModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private OnItemSelectedListener getCountrySpinnerListner() {
        return new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!countrySpinnerList.isEmpty()) {
                    SpinnerModel model = (SpinnerModel) countrySpinner.getSelectedItem();
                    if (model != null) {
                        selectedCountry = model.getName();
//                        Log.e(TAG, "onItemSelected: country : " + model.getName() + " id : " + model.getId());
                        getStates(model.getId());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private OnItemSelectedListener getStateSpinnerListner() {
        return new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!stateSpinnerList.isEmpty()) {
                    SpinnerModel model = (SpinnerModel) stateSpinner.getSelectedItem();
                    if (model != null) {
                        selectedState = model.getName();
//                        Log.e(TAG, "onItemSelected: state : " + model.getName() + " id : " + model.getId());
                        getCities(model.getId());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private OnItemSelectedListener getCitySpinnerListner() {
        return new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!citySpinnerList.isEmpty()) {
                    SpinnerModel model = (SpinnerModel) citySpinner.getSelectedItem();
                    if (model != null) {
                        selectedCity = model.getName();
//                        Log.e(TAG, "onItemSelected: city : " + model.getName() + " id : " + model.getId());
                        getPincodes(model.getId());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private OnItemSelectedListener getPincodeSpinnerListner() {
        return new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!pincodeSpinnerList.isEmpty()) {
                    SpinnerModel model = (SpinnerModel) pincodeSpinner.getSelectedItem();
                    if (model != null) {
                        selectedPincode = model.getName();
//                        Log.e(TAG, "onItemSelected: pincode : " + model.getName() + " id : " + model.getId());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private void sendOtp(String phone, AlertDialog dialog) {
        Call<LoginResponse> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(UserInterface.class).sendPhoneUpdateOtp(phone);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    openOtpPage(phone);
                    Toast.makeText(ProfilePage.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (dialog != null) dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void verifyOtp(String phone, String otp, AlertDialog dialog) {
        Call<LoginResponse> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(UserInterface.class).verifyPhoneUpdateOtp(phone, Integer.parseInt(otp));

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();

                    supplierDetailSession.setPhoneNo(loginResponse.getPhone_no());
                    tvPhone.setText(loginResponse.getPhone_no());
                    dialog.dismiss();
                    Toast.makeText(ProfilePage.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}