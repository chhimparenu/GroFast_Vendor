package com.wits.grofast_vendor.Details;

import static com.wits.grofast_vendor.CommonUtilities.handleApiError;
import static com.wits.grofast_vendor.CommonUtilities.showToast;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofast_vendor.Api.Interface.UserInterface;
import com.wits.grofast_vendor.Api.Response.LoginResponse;
import com.wits.grofast_vendor.Api.Retrofirinstance;
import com.wits.grofast_vendor.Login_page;
import com.wits.grofast_vendor.Policy.CancellationPolicy;
import com.wits.grofast_vendor.Policy.DeleteAccountPolicy;
import com.wits.grofast_vendor.Policy.DeleteDataPolicy;
import com.wits.grofast_vendor.Policy.PrivacyPolicyActivity;
import com.wits.grofast_vendor.Policy.RefundPolicy;
import com.wits.grofast_vendor.Policy.ReportPolicy;
import com.wits.grofast_vendor.Policy.ReturnPolicy;
import com.wits.grofast_vendor.Policy.TermsConditionPolicy;
import com.wits.grofast_vendor.R;
import com.wits.grofast_vendor.session.SupplierActivitySession;
import com.wits.grofast_vendor.session.SupplierDetailSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Settings extends AppCompatActivity {
    RadioButton englosh_rd;
    LinearLayout delete_account, privacy_policy, terms_condition_policy, delete_data_policy, delete_account_policy, refund_policy, cancellation_policy, report_policy, return_policy, delete_Account_message_layout;
    SupplierActivitySession supplierActivitySession;
    SupplierDetailSession supplierDetailSession;
    private final String TAG = "SettingPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.menu_setting));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_settings);

        englosh_rd = findViewById(R.id.language_english);
        englosh_rd.setChecked(true);
        delete_account = findViewById(R.id.delete_account);
        privacy_policy = findViewById(R.id.privacy_policy);
        terms_condition_policy = findViewById(R.id.terms_condition__policy);
        delete_data_policy = findViewById(R.id.delete_data_policy);
        delete_account_policy = findViewById(R.id.delete_account_policy);
        refund_policy = findViewById(R.id.refund_policy);
        cancellation_policy = findViewById(R.id.cancellation_policy);
        report_policy = findViewById(R.id.report_policy);
        return_policy = findViewById(R.id.return_policy);
        delete_Account_message_layout = findViewById(R.id.delete_Account_message_layout);

        supplierActivitySession = new SupplierActivitySession(this);
        supplierDetailSession = new SupplierDetailSession(this);

        privacy_policy.setOnClickListener(v -> startActivity(new Intent(Settings.this, PrivacyPolicyActivity.class)));
        terms_condition_policy.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), TermsConditionPolicy.class)));
        delete_data_policy.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), DeleteDataPolicy.class)));
        delete_account_policy.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), DeleteAccountPolicy.class)));
        refund_policy.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RefundPolicy.class)));
        return_policy.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ReturnPolicy.class)));
        cancellation_policy.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CancellationPolicy.class)));
        report_policy.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ReportPolicy.class)));
        delete_account.setOnClickListener(v -> DeleteAccountConfirmation());

        checkAndUpdateVisibility();
        Log.e(TAG, "onCreate: timme : "+ supplierActivitySession.getHour());

    }

    private void checkAndUpdateVisibility() {
        int storedHour = supplierActivitySession.getHour();
        if (storedHour != 0) {
            delete_Account_message_layout.setVisibility(View.VISIBLE);
            delete_account.setVisibility(View.GONE);
        } else {
            delete_Account_message_layout.setVisibility(View.GONE);
            delete_account.setVisibility(View.VISIBLE);
        }
    }

    private void DeleteAccountConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogButtonsView = LayoutInflater.from(this).inflate(R.layout.confirmationdelete, null);
        builder.setView(dialogButtonsView);

        // Find the buttons in the custom layout
        TextView title = dialogButtonsView.findViewById(R.id.delete_confirmation_title);
        TextView msg = dialogButtonsView.findViewById(R.id.delete_confirmation_msg);
        Button btnNo = dialogButtonsView.findViewById(R.id.btnNo);
        Button btnYes = dialogButtonsView.findViewById(R.id.btnYes);
        AlertDialog dialog = builder.create();

        title.setText(R.string.delete_account_confirmation);
        msg.setText(R.string.are_you_sure_delete_account);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccount();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void DeleteAccount() {
        Call<LoginResponse> call = Retrofirinstance.getClient(supplierActivitySession.getToken()).create(UserInterface.class).DeleteAccount();
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        String message = loginResponse.getMessage();
                        showToast(getApplicationContext(), message);
                        Log.e(TAG, "onResponse: timming : " + loginResponse.getHours());
                        supplierActivitySession.setHour(loginResponse.getHours());
                        delete_Account_message_layout.setVisibility(View.VISIBLE);
                        delete_account.setVisibility(View.GONE);
                    }
                } else if (response.code() == 422) {
                    try {
                        String errorBodyString = response.errorBody().string();
                        Gson gson = new Gson();
                        JsonObject errorBodyJson = gson.fromJson(errorBodyString, JsonObject.class);

                        String message = errorBodyJson.has("message") ? errorBodyJson.get("message").getAsString() : "No message";
                        delete_Account_message_layout.setVisibility(View.GONE);
                        delete_account.setVisibility(View.VISIBLE);
                        showCustomeDialog(message);
                    } catch (Exception e) {
                        e.printStackTrace();
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
    }

    private void showCustomeDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogButtonsView = LayoutInflater.from(this).inflate(R.layout.confirmationdelete, null);
        builder.setView(dialogButtonsView);

        // Find the buttons in the custom layout
        TextView title = dialogButtonsView.findViewById(R.id.delete_confirmation_title);
        TextView msg = dialogButtonsView.findViewById(R.id.delete_confirmation_msg);
        Button btnNo = dialogButtonsView.findViewById(R.id.btnNo);
        Button btnYes = dialogButtonsView.findViewById(R.id.btnYes);
        Button btnOkay = dialogButtonsView.findViewById(R.id.btnOkay);
        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        title.setText(R.string.account_deletion_title);
        msg.setText(message);
        btnNo.setVisibility(View.GONE);
        btnYes.setVisibility(View.GONE);
        btnOkay.setVisibility(View.VISIBLE);

        btnOkay.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
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