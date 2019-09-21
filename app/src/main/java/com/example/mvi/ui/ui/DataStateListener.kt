package com.example.mvi.ui.ui

import com.example.mvi.ui.util.DataState

interface DataStateListener {
    fun onDataStateChange(datastate : DataState<*>?)
}