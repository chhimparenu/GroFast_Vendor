package com.wits.grofast_vendor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofast_vendor.Api.Model.SpinnerModel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

public class CommonUtilities {

    public static void handleApiError(String TAG, Response response, Context context) {
        try {

            String errorBodyString = response.errorBody().string();
            Gson gson = new Gson();
            JsonObject errorBodyJson = gson.fromJson(errorBodyString, JsonObject.class);

            String errorMessage = errorBodyJson.has("errorMessage") ? errorBodyJson.get("errorMessage").getAsString() : "No errorMessage";
            String status = errorBodyJson.has("status") ? errorBodyJson.get("status").getAsString() : "No status";
            String message = errorBodyJson.has("message") ? errorBodyJson.get("message").getAsString() : "No message";

            Log.e(TAG, "onResponse -> status       : " + status);
            Log.e(TAG, "onResponse -> message      : " + message);
            Log.e(TAG, "onResponse -> errorMessage : " + errorMessage);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "handleApiError: error " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getPathFromUri(Context context, Uri uri) {
        String[] projection = {android.provider.MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);
        cursor.close();
        return imagePath;
    }

    public static String formatDate(String dateTimeString) {
        // Parse the timestamp to LocalDateTime
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
        }
        LocalDateTime dateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.parse(dateTimeString, formatter);
        }

        // Format to show only the date
        DateTimeFormatter dateFormatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return dateTime.format(dateFormatter);
        }
        return dateTimeString;
    }

    public static int getSelectedSpinnerItemPosition(List<SpinnerModel> spinnerItemList, String selectedItem) {
        int position = 1;
        for (SpinnerModel model : spinnerItemList) {
            String id = model.getId() + "";
            if (model.getName().equals(selectedItem) || id.equals(selectedItem)) {
                break;
            }
            position++;
        }
        return position;
    }

    public static void startCountdown(AppCompatButton resend, TextView countDownTimer, Context context, long countDownTime) {
        new CountDownTimer(countDownTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                countDownTimer.setText(timeLeftFormatted);
                resend.setClickable(false);
                resend.setBackgroundDrawable(context.getDrawable(R.drawable.textview_design));
                resend.setTextColor(context.getColor(R.color.default_color));
            }

            @Override
            public void onFinish() {
                resend.setClickable(true);
                countDownTimer.setText("00:00");
                countDownTimer.setVisibility(View.GONE);
                resend.setVisibility(View.VISIBLE);
                resend.setBackgroundDrawable(context.getDrawable(R.drawable.button_round));
                resend.setTextColor(context.getColor(R.color.button_text_color));
            }
        }.start();
        resend.setVisibility(View.GONE);
        countDownTimer.setVisibility(View.VISIBLE);
    }

    public static void setEditTextListeners(EditText digit1, EditText digit2, EditText digit3, EditText digit4) {
        digit1.requestFocus();
        digit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    digit2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        digit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    digit3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        digit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    digit4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        digit2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (digit2.getText().toString().isEmpty()) {
                        digit1.requestFocus();
                    }
                }
                return false;
            }
        });

        digit3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (digit3.getText().toString().isEmpty()) {
                        digit2.requestFocus();
                    }
                }
                return false;
            }
        });

        digit4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (digit4.getText().toString().isEmpty()) {
                        digit3.requestFocus();
                    }
                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDateFromTimestamp(String isoTimestamp) {
        if (isoTimestamp == null || isoTimestamp.isEmpty()) {
            return "";
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoTimestamp);
        ZonedDateTime istDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return istDateTime.format(dateFormatter);
    }
}