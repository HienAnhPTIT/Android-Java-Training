package com.example.myapplication.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.db.AppDatabase;
import com.example.myapplication.db.entity.EmployeeEntity;
import com.example.myapplication.db.entity.EmployeeMapper;
import com.example.myapplication.model.Employee;
import com.example.myapplication.model.dao.EmployeeDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRepository {
    private final EmployeeDao dao;
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Context mContext;

    public EmployeeRepository(Context context) {
        this.dao = AppDatabase.getInstance(context).employeeDao();
        mContext = context;
    }

    public LiveData<List<EmployeeEntity>> getAllLive() {
        return dao.getAllLive();
    }

    public void addEmployee(Employee employee, SyncCallback callback) {
        RetrofitClient.getInstance(mContext).getService().addEmployee(employee)
                .enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            mExecutor.execute(() -> {
                                try {
                                    dao.insertOrUpdate(EmployeeMapper.toEntity(response.body()));
                                    mHandler.post(callback::onSuccess);
                                } catch (Exception e) {
                                    mHandler.post(() -> callback.onFailure("DB Error: " + e.getMessage()));
                                }
                            });
                        } else {
                            callback.onFailure("API Error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                        callback.onFailure("Network Error: " + t.getMessage());
                    }
                });
    }

    public void updateEmployee(Employee employee, SyncCallback callback) {
        RetrofitClient.getInstance(mContext).getService().updateEmployee(employee.getId(), employee)
                .enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                        if (response.isSuccessful()) {
                            mExecutor.execute(() -> {
                                try {
                                    dao.insertOrUpdate(EmployeeMapper.toEntity(employee));
                                    mHandler.post(callback::onSuccess);
                                } catch (Exception e) {
                                    mHandler.post(() -> callback.onFailure("DB Error: " + e.getMessage()));
                                }
                            });
                        } else {
                            callback.onFailure("API Error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                        callback.onFailure("Network Error: " + t.getMessage());
                    }
                });
    }

    public void deleteEmployee(int id, SyncCallback callback) {
        RetrofitClient.getInstance(mContext).getService().deleteEmployee(id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful() || response.code() == 204) {
                            mExecutor.execute(() -> {
                                try {
                                    dao.deleteById(id);
                                    mHandler.post(callback::onSuccess);
                                } catch (Exception e) {
                                    mHandler.post(() -> callback.onFailure("DB Error: " + e.getMessage()));
                                }
                            });
                        } else {
                            callback.onFailure("API Error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        callback.onFailure("Network Error: " + t.getMessage());
                    }
                });
    }

    public interface SyncCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public void getAllEmp(SyncCallback callback) {
        Log.d("EmployeeRepo", "Fetching employees from API at /users...");
        RetrofitClient.getInstance(mContext).getService().getEmployees()
                .enqueue(new Callback<List<Employee>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Employee>> call, @NonNull Response<List<Employee>> response) {
                        if (response.isSuccessful()) {
                            List<Employee> employees = response.body();
                            if (employees != null) {
                                Log.d("EmployeeRepo", "Success! Received " + employees.size() + " employees");
                                mExecutor.execute(() -> {
                                    try {
                                        dao.deleteAll();
                                        dao.insertOrReplaceAll(EmployeeMapper.toEntityList(employees));
                                        mHandler.post(callback::onSuccess);
                                    } catch (Exception e) {
                                        Log.e("EmployeeRepo", "Database error: " + e.getMessage());
                                        mHandler.post(() -> callback.onFailure("Database error: " + e.getMessage()));
                                    }
                                });
                            } else {
                                Log.w("EmployeeRepo", "Response body is null");
                                callback.onFailure("Empty response from server");
                            }
                        } else {
                            String errorMsg = "Server error: " + response.code();
                            if (response.code() == 401) {
                                errorMsg = "Unauthorized - Check your token";
                            } else if (response.code() == 404) {
                                errorMsg = "Endpoint /users not found in Mockoon";
                            }
                            Log.e("EmployeeRepo", errorMsg);
                            callback.onFailure(errorMsg);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Employee>> call, @NonNull Throwable t) {
                        Log.e("EmployeeRepo", "Network error: " + t.getMessage());
                        callback.onFailure("Error: " + t.getMessage());
                    }
                });
    }
}
