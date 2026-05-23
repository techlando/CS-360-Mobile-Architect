package com.example.landon_phillips_first_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private Database db;
    private String username;
    private RecyclerView rvWeights;
    private WeightAdapter adapter;
    private List<Weight> weightList;
    private int currentUserId;
    private SwitchMaterial switchSms;
    private FloatingActionButton fabAdd;
    private boolean smsPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        username = getIntent().getStringExtra("username");
        db = new Database(this);
        rvWeights = findViewById(R.id.rvWeights);

        fabAdd = findViewById(R.id.fabAdd);

        findViewById(R.id.btnNotifications).setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationsActivity.class));
        });
        checkSmsPermission();
        currentUserId = db.getUserId(username);
        double goalWeight = db.getGoalWeight(currentUserId);
        weightList = new ArrayList<>();
        adapter = new WeightAdapter(weightList, new WeightAdapter.OnItemActionListener() {
            @Override
            public void onDelete(int weightId) {
                db.deleteWeight(weightId);
                loadWeights();
                Toast.makeText(DashboardActivity.this, "Entry deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdate(Weight weight) {
                showWeightDialog("Update Weight", weight);
            }
        });

        rvWeights.setLayoutManager(new LinearLayoutManager(this));
        rvWeights.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showWeightDialog("Add Weight", null));

        loadWeights();
    }



    private void loadWeights() {
        weightList.clear();
        Cursor cursor = db.getWeightsForUser(currentUserId);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                double weight = cursor.getDouble(cursor.getColumnIndexOrThrow("weight"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                weightList.add(new Weight(id, weight, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.updateList(weightList);
    }

    private void showWeightDialog(String title, Weight existing) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_weight, null);
        EditText etWeight = view.findViewById(R.id.etWeight);
        EditText etDate = view.findViewById(R.id.etDate);

        if (existing != null) {
            etWeight.setText(String.valueOf(existing.weight));
            etDate.setText(existing.date);
        }

        builder.setView(view);
        builder.setPositiveButton("Save", (dialog, which) -> {
            double weight = Double.parseDouble(etWeight.getText().toString().trim());
            String date = etDate.getText().toString().trim();

            if (existing == null) {
                db.addWeight(currentUserId, weight, date);
                Toast.makeText(this, "Entry added", Toast.LENGTH_SHORT).show();

                double goal = db.getGoalWeight(currentUserId);
                boolean alertsEnabled = getSharedPreferences("prefs", MODE_PRIVATE)
                        .getBoolean("sms_enabled", false);

                if (smsPermissionGranted && alertsEnabled && weight <= goal) {
                    sendGoalReachedSms();
                }
            } else {
                db.updateWeight(existing.id, weight, date);
                Toast.makeText(this, "Entry updated", Toast.LENGTH_SHORT).show();
            }

            loadWeights();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            smsPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.SEND_SMS},
                    101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            smsPermissionGranted = true;
        }
    }

    private void sendGoalReachedSms() {
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage("4695866914", null, "Congrats! You've hit your goal weight!", null, null);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
