package com.example.myapplication

data class Post(
    val content: String,
    val username: String,
    var liked: Boolean = false
)