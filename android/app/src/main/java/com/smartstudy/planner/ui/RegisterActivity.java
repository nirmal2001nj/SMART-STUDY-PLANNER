package com.smartstudy.planner.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smartstudy.planner.R;
import com.smartstudy.planner.db.DatabaseHelper;

public class RegisterActivity extends Activity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etRecovery;
    private Button btnRegister;
    private Button btnToLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = DatabaseHelper.getInstance(this);

        etUsername = findViewById(R.id.et_reg_username);
        etPassword = findViewById(R.id.et_reg_password);
        etConfirmPassword = findViewById(R.id.et_reg_confirm_password);
        etRecovery = findViewById(R.id.et_reg_recovery);
        btnRegister = findViewById(R.id.btn_register);
        btnToLogin = findViewById(R.id.btn_to_login);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void attemptRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();
        String recovery = etRecovery.getText().toString().trim();

        if (username.isEmpty()) {
            etUsername.setError("Username required");
            etUsername.requestFocus();
            return;
        }
        if (password.length() < 3) {
            etPassword.setError("Password must be at least 3 characters");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirm)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }
        if (recovery.isEmpty()) {
            etRecovery.setError("Recovery answer required");
            etRecovery.requestFocus();
            return;
        }

        if (dbHelper.userExists(username)) {
            Toast.makeText(this, "Username already exists. Please pick another.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.registerUser(username, password, recovery);
        if (success) {
            Toast.makeText(this, "Account created successfully! Please sign in.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
