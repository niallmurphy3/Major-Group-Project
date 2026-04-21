package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SocialFeedActivity : AppCompatActivity() {

    private lateinit var adapter: PostAdapter
    private val posts = mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_feed)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val postInput = findViewById<EditText>(R.id.postInput)
        val postBtn = findViewById<Button>(R.id.postBtn)
        val backBtn = findViewById<Button>(R.id.backBtn)

        adapter = PostAdapter(posts)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        postBtn.setOnClickListener {
            val text = postInput.text.toString()

            if (text.isNotEmpty()) {
                posts.add(0, Post(text, "You"))
                adapter.notifyItemInserted(0)
                recyclerView.scrollToPosition(0)
                postInput.text.clear()
            } else {
                Toast.makeText(this, "Write something!", Toast.LENGTH_SHORT).show()
            }
        }

        backBtn.setOnClickListener {
            finish()
        }
    }
}