package com.example.landon_phillips_first_app;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin, btnCreateAccount, btnForgotPassword;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        db = new Database(this);

        btnForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String username = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!validateLoginInput(username, password)) {
                return;
            }

            if (db.loginUser(username, password)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, DashboardActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid email or password. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateLoginInput(String username, String password) {
        etEmail.setError(null);
        etPassword.setError(null);

        if (username.isEmpty()) {
            etEmail.setError("Email is required.");
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            etEmail.setError("Enter a valid email address.");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required.");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }
}