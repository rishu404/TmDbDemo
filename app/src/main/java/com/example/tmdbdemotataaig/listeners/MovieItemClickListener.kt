package com.example.tmdbdemotataaig.listeners

import com.example.tmdbdemotataaig.views.home.model.MovieModel

interface MovieItemClickListener {
    fun onMovieItemClick(movieModel: MovieModel)
}