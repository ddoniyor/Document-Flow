package com.example.documentflow.data.retrofit

import android.content.Context
import com.example.documentflow.App
import com.example.documentflow.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {
    private lateinit var apiInterface: ApiInterface


    fun getApiInterface(context: Context): ApiInterface {
        //initialize ApiInterface if not init yet
        if (!::apiInterface.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://temp-sed.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build()
            apiInterface = retrofit.create(ApiInterface::class.java)
        }
        return apiInterface
    }


    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }
}