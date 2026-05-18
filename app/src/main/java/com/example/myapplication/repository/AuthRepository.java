package com.example.myapplication.repository;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.db.AppDatabase;
import com.example.myapplication.model.LoginRequest;
import com.example.myapplication.model.LoginResponse;
import com.example.myapplication.model.dao.UserDao;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final UserDao dao;
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Context mContext;

    public AuthRepository(Context content) {
        this.dao = AppDatabase.getInstance(content).userDao();
        mContext = content;
    }

    public interface SyncCallback<T> {
        void onSuccess(T data);
        void onFailure(String message);
    }
    public void checkLogin(String username, String pw, SyncCallback<LoginResponse> callback) {
        RetrofitClient.getInstance(mContext).getService().login(new LoginRequest(username, pw))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.isSuccess()) {
                                SharedPreferences preferences = mContext.getSharedPreferences("AppPref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                // Lưu thông tin an toàn
                                if (loginResponse.getUser() != null) {
                                    editor.putString("login_user", loginResponse.getUser().getUsername());
                                }
                                editor.putString("access_token", loginResponse.getAccess_token());
                                editor.putString("refresh_token", loginResponse.getRefresh_token());
                                editor.apply();

                                callback.onSuccess(loginResponse);
                            } else {
                                callback.onFailure(loginResponse.getStatus() != null ? loginResponse.getStatus() : "Login failed");
                            }
                        } else {
                            String errorMessage = "Error: " + response.code();
                            try {
                                if (response.errorBody() != null) {
                                    errorMessage = response.errorBody().string();
                                }
                            } catch (Exception e) {
                                Log.e("AuthRepo", "Error parsing error body", e);
                            }
                            callback.onFailure(errorMessage);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        Log.e("AuthRepo", "Login Error: " + t.getMessage()); // Thêm dòng này để xem lỗi trong Logcat
                        callback.onFailure(t.getMessage());
                    }
                });

    }
}
