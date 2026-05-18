package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.db.entity.EmployeeEntity;
import com.example.myapplication.model.Employee;
import com.example.myapplication.repository.EmployeeRepository;

import java.util.List;

public class EmployeeViewModel extends AndroidViewModel {

    private final EmployeeRepository repository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final LiveData<List<EmployeeEntity>> employees;

    public EmployeeViewModel(@NonNull Application application){
        super(application);
        repository = new EmployeeRepository(application);
        employees = repository.getAllLive();
    }
    public LiveData<Boolean> getIsLoading(){
        return isLoading;
    }
    public LiveData<String> getError(){
        return errorMessage;
    }
    public LiveData<List<EmployeeEntity>> getEmployees() {
        return employees;
    }

    public void loadEmployee(){
        isLoading.postValue(true);
        //logic code
        repository.getAllEmp(new EmployeeRepository.SyncCallback(){
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
            }
            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }
    public void addEmployee(Employee employee){
        isLoading.postValue(true);
        repository.addEmployee(employee, new EmployeeRepository.SyncCallback() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    public void updateEmployee(Employee employee) {
        isLoading.postValue(true);
        repository.updateEmployee(employee, new EmployeeRepository.SyncCallback() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    public void deleteEmployee(int id) {
        isLoading.postValue(true);
        repository.deleteEmployee(id, new EmployeeRepository.SyncCallback() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }
}
