package com.example.appcent.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.appcent.model.Game
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GamesDataSource(private val compositeDisposable: CompositeDisposable) :
    PageKeyedDataSource<Int, Game>() {

    private val repo = Repository.instance
    val postList = MutableLiveData<List<Game>>()


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Game>
    ) {
        compositeDisposable.add(repo.getGames(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.d("QuestionsDataSource", "Api fetch error: " + it.message)
            }
            .subscribe {
                callback.onResult(it.results, null, 2)
                postList.postValue(it.results)
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Game>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Game>) {
        compositeDisposable.add(repo.getGames(params.key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.d("QuestionsDataSource", "Api fetch error: " + it.message)
            }
            .subscribe {
                callback.onResult(it.results, params.key + 1)
                postList.value = it.results
            })
    }
}