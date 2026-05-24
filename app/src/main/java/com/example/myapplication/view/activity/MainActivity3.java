package com.example.myapplication.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.viewmodel.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity3 extends AppCompatActivity {

    private Button mBtnLogin, mBtnGuest;
    private TextInputLayout mLayoutUserName, mLayoutPw;
    private TextInputEditText mEdtUserName, mEdtPw;
    private ProgressBar mProgressBar;
    private LoginViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        initViews();
        setupViewModel();
        loadSavedUser();
    }

    private void initViews() {
        mBtnLogin = findViewById(R.id.btnLogin);
        mBtnGuest = findViewById(R.id.btnGuest);
        mLayoutUserName = findViewById(R.id.layout_user);
        mLayoutPw = findViewById(R.id.layout_password);
        mEdtUserName = findViewById(R.id.etUsername);
        mEdtPw = findViewById(R.id.etPassword);
        mProgressBar = findViewById(R.id.progressBar);

        mBtnLogin.setOnClickListener(v -> handleLogin());
        mBtnGuest.setOnClickListener(v -> mViewModel.guestLogin());
    }

    private void setupViewModel() {
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        
        mViewModel.getLoading().observe(this, isLoading -> {
            mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            mBtnLogin.setEnabled(!isLoading);
            mBtnGuest.setEnabled(!isLoading);
        });

        mViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.getIsLoginSuccess().observe(this, isSuccess -> {
            if (isSuccess != null && isSuccess) {
                navigateToEmployeeList();
            }
        });
    }

    private void loadSavedUser() {
        String loginUser = getSharedPreferences("AppPref", MODE_PRIVATE)
                .getString("login_user", null);
        if (loginUser != null) {
            mEdtUserName.setText(loginUser);
            mEdtPw.requestFocus();
        }
    }

    private void handleLogin() {
        String user = mEdtUserName.getText() != null ? mEdtUserName.getText().toString().trim() : "";
        String pw = mEdtPw.getText() != null ? mEdtPw.getText().toString().trim() : "";

        if (validate(user, pw)) {
            mViewModel.checkLogin(user, pw);
        }
    }

    private void navigateToEmployeeList() {
        Intent intent = new Intent(MainActivity3.this, EmployeeActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validate(String userName, String password) {
        mLayoutUserName.setError(null);
        mLayoutPw.setError(null);

        boolean valid = true;

        if (userName.isEmpty()) {
            mLayoutUserName.setError("Username is required");
            valid = false;
        }

        if (password.isEmpty()) {
            mLayoutPw.setError("Password is required");
            valid = false;
        }

        return valid;
    }
}
