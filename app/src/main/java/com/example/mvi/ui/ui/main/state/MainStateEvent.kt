package com.example.mvi.ui.ui.main.state

sealed class MainStateEvent {
    class GetBlogPostEvent : MainStateEvent ()

    class GetUserEvent(
        val userId : String
    ) : MainStateEvent()
    class None : MainStateEvent()
}