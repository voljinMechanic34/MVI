package com.example.mvi.ui.api

import androidx.lifecycle.LiveData
import com.example.mvi.ui.models.BlogPost
import com.example.mvi.ui.models.User
import com.example.mvi.ui.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("placeholder/blogs")
    fun getBlogPosts() : LiveData<GenericApiResponse<List<BlogPost>>>

    @GET("placeholder/user/{userId}")
    fun getUser(
        @Path("userId") userId : String
    ) : LiveData<GenericApiResponse<User>>
}