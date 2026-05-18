package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.model.LoginResponse;
import com.example.myapplication.repository.AuthRepository;

public class LoginViewModel extends AndroidViewModel {
    private final AuthRepository repository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoginSuccess = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application){
        super(application);
        repository = new AuthRepository(application);
    }

    public LiveData<Boolean> getLoading(){
        return isLoading;
    }

    public LiveData<String> getErrorMessage(){
        return errorMessage;
    }
    public LiveData<Boolean> getIsLoginSuccess(){
        return isLoginSuccess;
    }
    public void checkLogin(String user, String pw){
        isLoading.setValue(true);
        repository.checkLogin(user, pw, new AuthRepository.SyncCallback<LoginResponse>(){
            @Override
            public void onSuccess(LoginResponse response) {
                isLoading.postValue(false);
                isLoginSuccess.postValue(true);
            }
            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                isLoginSuccess.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }
}