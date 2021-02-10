package com.example.appcent.ui

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.*
import com.example.appcent.R
import com.example.appcent.data.Repository
import com.example.appcent.model.Game
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ViewModel(application: Application) : AndroidViewModel(application) {

    val onError: MutableLiveData<Throwable> = MutableLiveData()
    val games: MediatorLiveData<List<Game>> = MediatorLiveData()
    val gameDetail: MediatorLiveData<Game> = MediatorLiveData()
    val filteredGames: MutableLiveData<List<Game>> = MutableLiveData()
    val favGames: MutableLiveData<List<Game>> = MutableLiveData()
    val allGames = mutableListOf<Game>()
    private val compositeDisposable = CompositeDisposable()
    var isLastPage = true

    private var sharedPref = application
        .getSharedPreferences(
            application.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

    private val repository: Repository
        get() {
            return Repository.instance
        }

    fun getGames(page: Int) {
        compositeDisposable.add(
            repository.getGames(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        isLastPage = it.next.isNullOrEmpty()
                        games.postValue(it.results)
                    },
                    { onError.postValue(it) }
                )
        )
    }

    fun getGameDetail(id: Int) {
        compositeDisposable.add(
            repository.getGameDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        it.isFav = checkIfFavorite(it.id)
                        gameDetail.postValue(it)
                    },
                    { onError.postValue(it) }
                )
        )
    }

    fun getFavGames() {
        val locallyStoredIds = getFavoriteList()
        val favsToAdd = mutableListOf<Game>()
        if (!locallyStoredIds.isNullOrEmpty()) {
            games.value?.let {
                it.forEach { item ->
                    if (locallyStoredIds.contains(item.id))
                        favsToAdd.add(item)
                }
            }
        }
        favGames.postValue(favsToAdd)
    }

    fun resetData() {
        favGames.value = null
    }

    private fun getFavoriteList(): List<Int> {
        return if (sharedPref.all.values.toList().firstOrNull() is Int)
            (sharedPref.all.values.toList() as List<Int>)
        else
            listOf()
    }

    fun addToFavorites(id: Int) {
        sharedPref.edit {
            putInt(id.toString(), id)
            apply()
        }
    }

    fun removeFromFavorites(id: Int) {
        sharedPref.edit {
            remove(id.toString())
            apply()
        }
    }

    private fun checkIfFavorite(id: Int): Boolean {
        return sharedPref.getInt(id.toString(), -1) != -1
    }


    /**
     * returns a filtered list by the given query word
     */
    fun filterGames(query: String) {
        val list = copyGames()
        val filteredList = mutableListOf<Game>()
        list.let {
            filteredList.addAll(it.filter { game -> game.name.toLowerCase().contains(query) })
        }
        filteredGames.postValue(filteredList)
    }

    private fun copyGames(): MutableList<Game> {
        val list = mutableListOf<Game>()
        games.value?.let {
            for (item in it) {
                list.add(item.copy())
            }
        }
        return list
    }

    override fun onCleared() {
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()

        super.onCleared()
    }
}