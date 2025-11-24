package com.example.myapplicationlab6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CommentAdapter(private val items: List<Comment>) : RecyclerView.Adapter<CommentAdapter.Holder>() {
    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.comment_image)
        val name: TextView = view.findViewById(R.id.comment_name)
        val text: TextView = view.findViewById(R.id.comment_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val comment = items[position]
        holder.name.text = comment.name ?: "—"
        holder.text.text = comment.description ?: ""
        Glide.with(holder.image.context)
            .load(comment.image)
            .placeholder(R.drawable.ic_placeholder_avatar)
            .into(holder.image)
    }

    override fun getItemCount(): Int = items.size
}
