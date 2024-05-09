package com.example.tmdbdemotataaig.database

import androidx.room.TypeConverter
import com.google.gson.Gson

class MoviesListTypeConverters {
    @TypeConverter
    fun listToJsonString(value: List<Int>?): String = Gson().toJson(value)
    @TypeConverter
    fun jsonStringToList(value: String) = Gson().fromJson(value, Array<Int>::class.java).toList()
}