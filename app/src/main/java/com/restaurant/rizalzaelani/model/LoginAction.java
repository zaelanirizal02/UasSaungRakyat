package com.restaurant.rizalzaelani.model;

import com.google.gson.annotations.Expose;

public class LoginAction {
    @Expose
    private LoginBase data;
    @Expose
    private String message;
    @Expose
    private String status;
    public LoginBase getLogin() {
        return data;
    }
    public void setLogin(LoginBase data) {
        this.data = data;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}
