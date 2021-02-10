package com.example.appcent.data

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.appcent.model.Game
import io.reactivex.disposables.CompositeDisposable

class GamesDataFactory(private val compositeDisposable: CompositeDisposable) :
    DataSource.Factory<Int, Game>() {

    val sourceMutableLiveData: MutableLiveData<GamesDataSource> =
        MutableLiveData<GamesDataSource>()

    @NonNull
    override fun create(): DataSource<Int, Game> {
        val questionsDataSource = GamesDataSource(compositeDisposable)
        sourceMutableLiveData.postValue(questionsDataSource)
        return questionsDataSource
    }
}