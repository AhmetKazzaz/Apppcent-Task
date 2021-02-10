package com.example.appcent.data

import com.example.appcent.api.AppService
import com.example.appcent.api.RetrofitClient
import com.example.appcent.model.Game
import com.example.appcent.model.response.GamesResponse
import io.reactivex.Observable

class Repository private constructor() {

    companion object {
        var instance: Repository = Repository()
    }

    private val service: AppService = RetrofitClient.instance.service

    fun getGames(page: Int): Observable<GamesResponse> {
        return service.games(page)
    }

    fun getGameDetail(id: Int): Observable<Game> {
        return service.gameDetail(id)
    }
}