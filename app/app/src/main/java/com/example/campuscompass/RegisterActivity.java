package com.example.campuscompass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText name = findViewById(R.id.nameEditText);
        EditText email = findViewById(R.id.emailEditText);
        EditText password = findViewById(R.id.passwordEditText);
        Button registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(v -> {

            String url = "http://192.168.0.207/campusCompass/register.php";

            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    response -> {
                        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    },
                    error -> {
                        Toast.makeText(this,
                                error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name.getText().toString().trim());
                    params.put("email", email.getText().toString().trim());
                    params.put("password", password.getText().toString().trim());
                    return params;
                }
            };

            queue.add(request);
        });
    }
}