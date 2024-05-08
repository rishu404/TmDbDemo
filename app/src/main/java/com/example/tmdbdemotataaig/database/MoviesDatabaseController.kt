package com.example.tmdbdemotataaig.database

import android.content.Context
import com.example.tmdbdemotataaig.database.MoviesDatabase.Companion.getMoviesDatabase

object MoviesDatabaseController {
    lateinit var databaseRepository: MoviesDatabaseRepository

    /**
     * initialise room database
     */
    fun initialiseRoomDB(context: Context) {
        val moviesDatabase = getMoviesDatabase(context)
        databaseRepository = MoviesDatabaseRepository(moviesDatabase.movieDao())
    }
}