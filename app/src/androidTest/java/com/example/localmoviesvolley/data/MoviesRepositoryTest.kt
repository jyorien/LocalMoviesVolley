package com.example.localmoviesvolley.data

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MoviesRepositoryTest {
    private lateinit var context: Context
    private lateinit var repository: MoviesRepository

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        repository = MoviesRepository.getInstance(context)
    }

    @Test
    fun test_getComments() {
        var commentsString = ""
        repository.getComments { comments ->
            commentsString = comments.toString()
            Log.d("hello", commentsString)
            assert(commentsString != "")
        }

    }

    @Test
    fun test_addComment() {
        repository.addComment {
            assert(it == 200)
        }
    }

    @Test
    fun test_deleteComment() {
        repository.deleteComment(81) {
            assert(it == 200)
        }
    }

    @Test
    fun test_updateComment() {
        repository.updateComment(80, "Hello there", 1) {
            assert(it == 200)
        }
    }


}