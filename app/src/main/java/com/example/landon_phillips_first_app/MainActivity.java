package com.example.landon_phillips_first_app;

import android.content.Intent;
import android.os.Bundle;
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
    Button btnLogin, btnCreateAccount;
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

        db = new Database(this);

        btnLogin.setOnClickListener(v -> {
            String username = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (db.loginUser(username, password)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid login. Try again.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCreateAccount.setOnClickListener(v -> {
            String username = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            EditText inputGoal = new EditText(this);
            inputGoal.setHint("Enter goal weight (e.g., 170.0)");
            inputGoal.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Set Goal Weight")
                    .setView(inputGoal)
                    .setPositiveButton("Create", (dialog, which) -> {
                        String goalStr = inputGoal.getText().toString().trim();
                        if (goalStr.isEmpty()) {
                            Toast.makeText(this, "Goal weight is required.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        double goalWeight = Double.parseDouble(goalStr);
                        boolean created = db.registerUser(username, password, goalWeight);

                        if (created) {
                            Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Username already exists!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}
