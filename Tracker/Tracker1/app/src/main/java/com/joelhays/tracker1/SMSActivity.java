package com.joelhays.tracker1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SMSActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private TextView textPermissionExplanation;
    private Button buttonRequestPermission, buttonBackToInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        textPermissionExplanation = findViewById(R.id.textPermissionExplanation);
        buttonRequestPermission = findViewById(R.id.buttonRequestPermission);
        buttonBackToInventory = findViewById(R.id.buttonBackToInventory);

        buttonRequestPermission.setOnClickListener(new View.OnClickListener() {
            @Override // Request permission
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SMSActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SMSActivity.this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
                } else {
                    sendSMSNotification();
                }
            }
        });

        buttonBackToInventory.setOnClickListener(new View.OnClickListener() {
            @Override // Navigate back to MainActivity
            public void onClick(View v) {
                // Navigate back to MainActivity (Inventory screen)
                Intent intent = new Intent(SMSActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override // Handle permission request results
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMSNotification();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendSMSNotification() {
        // Placeholder for sending SMS notification. Sends test notification when permission is granted.
        String phoneNumber = "1234567890";
        String message = "This is a test SMS notification";

        try {
            // Send SMS using SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Handle exception
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
