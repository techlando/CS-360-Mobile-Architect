package com.example.landon_phillips_first_app;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etForgotEmail, etNewPassword, etConfirmNewPassword;
    Button btnResetPassword;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgotPasswordMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etForgotEmail = findViewById(R.id.etForgotEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        db = new Database(this);

        btnResetPassword.setOnClickListener(v -> {
            String email = etForgotEmail.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmNewPassword = etConfirmNewPassword.getText().toString().trim();

            if (!validateResetInput(email, newPassword, confirmNewPassword)) {
                return;
            }

            if (!db.userExists(email)) {
                etForgotEmail.setError("No account found with this email.");
                etForgotEmail.requestFocus();
                Toast.makeText(this, "No account found with this email.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = db.updatePassword(email, newPassword);

            if (updated) {
                Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Password update failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateResetInput(String email, String newPassword, String confirmNewPassword) {
        etForgotEmail.setError(null);
        etNewPassword.setError(null);
        etConfirmNewPassword.setError(null);

        if (email.isEmpty()) {
            etForgotEmail.setError("Email is required.");
            etForgotEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etForgotEmail.setError("Enter a valid email address.");
            etForgotEmail.requestFocus();
            return false;
        }

        if (newPassword.isEmpty()) {
            etNewPassword.setError("New password is required.");
            etNewPassword.requestFocus();
            return false;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Password must be at least 6 characters.");
            etNewPassword.requestFocus();
            return false;
        }

        if (confirmNewPassword.isEmpty()) {
            etConfirmNewPassword.setError("Confirm password is required.");
            etConfirmNewPassword.requestFocus();
            return false;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            etConfirmNewPassword.setError("Passwords do not match.");
            etConfirmNewPassword.requestFocus();
            return false;
        }

        return true;
    }
}