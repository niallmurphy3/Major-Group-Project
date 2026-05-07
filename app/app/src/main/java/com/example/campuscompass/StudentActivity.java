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

        String username = getSharedPreferences("user", MODE_PRIVATE)
                .getString("name", "User");

        TextView usernameText = findViewById(R.id.usernameText);
        usernameText.setText(username);

        findViewById(R.id.logoutBtn).setOnClickListener(v -> {

            getSharedPreferences("user", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        Button feedBtn = findViewById(R.id.feedBtn);
        Button messagesBtn = findViewById(R.id.messagesBtn);
        Button mapBtn = findViewById(R.id.mapBtn);
        Button notificationsBtn = findViewById(R.id.notificationsBtn);
        Button settingsBtn = findViewById(R.id.settingsBtn);

        feedBtn.setOnClickListener(v ->
                startActivity(new Intent(this, StudentFeedActivity.class)));

        messagesBtn.setOnClickListener(v ->
                startActivity(new Intent(this, MessageActivity.class)));

        mapBtn.setOnClickListener(v ->
                startActivity(new Intent(this, MapsActivity.class)));

        notificationsBtn.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationsActivity.class)));

        settingsBtn.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
    }
}