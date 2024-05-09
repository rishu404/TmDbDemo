package com.example.tmdbdemotataaig.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tmdbdemotataaig.views.home.model.MovieModel

@Database(entities = [MovieModel::class], version = 1, exportSchema = false)
@TypeConverters(MoviesListTypeConverters::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDao(): MoviesDao

    companion object {
        @Volatile
        private var INSTANCE: MoviesDatabase? = null
        private const val MOVIES_DATABASE_NAME = "movies_database"

        fun getMoviesDatabase(
            context: Context
        ): MoviesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoviesDatabase::class.java,
                    MOVIES_DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}