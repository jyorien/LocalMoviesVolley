package com.example.localmoviesvolley.data

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

class MoviesRepository(private val context: Context) {
    private val url = "http://192.168.0.101:8080/"
    private val moviesUrl = "movies/"
    private val commentsUrl = "comments/"
    private val movieService = MovieService.getInstance(context.applicationContext)

    companion object {
        private var INSTANCE: MoviesRepository? = null
        fun getInstance(context: Context): MoviesRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = MoviesRepository(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    fun getMovies(callback: (JSONArray) -> Unit) {
//        GET /movies
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            "$url$moviesUrl",
            null,
            { response ->
                callback(response)
            },
            { error -> Log.d("hello", "error $error") })
        movieService.addToRequestQueue(jsonArrayRequest)
    }

    fun getComments(callback: (JSONArray) -> Unit) {
//        GET /comments
        val jsonArrayRequest =
            JsonArrayRequest(
                Request.Method.GET,
                "$url$commentsUrl",
                null,
                { response -> callback(response) },
                { error -> Log.d("hello", "error $error") })
        movieService.addToRequestQueue(jsonArrayRequest)
    }

    fun addComment(movieId: Int, movie: String, username: String, review: String, rating: Int, callback: (Int) -> Unit) {
//        POST /comments
        val jsonObject = JSONObject()
        jsonObject.put("movieId", movieId)
        jsonObject.put("movie", movie)
        jsonObject.put("username", username)
        jsonObject.put("review", review)
        jsonObject.put("rating", rating)
        val jsonRequest = JsonObjectRequest(Request.Method.POST, "$url$commentsUrl", jsonObject, {
            callback(200)

        }, {})
        movieService.addToRequestQueue(jsonRequest)
    }

    fun deleteComment(index: Int, callback: (Int) -> Unit) {
//        DELETE /comments/index
        Log.d("hello","delete AHHHH")
        val jsonRequest =
            JsonObjectRequest(Request.Method.DELETE, "$url$commentsUrl$index", null, {
                Log.d("hello","delete???")
                callback(200)
            }, {
                Log.d("hello","error $it")
            })
        movieService.addToRequestQueue(jsonRequest)
    }

    fun updateComment(index: Int, review: String, rating: Int, callback: (Int) -> Unit) {
//        UPDATE /comments/index
        val jsonObject = JSONObject()
        jsonObject.put("review", review)
        jsonObject.put("rating", rating)
        val jsonRequest =
            JsonObjectRequest(Request.Method.PUT, "$url$commentsUrl$index", jsonObject, {
                callback(200)
            }, {})
        movieService.addToRequestQueue(jsonRequest)
    }

}