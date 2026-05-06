package com.example.campuscompass;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AdministrationActivity extends AppCompatActivity {

    private boolean isStrongPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[@#$%^&+=!].*");
    }

    EditText name, email, password, course;
    Spinner roleSpinner;
    Button createBtn, deleteBtn, updateBtn;


    String BASE_URL = "http://192.168.0.207/campusCompass/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_administration);

        name = findViewById(R.id.adminName);
        email = findViewById(R.id.adminEmail);
        password = findViewById(R.id.adminPassword);
        roleSpinner = findViewById(R.id.roleSpinner);
        course = findViewById(R.id.adminCourse);

        createBtn = findViewById(R.id.createUserBtn);
        deleteBtn = findViewById(R.id.deleteUserBtn);
        updateBtn = findViewById(R.id.updatePasswordBtn);


        String[] roles = {"student", "teacher", "admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, roles);
        roleSpinner.setAdapter(adapter);

        createBtn.setOnClickListener(v -> createUser());
        deleteBtn.setOnClickListener(v -> deleteUser());
        updateBtn.setOnClickListener(v -> updatePassword());
    }

    private void createUser() {

        String pwd = password.getText().toString().trim();

        if (!isStrongPassword(pwd)) {
            Toast.makeText(this,
                    "Password must be 8+ chars, include upper, lower, number, special char",
                    Toast.LENGTH_LONG).show();
            return;
        }
        StringRequest request = new StringRequest(
                Request.Method.POST,
                BASE_URL + "admin_create_user.php",
                response -> Toast.makeText(this, response, Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(this, "Error: " + error.toString(), Toast.LENGTH_LONG).show()
        ) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("name", name.getText().toString());
                map.put("email", email.getText().toString());
                map.put("password", password.getText().toString());
                map.put("course", course.getText().toString());
                map.put("role", roleSpinner.getSelectedItem().toString());
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void deleteUser() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                BASE_URL + "admin_delete_user.php",
                response -> Toast.makeText(this, response, Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("email", email.getText().toString());
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void updatePassword() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                BASE_URL + "admin_update_password.php",
                response -> Toast.makeText(this, response, Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("email", email.getText().toString());
                map.put("password", password.getText().toString());
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}