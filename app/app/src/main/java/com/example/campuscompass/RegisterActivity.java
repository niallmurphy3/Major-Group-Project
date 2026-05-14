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
    //strength checker (ensures pw is 8 digits or more, has a capital and a number. needs all 3
    private boolean isStrongPassword(String password) {
        boolean hasUpper = false;
        boolean hasNumber = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            }

            if (Character.isDigit(c)) {
                hasNumber = true;
            }
        }

        return password.length() >= 8 && hasUpper && hasNumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText name = findViewById(R.id.nameEditText);
        EditText email = findViewById(R.id.emailEditText);
        EditText password = findViewById(R.id.passwordEditText);
        EditText course = findViewById(R.id.courseEditText);
        Button registerBtn = findViewById(R.id.registerBtn);

        //when register is clicked
        registerBtn.setOnClickListener(v -> {

            //strength check password
            String pwd = password.getText().toString().trim();

            //if its not strong enough error
            if (!isStrongPassword(pwd)) {
                Toast.makeText(this,
                        "Password must be 8+ chars, include upper, lower, number, special char",
                        Toast.LENGTH_LONG).show();
                return;
            }//otherwise continue

            String url = "http://10.68.168.121/campusCompass/register.php";


            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    response -> {
                        //if server responds successfuly
                        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    },
                    error -> {
                        Toast.makeText(this,
                                error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
            ) {
                @Override
                //prepares information to be sent
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name.getText().toString().trim());
                    params.put("email", email.getText().toString().trim());
                    params.put("password", password.getText().toString().trim());
                    params.put("course", course.getText().toString().trim());
                    return params;
                }
            };
            //sends request
            queue.add(request);
        });
    }
}