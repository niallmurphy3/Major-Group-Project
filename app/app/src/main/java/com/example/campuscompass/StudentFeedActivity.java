package com.example.campuscompass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class StudentFeedActivity extends AppCompatActivity {

    TextView feedText;
    EditText postInput;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_feed);

        feedText = findViewById(R.id.feedText);
        postInput = findViewById(R.id.postContent);

        userEmail = getIntent().getStringExtra("email");

        Button goMessagesBtn = findViewById(R.id.goMessagesBtn);
        Button postBtn = findViewById(R.id.postBtn);

        loadFeed();

        goMessagesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MessageActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
        });

        postBtn.setOnClickListener(v -> postContent());
    }

    private void postContent() {

        String content = postInput.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, "Write something first", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                "http://192.168.0.207/campusCompass/create_post.php",
                response -> {
                    Log.d("POST_RESPONSE", response);
                    Toast.makeText(this, "Posted", Toast.LENGTH_SHORT).show();
                    postInput.setText("");
                    loadFeed();
                },
                error -> {
                    Log.e("POST_ERROR", error.toString());
                    Toast.makeText(this, "Post failed", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("email", userEmail);
                map.put("content", content);
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void loadFeed() {

        String url = "http://192.168.0.207/campusCompass/get_feed.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    Log.d("FEED_RAW", response);
                    feedText.setText(response);
                },
                error -> {
                    Log.e("FEED_ERROR", error.toString());
                    Toast.makeText(this, "Error loading feed", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("email", userEmail);
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}