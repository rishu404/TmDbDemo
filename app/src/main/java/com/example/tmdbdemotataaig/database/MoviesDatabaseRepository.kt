package com.example.tmdbdemotataaig.database

import androidx.lifecycle.LiveData
import com.example.tmdbdemotataaig.views.home.model.MovieModel

class MoviesDatabaseRepository(private val moviesDao: MoviesDao) {
    suspend fun insertMovies(moviesList: ArrayList<MovieModel>) {
        moviesDao.insertMovies(moviesList)
    }

    fun getAllMovies(pageNumber: Int): LiveData<List<MovieModel>> {
        return moviesDao.getAllMovies(20, pageNumber * 20)
    }
}