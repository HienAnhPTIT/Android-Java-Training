package com.example.myapplication.api;

import com.example.myapplication.model.Employee;
import com.example.myapplication.model.LoginRequest;
import com.example.myapplication.model.LoginResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ApiService_bk {
    public static void getEmpList(String url, String method,
                                  ApiClient.Callback<List<Employee>> callback) {
        Type type = new TypeToken<List<Employee>>() {
        }.getType();
        ApiClient.request(url, method, type, null, callback);
    }

    public static void login(String username, String pw, ApiClient.Callback<LoginResponse> callback){
        LoginRequest request = new LoginRequest(username, pw);
        String json = new Gson().toJson(request);
        Type type = new TypeToken<LoginResponse>(){}.getType();
        ApiClient.request("auth/login", "POST", type, json, callback);
    }
}
