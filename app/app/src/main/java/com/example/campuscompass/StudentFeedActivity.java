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


        //receives username session data
        String username = getSharedPreferences("user", MODE_PRIVATE)
                .getString("name", "User");

        //logout
        findViewById(R.id.logoutButton).setOnClickListener(v -> {

            getSharedPreferences("user", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(this, LoginActivity.class);
            //prevents return to paeg
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });


        TextView usernameText = findViewById(R.id.usernameText);
        usernameText.setText(username);

        //stored info for displayed feed and messages to be uploaded
        feedText = findViewById(R.id.feedText);
        postInput = findViewById(R.id.postContent);

        userEmail = getSharedPreferences("user", MODE_PRIVATE)
                .getString("email", "");

        //button to go to private messages
        Button goMessagesBtn = findViewById(R.id.goMessagesBtn);
        Button postBtn = findViewById(R.id.postBtn);

        loadFeed();

        //on messages go to messages
        goMessagesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MessageActivity.class);
            //carry username and email
            intent.putExtra("email", userEmail);
            intent.putExtra("name", getIntent().getStringExtra("name"));
            startActivity(intent);
        });

        postBtn.setOnClickListener(v -> postContent());
    }

    private void postContent() {

        //gets iputted text
        String content = postInput.getText().toString().trim();

        //if no text fonud
        if (content.isEmpty()) {
            Toast.makeText(this, "Write something first", Toast.LENGTH_SHORT).show();
            return;
        }

        //requests server connection
        StringRequest request = new StringRequest(
                Request.Method.POST,
                "http://10.68.168.121/campusCompass/create_post.php",
                response -> {
                    //response
                    Log.d("POST_RESPONSE", response);
                    Toast.makeText(this, "Posted", Toast.LENGTH_SHORT).show();
                    postInput.setText("");
                    loadFeed();
                },//no response
                error -> {
                    Log.e("POST_ERROR", error.toString());
                    Toast.makeText(this, "Post failed", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            //prepares data to send
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("email", userEmail);
                map.put("content", content);
                return map;
            }
        };
        //sends requests
        Volley.newRequestQueue(this).add(request);
    }

    private void loadFeed() {

        String url = "http://10.68.168.121/campusCompass/get_feed.php";

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