package com.smartstudy.planner.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smartstudy.planner.R;
import com.smartstudy.planner.db.DatabaseHelper;

public class ForgotPasswordActivity extends Activity {

    private EditText etUsername;
    private EditText etRecovery;
    private EditText etNewPassword;
    private Button btnResetPassword;
    private Button btnBack;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        dbHelper = DatabaseHelper.getInstance(this);

        etUsername = findViewById(R.id.et_fp_username);
        etRecovery = findViewById(R.id.et_fp_recovery);
        etNewPassword = findViewById(R.id.et_fp_new_password);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        btnBack = findViewById(R.id.btn_fp_back);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptReset();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void attemptReset() {
        String username = etUsername.getText().toString().trim();
        String recovery = etRecovery.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();

        if (username.isEmpty() || recovery.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.resetPassword(username, recovery, newPassword);
        if (success) {
            Toast.makeText(this, "Password updated successfully! Please sign in.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Incorrect username or recovery answer", Toast.LENGTH_SHORT).show();
        }
    }
}
