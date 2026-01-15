package com.example.campuscompass;

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

        TextView messagesText = findViewById(R.id.messagesText);
        EditText recipientEmail = findViewById(R.id.recipientEmail);
        EditText messageContent = findViewById(R.id.messageContent);
        Button sendBtn = findViewById(R.id.sendBtn);

        String userEmail = getIntent().getStringExtra("email");

        StringRequest loadRequest = new StringRequest(
                Request.Method.POST,
                "http://192.168.0.207/campusCompass/get_messages.php",
                response -> {
                    try {
                        JSONArray arr = new JSONArray(response);
                        String text = "";

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            text += "From: " + obj.getString("studentEmail") + "\n";
                            text += obj.getString("content") + "\n\n";
                        }

                        messagesText.setText(text);

                    } catch (Exception e) {
                        Toast.makeText(this, "Error loading messages", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("email", userEmail);
                return map;
            }
        };

        Volley.newRequestQueue(this).add(loadRequest);

        
        sendBtn.setOnClickListener(v -> {

            StringRequest sendRequest = new StringRequest(
                    Request.Method.POST,
                    "http://192.168.0.207/campusCompass/send_message.php",
                    response -> Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show(),
                    error -> Toast.makeText(this, "Send failed", Toast.LENGTH_SHORT).show()
            ) {
                @Override
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
