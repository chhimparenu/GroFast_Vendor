package com.wits.grofast_vendor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
}