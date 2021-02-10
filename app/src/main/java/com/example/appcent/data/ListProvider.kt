package com.example.appcent.data

import com.example.appcent.model.Game

class ListProvider(private val list: List<Game>) {


    fun getList(page: Int, pageSize: Int): List<Game> {
        return list
    }
}
