package com.example.tmdbdemotataaig.listeners

import com.androidnetworking.error.ANError


/**
 * Dispatch network callbacks*/
interface NetworkDispatcher {
    fun getResponse(tag: String, data: String)
    fun getError(tag: String, anError: ANError?)
}