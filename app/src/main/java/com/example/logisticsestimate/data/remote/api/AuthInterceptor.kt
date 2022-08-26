package com.example.logisticsestimate.data.remote.api

import com.example.logisticsestimate.base.App
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", App.prefs.getAccessToken("none"))
            .build()
        return chain.proceed(request)
    }
}