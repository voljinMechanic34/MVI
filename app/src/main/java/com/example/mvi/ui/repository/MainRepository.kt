package com.example.mvi.ui.repository

import androidx.lifecycle.LiveData
import com.example.mvi.ui.api.MyRetrofitBuilder
import com.example.mvi.ui.ui.main.state.MainViewState
import com.example.mvi.ui.models.BlogPost
import com.example.mvi.ui.models.User
import com.example.mvi.ui.util.ApiSuccessResponse

import com.example.mvi.ui.util.DataState
import com.example.mvi.ui.util.GenericApiResponse

object MainRepository {
    fun getBlogPosts() : LiveData<DataState<MainViewState>>{
            return object : NetworkBoundResource<List<BlogPost>,MainViewState>(){
                override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
                   return MyRetrofitBuilder.apiService.getBlogPosts()
                }

                override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {
                    result.value = DataState.data(
                       data = MainViewState(
                            blogsPosts = response.body
                        )
                    )
                }

            }.asLiveData()
    }

    fun getUser(userId : String) : LiveData<DataState<MainViewState>>{

        return object : NetworkBoundResource<User,MainViewState>(){
            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return MyRetrofitBuilder.apiService.getUser(userId)
            }

            override fun handleApiSuccessResponse(response: ApiSuccessResponse<User>) {
                result.value = DataState.data(
                    data = MainViewState(
                        user = response.body
                    )
                )
            }


        }.asLiveData()
    }
}