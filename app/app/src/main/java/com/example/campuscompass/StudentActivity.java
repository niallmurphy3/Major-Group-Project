package com.example.campuscompass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //receives session username data
        String username = getSharedPreferences("user", MODE_PRIVATE)
                .getString("name", "User");

        TextView usernameText = findViewById(R.id.usernameText);
        usernameText.setText(username);

        //logs out
        findViewById(R.id.logoutButton).setOnClickListener(v -> {

            getSharedPreferences("user", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(this, LoginActivity.class);
            //stops return to page
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        //buttons
        Button feedBtn = findViewById(R.id.feedBtn);
        Button messagesBtn = findViewById(R.id.messagesBtn);
        Button mapBtn = findViewById(R.id.mapBtn);
        Button notificationsBtn = findViewById(R.id.notificationsBtn);
        Button carpoolBtn = findViewById(R.id.carpoolBtn);
        Button settingsBtn = findViewById(R.id.settingsBtn);

        //listeners for button press
        feedBtn.setOnClickListener(v ->
                startActivity(new Intent(this, StudentFeedActivity.class)));

        messagesBtn.setOnClickListener(v ->
                startActivity(new Intent(this, MessageActivity.class)));

        mapBtn.setOnClickListener(v ->
                startActivity(new Intent(this, MapsActivity.class)));

        notificationsBtn.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationsActivity.class)));

        carpoolBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CarpoolActivity.class);
            startActivity(intent);
        });

        settingsBtn.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
    }
}