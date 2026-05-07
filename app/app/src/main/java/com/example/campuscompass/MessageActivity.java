package com.example.campuscompass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //receives session username
        String username = getSharedPreferences("user", MODE_PRIVATE)
                .getString("name", "User");

        //log out
        findViewById(R.id.logoutButton).setOnClickListener(v -> {

            getSharedPreferences("user", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(this, LoginActivity.class);
            //prevents user returnin wit back button on android
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // shows username
        TextView usernameText = findViewById(R.id.usernameText);
        usernameText.setText(username);

        //view content
        TextView messagesText = findViewById(R.id.messagesText);
        //email recipient
        EditText recipientEmail = findViewById(R.id.recipientEmail);
        //content to send
        EditText messageContent = findViewById(R.id.messageContent);
        //button to send request
        Button sendBtn = findViewById(R.id.sendBtn);

        //receives email from session data
        String userEmail = getSharedPreferences("user", MODE_PRIVATE)
                .getString("email", "");

        StringRequest loadRequest = new StringRequest(
                Request.Method.POST,
                "http://192.168.0.207/campusCompass/get_messages.php",
                response -> {
                    try {
                        //converts json to array
                        JSONArray arr = new JSONArray(response);
                        String text = "";

                        //for every response display each message (builds content to display)
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            text += "From: " + obj.getString("studentEmail") + "\n";
                            text += obj.getString("content") + "\n\n";
                        }

                        messagesText.setText(text);
                    //json error
                    } catch (Exception e) {
                        Toast.makeText(this, "Error loading messages", Toast.LENGTH_SHORT).show();
                    }
                },
                //network error (cant connect to php
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("email", userEmail);
                return map;
            }
        };
        //sends request
        Volley.newRequestQueue(this).add(loadRequest);

        //when send button pressed
        sendBtn.setOnClickListener(v -> {

            StringRequest sendRequest = new StringRequest(
                    Request.Method.POST,
                    "http://192.168.0.207/campusCompass/send_message.php",
                    //if response, success. if not fail
                    response -> Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show(),
                    error -> Toast.makeText(this, "Send failed", Toast.LENGTH_SHORT).show()
            ) {
                @Override
                //builds messae to send to php through post
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("sender", userEmail);
                    map.put("recipient", recipientEmail.getText().toString());
                    map.put("content", messageContent.getText().toString());
                    return map;
                }
            };

            Volley.newRequestQueue(this).add(sendRequest);
        });
    }
}
