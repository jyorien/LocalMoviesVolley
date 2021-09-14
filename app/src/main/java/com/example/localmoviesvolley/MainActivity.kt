package com.example.localmoviesvolley

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.localmoviesvolley.adapters.MovieAdapter
import com.example.localmoviesvolley.data.Movie
import com.example.localmoviesvolley.data.MoviesRepository
import com.example.localmoviesvolley.databinding.ActivityMainBinding
import org.json.JSONObject

const val MOVIE_ID = "movieId"
const val MOVIE_NAME = "movieName"
class MainActivity : AppCompatActivity() {
    private lateinit var repository: MoviesRepository
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        repository = MoviesRepository.getInstance(this)
        binding.movieList.adapter = MovieAdapter { movie ->
            Intent(this, CommentActivity::class.java).also {
                it.putExtra(MOVIE_ID, movie._id)
                it.putExtra(MOVIE_NAME, movie.title)
                startActivity(it)
            }
        }
        getMovies()
    }

    private fun getMovies() {
        val movies = mutableListOf<Movie>()
        repository.getMovies {
            (0 until it.length()).forEach { index ->
                val jsonArray = it.get(index) as JSONObject
                val movie = Movie(
                    jsonArray["_id"] as Int,
                    jsonArray["title"] as String,
                    jsonArray["genre"] as String,
                    jsonArray["availability"] as String,
                )
                movies.add(movie)
            }
            (binding.movieList.adapter as MovieAdapter).submitList(movies)
        }
    }
}