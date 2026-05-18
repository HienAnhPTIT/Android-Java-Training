package com.example.myapplication;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity_Demo2 extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnLogin;
    private TextInputLayout mLayoutUserName, mLayoutPw;
    private TextInputEditText mEdtUserName, mEdtPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_demo2);

        mBtnLogin = findViewById(R.id.btnLogin);
        mLayoutUserName = findViewById(R.id.layout_user);
        mLayoutPw = findViewById(R.id.layout_password);
        mEdtUserName = findViewById(R.id.etUsername);
        mEdtPw = findViewById(R.id.etPassword);

        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin){

            String login_user = mEdtUserName.getText() != null ? mEdtUserName.getText().toString() : "";
            String login_pw = mEdtPw.getText() != null ? mEdtPw.getText().toString() : "";

            if (validate(login_user, login_pw)) {
                Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validate(String login_user, String login_pw) {

        mLayoutUserName.setError(null);
        mLayoutPw.setError(null);

        boolean valid = true;

        if (login_user.isEmpty()) {
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