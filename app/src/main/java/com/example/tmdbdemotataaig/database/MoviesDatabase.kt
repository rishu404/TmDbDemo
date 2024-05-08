package com.example.tmdbdemotataaig.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tmdbdemotataaig.views.popular_movies_fragment.model.MovieModel

@Database(entities = [MovieModel::class], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDao(): MoviesDao

    companion object {
        @Volatile
        private var INSTANCE: MoviesDatabase? = null
        private const val databaseName = "movies_database"

        fun getMoviesDatabase(
            context: Context
        ): MoviesDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoviesDatabase::class.java,
                    databaseName
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}