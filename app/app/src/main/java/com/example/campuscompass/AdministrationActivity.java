package com.example.campuscompass;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;

public class AdministrationActivity extends AppCompatActivity {

    //checks password matches criteria listed, must be 8 or greater and include at least 1 capital, 1 number and 1 symbol
    private boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {

            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if ("!@#$%^&+=.".indexOf(c) != -1) {
                hasSpecial = true;
            }
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    EditText name, email, password, course;
    Spinner roles;
    Button createButton, deleteButton, updateButton;

    //CHANGE IP FOR NEW CONNECTION
    String BASE_URL = "http://192.168.0.207/campusCompass/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_administration);
        //logout
        findViewById(R.id.topBar)
                .findViewById(R.id.logoutButton)
                .setOnClickListener(v -> {
                    //clears current session
                    getSharedPreferences("user", MODE_PRIVATE)
                            .edit()
                            .clear()
                            .apply();
                    //redirects to loin page
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                });
        //displays name
        String username = getIntent().getStringExtra("name");

        TextView usernameText = findViewById(R.id.usernameText);
        usernameText.setText(username);



        name = findViewById(R.id.adminName);
        email = findViewById(R.id.adminEmail);
        password = findViewById(R.id.adminPassword);
        roles = findViewById(R.id.roleMenu);
        course = findViewById(R.id.adminCourse);

        createButton = findViewById(R.id.createUserButton);
        deleteButton = findViewById(R.id.deleteUserButton);
        updateButton = findViewById(R.id.updatePasswordButton);

        //roles for user creation (drop down to swap between)
        String[] roles = {"student", "teacher", "admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, roles);
        this.roles.setAdapter(adapter);

        createButton.setOnClickListener(v -> createUser());
        deleteButton.setOnClickListener(v -> deleteUser());
        updateButton.setOnClickListener(v -> updatePassword());
    }

    private void createUser() {
        //checks inputted password
        String pwd = password.getText().toString().trim();

        if (!isStrongPassword(pwd)) {
            Toast.makeText(this,
                    "Password must be 8+ chars, include upper, lower, number, special char",
                    Toast.LENGTH_LONG).show();
            return;
        }
        //sends data to php
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
                map.put("role", roles.getSelectedItem().toString());
                return map;
            }
        };
        //sends request
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
                //deletes user by email
                map.put("email", email.getText().toString());
                return map;
            }
        };
        //sends delete request
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
                //updates by email
                map.put("email", email.getText().toString());
                //new password
                map.put("password", password.getText().toString());
                return map;
            }
        };
        //sends request
        Volley.newRequestQueue(this).add(request);
    }
}