package com.example.localmoviesvolley.data

data class Comment(
    val _id: Int,
    val movieId: Int,
    val movie: String,
    val username: String,
    val review: String,
    val rating: Int,
    val datePosted: String
)
