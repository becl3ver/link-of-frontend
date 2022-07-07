package com.example.logisticsestimate.repository

import com.example.logisticsestimate.BuildConfig
import com.example.logisticsestimate.request.AccountService
import com.example.logisticsestimate.request.AuthInterceptor
import com.example.logisticsestimate.request.SSLCertificateHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AccountRetrofitBuilder {
    companion object {
        fun getInstance() : AccountService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val helper = SSLCertificateHelper()
            val builder = OkHttpClient.Builder()

            helper.setSSLTrust(builder, BuildConfig.SSL_CERTIFICATE_URL)

            val client = builder
                .addInterceptor(interceptor)
                .addInterceptor(AuthInterceptor())
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.SPRING_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AccountService::class.java)
        }
    }
}