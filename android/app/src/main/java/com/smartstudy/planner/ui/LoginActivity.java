package com.smartstudy.planner.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartstudy.planner.R;
import com.smartstudy.planner.db.DatabaseHelper;

public class LoginActivity extends Activity {

    public static final String PREFS_NAME = "SmartStudyPrefs";
    public static final String KEY_USER = "current_username";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnToRegister;
    private TextView tvForgotPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if already logged in
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedUser = prefs.getString(KEY_USER, null);
        if (savedUser != null && !savedUser.isEmpty()) {
            launchMainActivity(savedUser);
            finish();
            return;
        }

        setContentView(R.layout.activity_login);
        dbHelper = DatabaseHelper.getInstance(this);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnToRegister = findViewById(R.id.btn_to_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            etUsername.setError("Username required");
            etUsername.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Password required");
            etPassword.requestFocus();
            return;
        }

        boolean success = dbHelper.loginUser(username, password);
        if (success) {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            prefs.edit().putString(KEY_USER, username).apply();
            Toast.makeText(this, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();
            launchMainActivity(username);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void launchMainActivity(String username) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(KEY_USER, username);
        startActivity(intent);
    }
}
