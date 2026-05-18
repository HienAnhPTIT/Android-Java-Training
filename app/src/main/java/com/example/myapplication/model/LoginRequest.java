package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("username")
    String userName;
    @SerializedName("password")
    String pw;

    public LoginRequest(String userName, String pw){
        this.userName = userName;
        this.pw = pw;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
}
