package com.example.localmoviesvolley

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.example.localmoviesvolley.adapters.CommentAdapter
import com.example.localmoviesvolley.data.Comment
import com.example.localmoviesvolley.data.MoviesRepository
import com.example.localmoviesvolley.databinding.ActivityCommentBinding
import org.json.JSONObject

class CommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentBinding
    private lateinit var repository: MoviesRepository
    private var isEditing = false
    var commentId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment)
        repository = MoviesRepository.getInstance(this)
        val extras = intent.extras
        val title = extras?.get(MOVIE_NAME)
        val id = extras?.get(MOVIE_ID)
        supportActionBar?.title = title.toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.newRating.also {
            it.minValue = 1
            it.maxValue = 5
        }
        binding.btnClose.setOnClickListener { isEditing(false) }
        binding.commentList.adapter = CommentAdapter({ comment ->
//          onEdit
            binding.apply {
                newUsername.setText(comment.username)
                newRating.value = comment.rating
                newReview.setText(comment.review)
                commentId = comment._id
                isEditing(true)
            }
        }, { index ->
//            onDelete
            Log.d("hello","delete $index")
            repository.deleteComment(index) {
                Log.d("hello","delete")
                getComments(id.toString().toInt())
            }
        })
        getComments(id.toString().toInt())

        binding.btnSubmit.setOnClickListener {
            if (isEditing) {
                Log.d("hello","SAVED")
                repository.updateComment(commentId, binding.newReview.text.toString(), binding.newRating.value) {
                    isEditing(false)
                    getComments(id.toString().toInt())
                }
            }
            else addComment(id.toString().toInt(), title.toString())

        }
    }

    private fun getComments(movieId: Int) {
        repository.getComments { jsonArray ->
            Thread.sleep(200)
            val comments = mutableListOf<Comment>()
            (0 until jsonArray.length()).forEach {
                val jsonObject = jsonArray.get(it) as JSONObject
                Log.d("hello", "$jsonObject")
                if (jsonObject["movieId"] as Int != movieId)
                    return@forEach
                val comment = Comment(
                    jsonObject["_id"] as Int,
                    jsonObject["movieId"] as Int,
                    jsonObject["movie"] as String,
                    jsonObject["username"] as String,
                    jsonObject["review"] as String,
                    jsonObject["rating"] as Int,
                    jsonObject["datePosted"] as String
                )
                comments.add(comment)
            }
            (binding.commentList.adapter as CommentAdapter).submitList(comments)
            (binding.commentList.adapter as CommentAdapter).notifyDataSetChanged()
        }
    }

    private fun addComment(id: Int, title: String) {
        repository.addComment(
            id.toString().toInt(),
            title,
            binding.newUsername.text.toString(),
            binding.newReview.text.toString(),
            binding.newRating.value
        ) {
            getComments(id.toString().toInt())
            binding.apply {
                newUsername.text.clear()
                newRating.value = 1
                newReview.text.clear()
            }
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            Thread.sleep(200)
            binding.commentList.smoothScrollToPosition((binding.commentList.adapter as CommentAdapter).itemCount)
        }
    }

    private fun isEditing(state: Boolean) {
        isEditing = state
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (isEditing) {
            binding.btnSubmit.text = "SAVE"
            binding.btnClose.visibility = View.VISIBLE
            imm.showSoftInput(binding.newReview, InputMethodManager.SHOW_IMPLICIT)
        }
        else {
            binding.btnSubmit.text = "SEND"
            binding.btnClose.visibility = View.GONE
            commentId = -1
            binding.newUsername.text.clear()
            binding.newRating.value = 1
            binding.newReview.text.clear()
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> return false
        }
        return true
    }
}