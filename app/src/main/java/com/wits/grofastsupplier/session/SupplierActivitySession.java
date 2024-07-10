package com.wits.grofastsupplier.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SupplierActivitySession {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SupplierActivitySession(Context context) {
        sharedPreferences = context.getSharedPreferences("UserActivity", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoginStaus(boolean status) {
        editor.putBoolean("loginStatus", status);
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("loginStatus", false);
    }

    public void setToken(String token) {
        editor.putString("userAccessToken", token);
        editor.apply();
    }

    public void setHour(int hour) {
        editor.putInt("supplierDeleteAccount", hour);
        editor.apply();
    }

    public int getHour() {
        return sharedPreferences.getInt("supplierDeleteAccount", 0);
    }

    public String getToken() {
        return sharedPreferences.getString("userAccessToken", "");
    }

    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}

