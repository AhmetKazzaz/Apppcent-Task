package com.example.appcent


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

fun <T> Observable<T>.toLiveData(): LiveData<T> {

    return object : MutableLiveData<T>() {
        var disposable: Disposable? = null;

        override fun onActive() {
            super.onActive()

            // Observable -> LiveData
            disposable = this@toLiveData.subscribe({ this.postValue(it) }, {})
        }

        override fun onInactive() {
            disposable?.dispose();
            super.onInactive()
        }
    }
}