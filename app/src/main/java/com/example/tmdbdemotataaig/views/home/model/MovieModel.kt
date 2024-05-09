package com.example.tmdbdemotataaig.views.home.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class MovieModel(
    @ColumnInfo("adult")
    @SerializedName("adult")
    var adult: Boolean? = null,
    @ColumnInfo("backdrop_path")
    @SerializedName("backdrop_path")
    var backdropPath: String? = null,
    @ColumnInfo("genre_ids")
    @SerializedName("genre_ids")
    var genreIds: List<Int> = listOf(),
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    @SerializedName("id")
    var id: Int? = 0,
    @ColumnInfo("original_language")
    @SerializedName("original_language")
    var originalLanguage: String? = null,
    @ColumnInfo("original_title")
    @SerializedName("original_title")
    var originalTitle: String? = null,
    @ColumnInfo("overview")
    @SerializedName("overview")
    var overview: String? = null,
    @ColumnInfo("popularity")
    @SerializedName("popularity")
    var popularity: Double? = null,
    @ColumnInfo("poster_path")
    @SerializedName("poster_path")
    var posterPath: String? = null,
    @ColumnInfo("release_date")
    @SerializedName("release_date")
    var releaseDate: String? = null,
    @ColumnInfo("title")
    @SerializedName("title")
    var title: String? = null,
    @ColumnInfo("video")
    @SerializedName("video")
    var video: Boolean? = null,
    @ColumnInfo("vote_average")
    @SerializedName("vote_average")
    var voteAverage: Double? = null,
    @ColumnInfo("vote_count")
    @SerializedName("vote_count")
    var voteCount: Int? = null
)
