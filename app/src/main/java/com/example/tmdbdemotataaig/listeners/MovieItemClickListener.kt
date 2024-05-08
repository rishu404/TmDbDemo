package com.example.tmdbdemotataaig.listeners

import com.example.tmdbdemotataaig.views.popular_movies_fragment.model.MovieModel

interface MovieItemClickListener {
    fun onMovieItemClick(movieModel: MovieModel)
}