package com.example.tmdbdemotataaig.utils

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import com.example.tmdbdemotataaig.appcontroller.AppController
import com.example.tmdbdemotataaig.listeners.NetworkDispatcher
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.concurrent.TimeUnit

class NetworkManager {
    /**
     * Debugging TAG
     */
    var mTAG: String = "NetworkManager"

    /**
     * Android networking get request
     * @param endPointUrl API url
     * @param listener for callbacks
     **/
    fun getRequest(endPointUrl: String, listener: NetworkDispatcher) {
        if (endPointUrl.trim().isEmpty()) {
            AppUtilsKotlin.showErrorLog(
                mTAG, "Get end points URL is empty so direct return."
            )
            return
        }
        var client = OkHttpClient()
        if (endPointUrl != "" && AppController().getInstance()?.okHttpClient != null) {
            client = AppController().getInstance()?.okHttpClient!!
        } else {
            client.newBuilder().connectTimeout(
                GlobalConfigs.APITimeout, TimeUnit.MILLISECONDS
            ).readTimeout(
                GlobalConfigs.APITimeout, TimeUnit.MILLISECONDS
            ).writeTimeout(
                GlobalConfigs.APITimeout, TimeUnit.MILLISECONDS
            ).addNetworkInterceptor(getHttpLogging()).build()
        }
        val mURL = MoviesUrls.BASE_URL + endPointUrl
        AppUtilsKotlin.showLog(mTAG, "this is getRequest & url is: $mURL")


        AndroidNetworking.get(mURL)
            .build()
            .getAsOkHttpResponseAndString(object : OkHttpResponseAndStringRequestListener {
                override fun onResponse(okHttpResponse: Response?, response: String?) {
                    if (response != null) {
                        listener.getResponse(mURL, response)
                    } else {
                        AppUtilsKotlin.showErrorLog(mTAG, "response is null")
                    }
                }

                override fun onError(anError: ANError?) {
                    if (anError == null) {
                        AppUtilsKotlin.showErrorLog(
                            mTAG,
                            "anError is null get"
                        )
                        return
                    }
                    AppUtilsKotlin.showLog(
                        mTAG,
                        "get error response code : " + anError.errorCode.toString() + "error body : ${anError.errorBody} error response : ${anError.response} error Detail : ${anError.errorDetail}"
                    )

                    listener.getError(mURL, anError)
                }
            })
    }

    private fun getHttpLogging(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }
}