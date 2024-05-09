package com.example.tmdbdemotataaig.views.home

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError
import com.example.tmdbdemotataaig.BuildConfig
import com.example.tmdbdemotataaig.database.MoviesDatabaseController
import com.example.tmdbdemotataaig.listeners.NetworkDispatcher
import com.example.tmdbdemotataaig.utils.AppUtilsKotlin
import com.example.tmdbdemotataaig.utils.MoviesUrls
import com.example.tmdbdemotataaig.utils.NetworkManager
import com.example.tmdbdemotataaig.views.home.model.MovieModel
import com.example.tmdbdemotataaig.views.home.model.MoviesResponseModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PopularMoviesViewModel : ViewModel() {
    private val mTAG = PopularMoviesViewModel::class.java.simpleName.toString()
    private var mNetworkManager: NetworkManager? = null
    private lateinit var mUrlBuilder: Uri.Builder
    private var popularMoviesMutableList: MutableLiveData<ArrayList<MovieModel>>? =
        MutableLiveData()
    private var internetConnectStatusMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    fun getInternetConnectionStatus(): MutableLiveData<Boolean> {
        return internetConnectStatusMutableLiveData
    }

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
                            val moviesResponseModel =
                                Gson().fromJson(data, MoviesResponseModel::class.java)
                            AppUtilsKotlin.showLog(
                                mTAG,
                                "moviesResponseModel: $moviesResponseModel"
                            )
                            insertMoviesInDatabase(moviesResponseModel?.moviesList)
                            popularMoviesMutableList?.value = moviesResponseModel?.moviesList
                        } else {
                            popularMoviesMutableList?.value = arrayListOf()
                        }
                        AppUtilsKotlin.showLog(mTAG, "movies response: $data")
                    }

                    override fun getError(tag: String, anError: ANError?) {
                        AppUtilsKotlin.showLog(mTAG, "error in getting movies")
                    }

                }
            )
        } else {
            internetConnectStatusMutableLiveData.value = false
        }

    }

    private fun insertMoviesInDatabase(moviesList: ArrayList<MovieModel>?) {
        CoroutineScope(Dispatchers.IO).launch {
            moviesList?.let {
                MoviesDatabaseController.databaseRepository.insertMovies(
                    it
                )
            }
        }
    }

    fun getMoviesFromDatabase(owner: LifecycleOwner, pageNumber: Int) {
        isLoadingMutable.value = true
        MoviesDatabaseController.databaseRepository.getAllMovies(pageNumber).observe(owner) {
            popularMoviesMutableList?.value = null
            popularMoviesMutableList?.postValue(it as ArrayList<MovieModel>?)
        }
    }


    private fun getPopularMoviesUrl(pageNumber: Int): String {
        mUrlBuilder = Uri.Builder()
        mUrlBuilder.appendPath(MoviesUrls.POPULAR_MOVIES_URL)
            .appendQueryParameter(MoviesUrls.API_KEY_STRING, BuildConfig.API_KEY)
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