package com.alemicode.amromovies.core.network

import okhttp3.Interceptor
import okhttp3.Response

internal class AuthInterceptor(private val readAccessToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authenticatedRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $readAccessToken")
            .build()
        return chain.proceed(authenticatedRequest)
    }
}
