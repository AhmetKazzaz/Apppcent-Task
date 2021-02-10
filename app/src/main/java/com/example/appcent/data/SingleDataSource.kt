package com.example.appcent.data

import androidx.paging.PageKeyedDataSource
import com.example.appcent.model.Game

class StringDataSource(val provider: ListProvider) : PageKeyedDataSource<Int, Game>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Game>
    ) {
        val list = provider.getList(0, params.requestedLoadSize)
        callback.onResult(list, 1, 2)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Game>) {
        val list = provider.getList(params.key, params.requestedLoadSize)
        callback.onResult(list, params.key + 1)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Game>) {
        val list = provider.getList(params.key, params.requestedLoadSize)
        val nextIndex = if (params.key > 1) params.key - 1 else null
        callback.onResult(list, nextIndex)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
