package com.example.appcent.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class Interceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val rqBuilder = chain.request().newBuilder()
        rqBuilder.addHeader("Accept", "application/json")
        rqBuilder.addHeader("x-rapidapi-key", RetrofitClient.apiKey)
        rqBuilder.addHeader("x-rapidapi-host", RetrofitClient.apiHost)

        return chain.proceed(rqBuilder.build())
    }
}