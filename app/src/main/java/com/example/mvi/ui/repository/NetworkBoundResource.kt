package com.example.mvi.ui.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.mvi.ui.util.*
import com.example.mvi.ui.util.Constants.Companion.TESTING_NETWORK_DELAY
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResource<ResponseObject,ViewStateType> {
     protected val result  = MediatorLiveData<DataState<ViewStateType>>()

    init{
        result.value = DataState.loading(true)

        GlobalScope.launch(IO) {
            delay(TESTING_NETWORK_DELAY)
            withContext(Main){
                val apiResponse = createCall()
                result.addSource(apiResponse){response ->
                    result.removeSource(apiResponse)

                    handleNetworkCall(response)

                }

            }
        }
    }

   fun handleNetworkCall(response: GenericApiResponse<ResponseObject>){
       when (response){
           is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
           }
           is ApiErrorResponse -> {
                println("debug : NetworkBoundResource : ${response.errorMessage}")
                onReturnError(response.errorMessage)
           }
           is ApiEmptyResponse -> {
               println("debug : NetworkBoundResource : http 204. returned nothing")
               onReturnError("http 204. returned nothing")
           }
       }
    }
    fun onReturnError(message : String){
        result.value = DataState.error(message)
    }
    abstract fun createCall() : LiveData<GenericApiResponse<ResponseObject>>

    abstract fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>
}