package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val posts: MutableList<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.username)
        val postText: TextView = view.findViewById(R.id.postText)
        val likeBtn: Button = view.findViewById(R.id.likeBtn)
        val timeStamp: TextView = view.findViewById(R.id.timeStamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        holder.username.text = post.username
        holder.postText.text = post.content
        holder.timeStamp.text = "Just now"

        holder.likeBtn.text = if (post.liked) "💔 Unlike" else "❤️ Like"

        holder.likeBtn.setOnClickListener {
            post.liked = !post.liked
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = posts.size
}