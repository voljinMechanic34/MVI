package com.example.mvi.ui.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mvi.ui.ui.main.state.MainStateEvent
import com.example.mvi.ui.ui.main.state.MainStateEvent.*
import com.example.mvi.ui.ui.main.state.MainViewState
import com.example.mvi.ui.models.BlogPost
import com.example.mvi.ui.models.User
import com.example.mvi.ui.repository.MainRepository
import com.example.mvi.ui.util.AbsentLiveData
import com.example.mvi.ui.util.DataState

class MainViewModel : ViewModel(){

    private val _stateEvent : MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState : MutableLiveData<MainViewState> = MutableLiveData()

    val viewState : LiveData<MainViewState> get() = _viewState

    val dataState : LiveData<DataState<MainViewState>> = Transformations
        .switchMap(_stateEvent){
            stateEvent ->
                    stateEvent?.let {
                        handleStateEvent(it)
                    }
        }
    fun handleStateEvent(stateEvent: MainStateEvent) : LiveData<DataState<MainViewState>>{
        when (stateEvent){
            is GetBlogPostEvent -> {
                return MainRepository.getBlogPosts()
            }
            is GetUserEvent -> {
                return MainRepository.getUser(stateEvent.userId)
            }
            is None -> {
                return AbsentLiveData.create()
            }
        }
    }
    fun setBlogListData(blogPosts: List<BlogPost>){
            val update = getCurrentViewStateorNew()
            update.blogsPosts = blogPosts
            _viewState.value = update
    }
    fun setUser(user : User){
        val update  = getCurrentViewStateorNew()
        update.user = user
        _viewState.value = update

    }
    fun getCurrentViewStateorNew():MainViewState{
        val value = viewState.value?.let {
            it
        }?: MainViewState()
        return value
    }
    fun setStateEvent(event : MainStateEvent){
        _stateEvent.value = event
    }
}