package com.wits.grofastsupplier.Api.Response;

public class LoginResponse {
    private String phone_no;
    private String message;
    private Integer status;
    private Integer hours;

    public String getPhone_no() {
        return phone_no;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getHours() {
        return hours;
    }
}
