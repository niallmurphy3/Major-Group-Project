package com.example.campuscompass;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherFeedActivity extends AppCompatActivity {

    EditText courseInput;
    ListView feedList;

    ArrayList<String> displayList = new ArrayList<>();
    ArrayList<String> postIds = new ArrayList<>();

    ArrayAdapter<String> adapter;

    String BASE_URL = "http://192.168.0.207/campusCompass/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_feed);

        courseInput = findViewById(R.id.courseInput);
        feedList = findViewById(R.id.feedList);
        Button loadBtn = findViewById(R.id.loadBtn);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        feedList.setAdapter(adapter);

        loadBtn.setOnClickListener(v -> loadFeed());

        feedList.setOnItemClickListener((parent, view, position, id) -> {
            String postID = postIds.get(position);
            deletePost(postID);
        });
    }

    private void loadFeed() {

        String course = courseInput.getText().toString().trim();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                BASE_URL + "get_teacher_feed.php",
                response -> {
                    try {
                        JSONArray arr = new JSONArray(response);

                        displayList.clear();
                        postIds.clear();

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            postIds.add(obj.getString("postID"));

                            String name = obj.optString("studentName", obj.getString("studentEmail"));
                            String content = obj.getString("content");

                            displayList.add(name + "\n" + content);
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("course", course);
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void deletePost(String postID) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                BASE_URL + "delete_post.php",
                response -> {
                    Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show();
                    loadFeed();
                },
                error -> Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("postID", postID);
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}