package com.example.appcent.util

import androidx.lifecycle.LiveData

class InitialLiveData<T : Any?> private constructor(data: T) : LiveData<T>() {
    init {
        postValue(data)
    }

    companion object {
        fun <T> create(data: T): LiveData<T> {
            return InitialLiveData(data)
        }
    }
}