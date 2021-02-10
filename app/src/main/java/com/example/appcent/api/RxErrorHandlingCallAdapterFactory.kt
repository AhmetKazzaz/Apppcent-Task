package com.example.appcent.api

import com.example.appcent.api.RetrofitException.Companion.httpError
import com.example.appcent.api.RetrofitException.Companion.networkError
import com.example.appcent.api.RetrofitException.Companion.unexpectedError
import io.reactivex.Observable
import io.reactivex.functions.Function
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory private constructor() : CallAdapter.Factory() {
    private val original: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *> {
        return RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit))
    }

    private class RxCallAdapterWrapper<R> internal constructor(private val retrofit: Retrofit, private val wrapped: CallAdapter<R, *>?) : CallAdapter<R, Observable<*>> {
        override fun responseType(): Type {
            return wrapped!!.responseType()
        }

        override fun adapt(call: Call<R>): Observable<*> {
            return (wrapped!!.adapt(call) as Observable<*>).onErrorResumeNext(Function { throwable ->
                Observable.error(
                    asRetrofitException(throwable = throwable)
                )
            })
        }

        private fun asRetrofitException(throwable: Throwable): RetrofitException {
            // We had non-200 http error
            if (throwable is HttpException) {
                val response = throwable.response()
                if (response != null) {
                    return httpError(
                        response.raw().request.url.toString(),
                        response,
                        retrofit
                    )
                }
            }

            // A network error happened
            if (throwable is IOException) {
                return networkError(throwable)
            }

            // We don't know what happened. We need to simply convert to an unknown error
            return unexpectedError(throwable)
        }
    }

    companion object {
        fun create(): CallAdapter.Factory {
            return RxErrorHandlingCallAdapterFactory()
        }
    }

}