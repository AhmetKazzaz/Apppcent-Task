package com.example.appcent.api

import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

// This is RetrofitError converted to Retrofit 2
class RetrofitException private constructor(
    message: String?,
    /**
     * The request URL which produced the error.
     */
    val url: String?,
    /**
     * Response object containing Status code, headers, body, etc.
     */
    val response: Response<*>?,
    /**
     * The event kind which triggered this error.
     */
    val kind: Kind,
    exception: Throwable?,
    /**
     * The Retrofit this request was executed on
     */
    val retrofit: Retrofit?,
    val statusCode: Int
) :
    RuntimeException(message, exception) {

//        /**
//     * HTTP response body converted to specified {@code type}. {@code null} if there is no
//     * response.
//     *
//     * @throws IOException if unable to convert the body to the specified {@code type}.
//     */
//    public ErrorResponse errorResponse() {
//        if (response == null || response.errorBody() == null) {
//            return null;
//        }
//        Converter<ResponseBody, ErrorResponse> converter = retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
//
//        ErrorResponse result = null;
//        try {
//            result = converter.convert(response.errorBody());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
    /**
     * Identifies the event kind which triggered a [RetrofitException].
     */
    enum class Kind {
        /**
         * An [IOException] occurred while communicating to the server.
         */
        NETWORK,
        /**
         * A non-200 HTTP Status code was received from the server.
         */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    companion object {
        fun httpError(
            url: String?,
            response: Response<*>,
            retrofit: Retrofit?
        ): RetrofitException {
            val message = response.code().toString() + " " + response.message()
            return RetrofitException(
                message,
                url,
                response,
                Kind.HTTP,
                null,
                retrofit,
                response.code()
            )
        }

        fun networkError(exception: IOException): RetrofitException {
            return RetrofitException(
                exception.message,
                null,
                null,
                Kind.NETWORK,
                exception,
                null,
                -1
            )
        }

        fun unexpectedError(exception: Throwable): RetrofitException {
            return RetrofitException(
                exception.message,
                null,
                null,
                Kind.UNEXPECTED,
                exception,
                null,
                -1
            )
        }
    }

}