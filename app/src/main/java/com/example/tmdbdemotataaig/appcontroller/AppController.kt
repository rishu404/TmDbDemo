package com.example.tmdbdemotataaig.appcontroller

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.example.tmdbdemotataaig.database.MoviesDatabase
import com.example.tmdbdemotataaig.database.MoviesDatabaseController
import okhttp3.OkHttpClient

public class AppController : Application() {
    /**
     * Debuggable TAG
     */
    val TAG = "AppController"

    /**
     * OkHttpClient used for sending cookies in all API requests.
     * This object gets initialized after the POST request of
     * get access token for invoke. There after all the APIs use
     * this object to send cookies.
     */
    var okHttpClient: OkHttpClient? = null

    /**
     * AppController reference object
     */
    private val mInstance: AppController? = null

    @Synchronized
    fun getInstance(): AppController? {
        return mInstance
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        MoviesDatabaseController.initialiseRoomDB(this)
    }

    fun getOkHttpClientNonNull(): OkHttpClient {
        return okHttpClient!!
    }

    fun setLocalOkHttpClient(okHttpClient: OkHttpClient?) {
        this.okHttpClient = okHttpClient
    }


}
