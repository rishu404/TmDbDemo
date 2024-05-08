package com.example.tmdbdemotataaig.views.popular_movies_fragment

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError
import com.example.tmdbdemotataaig.listeners.NetworkDispatcher
import com.example.tmdbdemotataaig.utils.AppUtilsKotlin
import com.example.tmdbdemotataaig.utils.GlobalConfigs
import com.example.tmdbdemotataaig.utils.MoviesUrls
import com.example.tmdbdemotataaig.utils.NetworkManager
import com.example.tmdbdemotataaig.views.popular_movies_fragment.model.MovieModel
import com.example.tmdbdemotataaig.views.popular_movies_fragment.model.MoviesResponseModel
import com.google.gson.Gson

class PopularMoviesViewModel : ViewModel() {
    private val mTAG = PopularMoviesViewModel::class.java.simpleName.toString()
    private var mNetworkManager: NetworkManager? = null
    private lateinit var mUrlBuilder: Uri.Builder
    private var popularMoviesMutableList: MutableLiveData<ArrayList<MovieModel>>? =
        MutableLiveData()

    fun getPopularMoviesList(): MutableLiveData<ArrayList<MovieModel>>? {
        return popularMoviesMutableList
    }

    var isLoadingMutable: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getIsLoadingMutable(): MutableLiveData<Boolean> {
        return isLoadingMutable
    }

    fun getPopularMovies(activity: Activity, pageNumber: Int) {
        if (AppUtilsKotlin.isNetworkAvailable(activity.applicationContext)) {
            isLoadingMutable.value = true
            getNetworkManager()
            mNetworkManager?.getRequest(
                getPopularMoviesUrl(pageNumber),
                object : NetworkDispatcher {
                    override fun getResponse(tag: String, data: String) {
                        if (data != "") {
                                val moviesResponseModel = Gson().fromJson(data, MoviesResponseModel::class.java)
                                AppUtilsKotlin.showLog(
                                    mTAG,
                                    "moviesResponseModel: $moviesResponseModel"
                                )
                                popularMoviesMutableList?.value = moviesResponseModel?.moviesList
                        } else {
                            popularMoviesMutableList?.value = arrayListOf()
                        }
                        AppUtilsKotlin.showLog(mTAG, "movies response: $data")
                    }

                    override fun getError(tag: String, anError: ANError?) {
                        AppUtilsKotlin.showLog(mTAG, "error in getting movies")
                    }

                },
                activity.applicationContext
            )
        } else {
            // toDo: to add the pop-up or error banner for no internet
        }

    }


    private fun getPopularMoviesUrl(pageNumber: Int): String {
        mUrlBuilder = Uri.Builder()
        mUrlBuilder.appendPath(MoviesUrls.POPULAR_MOVIES_URL)
            .appendQueryParameter(MoviesUrls.API_KEY_STRING, GlobalConfigs.API_KEY)
            .appendQueryParameter("page", pageNumber.toString())
        return mUrlBuilder.build().toString()
    }


    /**
     * Check network manager instance.
     */
    private fun getNetworkManager() {
        if (mNetworkManager == null) {
            mNetworkManager = NetworkManager()
        }
    }
}