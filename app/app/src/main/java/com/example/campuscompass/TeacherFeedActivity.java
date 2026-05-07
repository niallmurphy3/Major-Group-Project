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

    //course for teacer to searc
    EditText courseInput;
    ListView feedList;

    //display s
    ArrayList<String> displayList = new ArrayList<>();
    ArrayList<String> postIds = new ArrayList<>();

    //converts to list
    ArrayAdapter<String> adapter;

    String BASE_URL = "http://192.168.0.207/campusCompass/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_feed);

        //connects to xml
        courseInput = findViewById(R.id.courseInput);
        feedList = findViewById(R.id.feedList);
        Button loadBtn = findViewById(R.id.loadBtn);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        feedList.setAdapter(adapter);

        //when pressed, load inserted feed
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
                        //clears for new list (teacer inputs new code)
                        displayList.clear();
                        postIds.clear();
                        //for every post found display, display id name and email + content
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            postIds.add(obj.getString("postID"));

                            String name = obj.optString("studentName", obj.getString("studentEmail"));
                            String content = obj.getString("content");

                            displayList.add(name + "\n" + content);
                        }
                        //refreshes new page
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) { //brokenjsonw
                        Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },//cant connect to php
                error -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            //send post data
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("course", course);
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void deletePost(String postID) {
        //creates connection
        StringRequest request = new StringRequest(
                Request.Method.POST,
                BASE_URL + "delete_post.php",
                response -> {//wgen clicked deletes post by snedin to delete_post and refreshes
                    Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show();
                    loadFeed();
                },
                error -> Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            //sends postID to delete_post.php
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("postID", postID);
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}