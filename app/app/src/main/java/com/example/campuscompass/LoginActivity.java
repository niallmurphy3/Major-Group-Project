package com.example.campuscompass;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.loginBtn).setOnClickListener(v -> {

            String email = ((android.widget.EditText) findViewById(R.id.emailEditText))
                    .getText().toString().trim();
            String password = ((android.widget.EditText) findViewById(R.id.passwordEditText))
                    .getText().toString().trim();

            String url = "http://192.168.0.207/campusCompass/login.php";

            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    response -> {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");

                            if (status.equals("success")) {
                                String name = obj.getString("name");
                                Toast.makeText(this,
                                        "Welcome " + name,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                String message = obj.getString("message");
                                Toast.makeText(this,
                                        message,
                                        Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(this,
                                    "Response error",
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Toast.makeText(this,
                                "Network error",
                                Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            queue.add(request);
        });
    }
}
