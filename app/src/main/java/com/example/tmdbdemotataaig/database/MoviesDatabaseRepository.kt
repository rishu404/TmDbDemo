package com.example.tmdbdemotataaig.database

import com.example.tmdbdemotataaig.views.popular_movies_fragment.model.MovieModel

class MoviesDatabaseRepository(private val moviesDao: MoviesDao) {
    suspend fun insertMovies(moviesList: ArrayList<MovieModel>) {
        moviesDao.insertMovies(moviesList)
    }
}