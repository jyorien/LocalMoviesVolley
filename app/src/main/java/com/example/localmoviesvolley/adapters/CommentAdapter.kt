package com.example.localmoviesvolley.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.localmoviesvolley.R
import com.example.localmoviesvolley.data.Comment

class CommentAdapter(val onEdit: (Comment) -> Unit, val onDelete: (Int) -> Unit) :
    ListAdapter<Comment, CommentVH>(CommentComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.comment_list_item, parent, false)
        return CommentVH(view)
    }

    override fun onBindViewHolder(holder: CommentVH, position: Int) {
        holder.bind(getItem(position), onEdit, onDelete)
    }
}

class CommentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val username = itemView.findViewById<TextView>(R.id.comment_username)
    val rating = itemView.findViewById<TextView>(R.id.comment_rating)
    val review = itemView.findViewById<TextView>(R.id.comment_review)
    val date = itemView.findViewById<TextView>(R.id.comment_date)
    val btnEdit = itemView.findViewById<ImageButton>(R.id.btn_edit)
    val btnDelete = itemView.findViewById<ImageButton>(R.id.btn_delete)
    fun bind(comment: Comment, onEdit: (Comment) -> Unit, onDelete: (Int) -> Unit) {
        val ratingNumber = "Rating: ${comment.rating}"
        username.text = comment.username
        rating.text = ratingNumber
        review.text = comment.review
        date.text = comment.datePosted
        btnEdit.setOnClickListener { onEdit(comment) }
        btnDelete.setOnClickListener { onDelete(comment._id) }
    }

}

class CommentComparator : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem._id == newItem._id
    }

}