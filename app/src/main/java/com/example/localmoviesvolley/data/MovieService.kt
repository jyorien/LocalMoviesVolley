package com.example.localmoviesvolley.data

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MovieService(context: Context) {
    companion object {
        private var INSTANCE: MovieService? = null

        fun getInstance(context: Context): MovieService {
            return INSTANCE ?: synchronized(this) {
                val instance = MovieService(context)
                INSTANCE = instance
                instance
            }
        }
    }

    private val requestQueue: RequestQueue by lazy { Volley.newRequestQueue(context.applicationContext) }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}