package com.example.myapplication.api;

import com.example.myapplication.model.Employee;
import com.example.myapplication.model.LoginRequest;
import com.example.myapplication.model.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("users")
    Call<List<Employee>> getEmployees();

    @POST("user")
    Call<Employee> addEmployee(@Body Employee employee);

    @PUT("user/{id}")
    Call<Employee> updateEmployee(@Path("id") int id, @Body Employee employee);

    @DELETE("user/{id}")
    Call<Void> deleteEmployee(@Path("id") int id);
}
