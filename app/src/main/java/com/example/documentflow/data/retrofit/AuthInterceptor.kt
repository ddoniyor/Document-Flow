package com.example.documentflow.data.retrofit

import android.content.Context
import android.util.Log
import com.example.documentflow.utils.SharedPreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor(context: Context):Interceptor {
    private companion object{
        const val TAG ="AuthInterceptor"
        const val BEARER = "Bearer"
    }
    private val sessionManager = SharedPreferencesHelper(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        Log.d(TAG, "${sessionManager.getAccessToken()} HELLO FROM INTERCEPTOR")

        /**If token was saved we add it in to header of request*/
        sessionManager.getAccessToken()?.let {
            requestBuilder.addHeader("Authorization", "$BEARER $it")
        }

        return chain.proceed(requestBuilder.build())

    }


}