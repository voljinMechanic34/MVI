package com.example.mvi.ui.api

import com.example.mvi.ui.util.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyRetrofitBuilder {

    const val BASE_URL = "https://open-api.xyz/"
    val retrofitBuilder : Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
    }

    val apiService : ApiService by lazy {
            retrofitBuilder
                .build()
                .create(ApiService::class.java)
    }
}