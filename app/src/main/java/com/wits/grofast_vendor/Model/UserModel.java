package com.wits.grofast_vendor.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {
    private Integer id;

    private String uuid;
    private String mobile_number;
    private String image;
    private String email;
    private String gender;
    private String name;

    public Integer getId() {
        return id;
    }
    public String getPhone_no() {
        return mobile_number;
    }

    public String getImage() {
        return image;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }
}
