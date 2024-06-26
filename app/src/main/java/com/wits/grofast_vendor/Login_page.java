package com.wits.grofast_vendor;

import static android.content.ContentValues.TAG;

import static com.wits.grofast_vendor.CommonUtilities.handleApiError;
import static com.wits.grofast_vendor.CommonUtilities.setEditTextListeners;
import static com.wits.grofast_vendor.CommonUtilities.startCountdown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wits.grofast_vendor.Api.Retrofirinstance;
import com.wits.grofast_vendor.Homepage.Home_page;
import com.wits.grofast_vendor.Api.Interface.UserInterface;
import com.wits.grofast_vendor.Api.Model.SupplierModel;
import com.wits.grofast_vendor.Api.Response.LoginResponse;
import com.wits.grofast_vendor.Api.Response.OtpResponse;
import com.wits.grofast_vendor.Policy.PrivacyPolicyActivity;
import com.wits.grofast_vendor.Policy.ReturnPolicy;
import com.wits.grofast_vendor.Policy.TermsConditionPolicy;
import com.wits.grofast_vendor.session.SupplierActivitySession;
import com.wits.grofast_vendor.session.SupplierDetailSession;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_page extends AppCompatActivity {
    AppCompatButton Login;
    EditText phoneNo;
    String enteredPhone, enteredOtp = "";
    SupplierActivitySession session;
    SupplierDetailSession supplierDetailSession;
    ProgressBar progressBar;
    TextView privacy_policy, return_plocy, terms_condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login_page);

        Login = findViewById(R.id.login_button);
        phoneNo = findViewById(R.id.phone_no);
        progressBar = findViewById(R.id.loading_login);
        privacy_policy = findViewById(R.id.text_privacy_policy);
        return_plocy = findViewById(R.id.text_return_policy);
        terms_condition = findViewById(R.id.text_terms_policy);
        session = new SupplierActivitySession(getApplicationContext());
        supplierDetailSession = new SupplierDetailSession(getApplicationContext());

        return_plocy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ReturnPolicy.class);
                startActivity(in);
            }
        });

        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
                startActivity(in);
            }
        });

        terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), TermsConditionPolicy.class);
                startActivity(in);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve Phone Number
                enteredPhone = phoneNo.getText().toString().trim();
                //Validation
                if (enteredPhone.isEmpty()) {
                    showToastAndFocus(getString(R.string.toast_message_enter_number));
                } else if (!isValidPhoneNumber(enteredPhone)) {
                    showToastAndFocus(getString(R.string.toast_message_valid_number));
                } else {
                    Log.e(TAG, "onClick: phone no " + enteredPhone);    //Log Phone number
                    UserInterface userInterface = Retrofirinstance.getUnAuthorizedClient().create(UserInterface.class);  //API call
                    Call<LoginResponse> call = userInterface.Login(enteredPhone);

                    progressBar.setVisibility(View.VISIBLE);
                    Login.setVisibility(View.GONE);

                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            progressBar.setVisibility(View.GONE);
                            Login.setVisibility(View.VISIBLE);
                            if (response.isSuccessful()) {
                                LoginResponse loginResponse = response.body();
                                Toast.makeText(getApplicationContext(), "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "onResponse: message " + loginResponse.getMessage());
                                Log.i(TAG, "onResponse: phoneNo " + loginResponse.getPhone_no());
                                openOtpPage(enteredPhone);
                            } else {
                                handleApiError(TAG, response, getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            t.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Login.setVisibility(View.VISIBLE);
                        }
                    });

                }
            }
        });
    }

    EditText digit1, digit2, digit3, digit4;
    AppCompatButton Continue_otp_page, resend;
    TextView textphone, countDownTimer;
    long COUNTDOWN_TIME_MILLIS = 30000;

    private void openOtpPage(String phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login_page.this);
        View dialogView = getLayoutInflater().inflate(R.layout.otp_page, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        ImageView close_change_phone_number;
        textphone = dialogView.findViewById(R.id.otp_phone_no);
        textphone.setText(phone);
        resend = dialogView.findViewById(R.id.resend_otp_button);
        countDownTimer = dialogView.findViewById(R.id.countdown_timer);

        digit1 = dialogView.findViewById(R.id.otp_digit1);
        digit2 = dialogView.findViewById(R.id.otp_digit2);
        digit3 = dialogView.findViewById(R.id.otp_digit3);
        digit4 = dialogView.findViewById(R.id.otp_digit4);
        Continue_otp_page = dialogView.findViewById(R.id.Continue_otp_page);

        startCountdown(resend, countDownTimer, getApplicationContext(), COUNTDOWN_TIME_MILLIS);
        setEditTextListeners(digit1, digit2, digit3, digit4);

        close_change_phone_number = dialogView.findViewById(R.id.close_change_phone_number);
        close_change_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Continue_otp_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), Home_page.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                enteredOtp = digit1.getText().toString().trim() + digit2.getText().toString().trim() + digit3.getText().toString().trim() + digit4.getText().toString().trim();
                Log.e(TAG, "onCreate: enteredOtp " + enteredOtp);
                Log.e(TAG, "onCreate: enteredPhone_no " + phone);
                if (isOtpValid()) {
                    Integer userOtp = Integer.parseInt(enteredOtp);
                    Call<OtpResponse> call = Retrofirinstance.getUnAuthorizedClient().create(UserInterface.class).Otp(phone, userOtp);
                    call.enqueue(new Callback<OtpResponse>() {
                        @Override
                        public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                            if (response.isSuccessful()) {
                                OtpResponse otpVerifyResponse = response.body();
                                SupplierModel supplierModel = otpVerifyResponse.getUser();

                                Log.e(TAG, "id " + supplierModel.getId());
                                Log.e(TAG, "phone no " + supplierModel.getMobile_number());
                                session.setLoginStaus(true);
                                session.setToken(otpVerifyResponse.getAccessToken());

                                supplierDetailSession.setId(supplierModel.getId());
                                supplierDetailSession.setUuid(supplierModel.getUuid());
                                supplierDetailSession.setImage(supplierModel.getImage());
                                supplierDetailSession.setName(supplierModel.getName());
                                supplierDetailSession.setStoreName(supplierModel.getStore_name());
                                supplierDetailSession.setEmail(supplierModel.getEmail());
                                supplierDetailSession.setPhoneNo(supplierModel.getMobile_number());
                                supplierDetailSession.setDescription(supplierModel.getDescription());
                                supplierDetailSession.setStoreAddress(supplierModel.getStore_address());
                                supplierDetailSession.setPincode(supplierModel.getPin_code());
                                supplierDetailSession.setCiy(supplierModel.getCity());
                                supplierDetailSession.setState(supplierModel.getState());
                                supplierDetailSession.setCountry(supplierModel.getCountry());
                                supplierDetailSession.setStatus(supplierModel.getStatus());
                                supplierDetailSession.setGender(supplierModel.getGender());

                                startActivity(in);
                                Toast.makeText(Login_page.this, "" + otpVerifyResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "code " + response.code());
                                handleApiError(TAG, response, getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<OtpResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                } else {
                    showToastAndFocus(getString(R.string.toast_message_enter_otp));
                }
            }

        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer.getText().toString().equals("00:00")) {
                    Call<LoginResponse> call = Retrofirinstance.getUnAuthorizedClient().create(UserInterface.class).Login(phone);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                startCountdown(resend, countDownTimer, getApplicationContext(), COUNTDOWN_TIME_MILLIS);
                                LoginResponse loginResponse = response.body();
                                if (loginResponse != null) {
                                    Toast.makeText(getApplicationContext(), "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                handleApiError(TAG, response, getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_message_resend_otp), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialoguebox_baclground);
        }
        dialog.show();
    }

    private boolean isOtpValid() {
        boolean valid = true;
        if (digit1.getText().toString().trim().isEmpty()) {
            valid = false;
        }

        if (digit2.getText().toString().trim().isEmpty()) {
            valid = false;
        }
        if (digit3.getText().toString().trim().isEmpty()) {
            valid = false;
        }

        if (digit4.getText().toString().trim().isEmpty()) {
            valid = false;
        }

        return valid;
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.length() == 10 && phone.matches("\\d+");
    }

    private void showToastAndFocus(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        phoneNo.requestFocus();
        showKeyboard(phoneNo);
    }

    private void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}

