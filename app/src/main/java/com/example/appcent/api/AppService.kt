package com.example.appcent.api


import androidx.annotation.Nullable
import com.example.appcent.model.Game
import com.example.appcent.model.response.GamesResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface AppService {
    @GET("games")
    fun games(@Query("page") page: Int): Observable<GamesResponse>

    @GET("games/{id}")
    fun gameDetail(@Path("id") id: Int): Observable<Game>
}