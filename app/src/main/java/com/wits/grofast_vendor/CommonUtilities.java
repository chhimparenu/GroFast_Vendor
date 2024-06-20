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
import java.time.LocalDateTime;
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
        }
        LocalDateTime dateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.parse(dateTimeString, formatter);
        }

        // Format to show only the date
        DateTimeFormatter dateFormatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        }
        return dateTime.format(dateFormatter);
    }
}