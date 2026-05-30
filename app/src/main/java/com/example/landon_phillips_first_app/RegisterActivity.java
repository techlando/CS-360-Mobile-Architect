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

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName, etPhone, etEmail, etPassword, etConfirmPassword, etGoalWeight;
    Button btnRegister;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etGoalWeight = findViewById(R.id.etGoalWeight);
        btnRegister = findViewById(R.id.btnRegister);

        db = new Database(this);

        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String goalWeightText = etGoalWeight.getText().toString().trim();

            if (!validateRegisterInput(fullName, phone, email, password, confirmPassword, goalWeightText)) {
                return;
            }

            double goalWeight = Double.parseDouble(goalWeightText);

            boolean created = db.registerUser(fullName, phone, email, password, goalWeight);

            if (created) {
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                etEmail.setError("This email is already registered.");
                etEmail.requestFocus();
                Toast.makeText(this, "Email already exists. Please log in.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateRegisterInput(String fullName, String phone, String email, String password, String confirmPassword, String goalWeightText) {
        etFullName.setError(null);
        etPhone.setError(null);
        etEmail.setError(null);
        etPassword.setError(null);
        etConfirmPassword.setError(null);
        etGoalWeight.setError(null);

        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required.");
            etFullName.requestFocus();
            return false;
        }

        if (fullName.length() < 2) {
            etFullName.setError("Full name must be at least 2 characters.");
            etFullName.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Phone number is required.");
            etPhone.requestFocus();
            return false;
        }

        String phoneDigitsOnly = phone.replaceAll("[^0-9]", "");

        if (phoneDigitsOnly.length() != 10) {
            etPhone.setError("Enter a valid 10-digit phone number.");
            etPhone.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required.");
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address.");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required.");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters.");
            etPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Confirm password is required.");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match.");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (goalWeightText.isEmpty()) {
            etGoalWeight.setError("Goal weight is required.");
            etGoalWeight.requestFocus();
            return false;
        }

        try {
            double goalWeight = Double.parseDouble(goalWeightText);

            if (goalWeight <= 0) {
                etGoalWeight.setError("Goal weight must be greater than zero.");
                etGoalWeight.requestFocus();
                return false;
            }

            if (goalWeight < 50 || goalWeight > 800) {
                etGoalWeight.setError("Enter a realistic goal weight.");
                etGoalWeight.requestFocus();
                return false;
            }

        } catch (NumberFormatException e) {
            etGoalWeight.setError("Enter a valid goal weight.");
            etGoalWeight.requestFocus();
            return false;
        }

        return true;
    }
}