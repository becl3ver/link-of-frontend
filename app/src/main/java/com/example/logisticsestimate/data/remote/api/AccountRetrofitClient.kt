package com.example.logisticsestimate.data.remote.api

import com.example.logisticsestimate.BuildConfig
import com.example.logisticsestimate.data.remote.api.service.AccountService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * 계정 기능에서 사용되는 레트로핏 객체 생성
 */
class AccountRetrofitClient {
    companion object {
        var accountService: AccountService? = null

        fun getInstance(): AccountService {
            if(accountService == null) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                val helper = SSLCertificateHelper()
                val builder = OkHttpClient.Builder()

                helper.setSSLTrust(builder, BuildConfig.SSL_CERTIFICATE_URL)

                val client = builder
                    .addInterceptor(interceptor)
                    .addInterceptor(AuthInterceptor())
                    .build()

                accountService = Retrofit.Builder()
                    .baseUrl(BuildConfig.SPRING_API_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(
                        GsonBuilder().setLenient().create()
                    ))
                    .build()
                    .create(AccountService::class.java)
            }

            return accountService!!
        }
    }
}