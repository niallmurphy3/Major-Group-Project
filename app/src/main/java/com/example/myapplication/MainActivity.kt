package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapBtn = findViewById<Button>(R.id.MapBtn)
        val socialBtn = findViewById<Button>(R.id.SocialFeedBtn)

        mapBtn.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        socialBtn.setOnClickListener {
            startActivity(Intent(this, SocialFeedActivity::class.java))
        }
    }
}