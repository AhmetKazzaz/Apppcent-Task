package com.example.appcent.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.appcent.api.AppService
import com.example.appcent.api.RetrofitClient
import com.example.appcent.model.Game
import com.example.appcent.model.response.GamesResponse
import com.google.gson.Gson
import io.reactivex.Observable

class Repository private constructor() {

    companion object {
        var instance: Repository = Repository()
        var gson = Gson()
    }

    private val service: AppService = RetrofitClient.instance.service

    fun getGames(page: Int): Observable<GamesResponse> {
        return service.games(page)
    }

    fun getGameDetail(id: Int): Observable<Game> {
        return service.gameDetail(id)
    }

    fun addToFavorites(game: Game, sharedPref: SharedPreferences) {
        sharedPref.edit {
            val json: String = gson.toJson(game)
            putString(game.id.toString(), json)
            apply()
        }
    }

    fun removeFromFavorites(id: Int, sharedPref: SharedPreferences) {
        sharedPref.edit {
            remove(id.toString())
            apply()
        }
    }

    fun getGamesFromStrings(localStrings: List<String>): MutableList<Game> {
        val games = mutableListOf<Game>()
        localStrings.forEach {
            games.add(gson.fromJson(it, Game::class.java))
        }
        return games
    }
}