package com.example.tmdbdemotataaig.views.home.model

import com.google.gson.annotations.SerializedName


data class MoviesResponseModel(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var moviesList: ArrayList<MovieModel> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null
)