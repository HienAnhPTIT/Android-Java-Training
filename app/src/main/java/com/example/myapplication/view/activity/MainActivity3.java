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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity3.class.getName();
    private Button mBtnLogin;
    private TextInputLayout mLayoutUserName, mLayoutPw;
    private TextInputEditText mEdtUserName, mEdtPw;
    private ProgressBar mProgressBar;
    private LoginViewModel mViewModel;
    private Executor mExecutor = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mBtnLogin = findViewById(R.id.btnLogin);
        mLayoutUserName = findViewById(R.id.layout_user);
        mLayoutPw = findViewById(R.id.layout_password);
        mEdtUserName = findViewById(R.id.etUsername);
        mEdtPw = findViewById(R.id.etPassword);
        mProgressBar = findViewById(R.id.progressBar);

        mBtnLogin.setOnClickListener(this);

        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mViewModel.getLoading().observe(this, isLoading -> {
            mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
        mViewModel.getErrorMessage().observe(this, errorMessage -> {
            Toast.makeText(this, "error: " + errorMessage, Toast.LENGTH_SHORT).show();
        });
        mViewModel.getIsLoginSuccess().observe(this, isSuccess -> {
            if (isSuccess != null && isSuccess) {
                mProgressBar.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity3.this, EmployeeActivity.class);
                startActivity(intent);
            }
        });
        String login_user = getSharedPreferences("AppPref", MODE_PRIVATE)
                .getString("login_user", null);
        if (login_user != null) {
                mEdtUserName.setText(login_user);
                mEdtPw.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin){

            String login_user = mEdtUserName.getText() != null ? mEdtUserName.getText().toString() : "";
            String login_pw = mEdtPw.getText() != null ? mEdtPw.getText().toString() : "";

            onSign(login_user, login_pw);
        }
    }
    private void onSign(String login_user, String login_pw){
        if (validate(login_user, login_pw)) {
//                SharedPreferences preferences = getSharedPreferences("AppPref", MODE_PRIVATE);
//                preferences.edit().putString("login_user", login_user).apply();
//                Intent intent = new Intent(MainActivity3.this, EmployeeActivity.class);
//                intent.putExtra("username", login_user);
//                startActivity(intent);
////                finish();
//                Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
            mViewModel.checkLogin(login_user, login_pw);
        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean validate(String userName, String login_pw) {

        mLayoutUserName.setError(null);
        mLayoutPw.setError(null);

        boolean valid = true;

        if (userName.isEmpty()) {
            mLayoutUserName.setError("Username is required");
            valid = false;
        }

        if (login_pw.isEmpty()) {
            mLayoutPw.setError("Password is required");
            valid = false;
        }

        return valid;
    }
}