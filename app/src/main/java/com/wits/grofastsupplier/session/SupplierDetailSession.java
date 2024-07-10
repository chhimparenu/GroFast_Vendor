package com.wits.grofastsupplier.session;

import static com.wits.grofastsupplier.Api.Retrofirinstance.domain;

import android.content.Context;
import android.content.SharedPreferences;

public class SupplierDetailSession {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SupplierDetailSession(Context context) {
        sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void setPhoneNo(String phone) {
        editor.putString("userPhone", phone);
        editor.apply();
    }

    public String getPhoneNo() {
        return sharedPreferences.getString("userPhone", "1234567890");
    }

    public void setName(String name) {
        editor.putString("userName", name);
        editor.apply();
    }

    public String getName() {
        return sharedPreferences.getString("userName", "");
    }

    public void setImage(String image) {
        editor.putString("userImage", domain + image);
        editor.apply();
    }

    public String getImage() {
        return sharedPreferences.getString("userImage", null);
    }

    public void setEmail(String email) {
        editor.putString("userEmail", email);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString("userEmail", "");
    }

    public void setGender(String gender) {
        editor.putString("userGender", gender);
        editor.apply();
    }

    public String getGender() {
        return sharedPreferences.getString("userGender", "");
    }
    public void setUuid(String uuid) {
        editor.putString("userUUID", uuid);
        editor.apply();
    }

    public String getUuid() {
        return sharedPreferences.getString("userUUID", "");
    }

    public void setStoreName(String storename) {
        editor.putString("storeName", storename);
        editor.apply();
    }

    public String getStoreName() {
        return sharedPreferences.getString("storeName", "");
    }

    public void setDescription(String description) {
        editor.putString("description", description);
        editor.apply();
    }

    public String getDescription() {
        return sharedPreferences.getString("description", "");
    }

    public void setStoreAddress(String storeAddress) {
        editor.putString("storeAddress", storeAddress);
        editor.apply();
    }

    public String getStoreAddress() {
        return sharedPreferences.getString("storeAddress", "");
    }

    public void setPincode(String pincode) {
        editor.putString("pincode", pincode);
        editor.apply();
    }

    public String getPincode() {
        return sharedPreferences.getString("pincode", "");
    }

    public void setCiy(String city) {
        editor.putString("city", city);
        editor.apply();
    }

    public String getCity() {
        return sharedPreferences.getString("city", "");
    }

    public void setState(String state) {
        editor.putString("state", state);
        editor.apply();
    }

    public String getState() {
        return sharedPreferences.getString("state", "");
    }

    public void setCountry(String country) {
        editor.putString("country", country);
        editor.apply();
    }

    public String getCountry() {
        return sharedPreferences.getString("country", "");
    }

    public void setStatus(String status) {
        editor.putString("status", status);
        editor.apply();
    }

    public String getStatus() {
        return sharedPreferences.getString("status", "");
    }

    public void setId(int id) {
        editor.putInt("supplierId", id);
        editor.apply();
    }
    public int getId() {
        return sharedPreferences.getInt("supplierId", 0);
    }


    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
