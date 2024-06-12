package com.wits.grofast_vendor.Model;

import java.io.Serializable;

public class SupplierModel implements Serializable {
    private Integer id;
    private String uuid;
    private String image;
    private String name;
    private String store_name;
    private String email;
    private String description;
    private String store_address;
    private String pin_code;
    private String country;
    private String state;
    private String city;
    private String status;
    private String mobile_number;
    private String gender;

    public String getUuid() {
        return uuid;
    }

    public String getStore_name() {
        return store_name;
    }

    public String getDescription() {
        return description;
    }

    public String getStore_address() {
        return store_address;
    }

    public String getPin_code() {
        return pin_code;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getStatus() {
        return status;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getGender() {
        return gender;
    }

    public Integer getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
