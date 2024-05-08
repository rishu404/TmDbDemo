package com.example.tmdbdemotataaig.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.tmdbdemotataaig.views.popular_movies_fragment.model.MovieModel

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(moviesArrayList: ArrayList<MovieModel>)
}