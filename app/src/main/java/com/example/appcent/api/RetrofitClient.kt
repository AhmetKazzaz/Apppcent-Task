package com.example.appcent.api


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class RetrofitClient private constructor() {
    private val retrofit: Retrofit
    val service: AppService

    companion object {
        const val BASE_URL = "https://rawg-video-games-database.p.rapidapi.com/"
        const val apiKey = "1e893d1eecmshe34dfe2e61c9824p120b6djsn3627242df23c"
        const val apiHost = "rawg-video-games-database.p.rapidapi.com"
        val instance = RetrofitClient()
    }

    init {
        //For retrofit logging
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(Interceptor())
            .build()
        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, GsonDateFormatter())
            .create()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()


        service = retrofit.create(AppService::class.java)
    }
}