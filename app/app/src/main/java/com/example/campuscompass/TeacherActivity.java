package com.example.campuscompass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

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

        Button notificationsBtn = findViewById(R.id.notificationsBtn);
        Button studentFeedBtn = findViewById(R.id.studentFeedBtn);
        Button messagesBtn = findViewById(R.id.messagesBtn);
        Button mapBtn = findViewById(R.id.mapBtn);
        Button settingsBtn = findViewById(R.id.settingsBtn);

        notificationsBtn.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationsActivity.class)));

        messagesBtn.setOnClickListener(v ->
                startActivity(new Intent(this, MessageActivity.class)));

        mapBtn.setOnClickListener(v ->
                startActivity(new Intent(this, MapsActivity.class)));

        settingsBtn.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        studentFeedBtn.setOnClickListener(v -> {

            Intent intent = new Intent(this, TeacherFeedActivity.class);
            startActivity(intent);
        });
    }
}