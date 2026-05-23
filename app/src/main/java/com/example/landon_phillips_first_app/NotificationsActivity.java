package com.example.landon_phillips_first_app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class NotificationsActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 101;
    private SwitchMaterial switchSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        switchSms = findViewById(R.id.switchSms);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        boolean permissionGranted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED;
        switchSms.setChecked(permissionGranted);

        switchSms.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getSharedPreferences("prefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("sms_enabled", isChecked)
                    .apply();

            if (isChecked) {
                requestSmsPermission();
            } else {
                Toast.makeText(this, "Text alerts disabled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        } else {
            Toast.makeText(this, "SMS permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                switchSms.setChecked(true); // Reflect new state
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                switchSms.setChecked(false); // Ensure it's off
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
