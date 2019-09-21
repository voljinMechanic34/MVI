package com.example.mvi.ui.ui.main.state

import com.example.mvi.ui.models.BlogPost
import com.example.mvi.ui.models.User

data class MainViewState(
    var blogsPosts : List<BlogPost>? = null,
    var user : User? = null
) {
}