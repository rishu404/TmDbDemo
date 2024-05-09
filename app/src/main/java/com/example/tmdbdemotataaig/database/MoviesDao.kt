package com.example.tmdbdemotataaig.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdbdemotataaig.views.home.model.MovieModel

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovies(moviesArrayList: ArrayList<MovieModel>)

    @Query("SELECT * FROM movies ORDER BY id ASC LIMIT :pageSize OFFSET :offset")
    fun getAllMovies(pageSize: Int, offset: Int): LiveData<List<MovieModel>>
}