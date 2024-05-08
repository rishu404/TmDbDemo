package com.example.tmdbdemotataaig.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast

object AppUtilsKotlin {

    private const val TAG = "AppUtilsKotlin"

    fun showLog(tag: String, message: String) {
        Log.d(tag, message)
    }
    fun showErrorLog(tag: String, message: String) {
        Log.e(tag, message)
    }

    /**
     * Check if device is connected to internet*/
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    /**
     * Show Toast
     *
     * @param mActivity
     * @param msg
     */
    fun showToast(mActivity: Activity?, msg: String?) {
        try {
            if (mActivity != null && !msg.isNullOrEmpty()) {
                mActivity.runOnUiThread {
                    Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                showErrorLog(TAG, "mActivity is null on showToast")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}