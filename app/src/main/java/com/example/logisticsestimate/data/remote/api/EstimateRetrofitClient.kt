package com.example.logisticsestimate.data.remote.api

import com.example.logisticsestimate.BuildConfig
import com.example.logisticsestimate.data.remote.api.service.EstimateService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 견적 조회 레트로핏 객체 생성
 */
class EstimateRetrofitClient {
    companion object {
        var estimateService : EstimateService? = null

        fun getInstance() : EstimateService {
            if(estimateService == null) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                val helper = SSLCertificateHelper()
                val builder = OkHttpClient.Builder()

                helper.setSSLTrust(builder, BuildConfig.SSL_CERTIFICATE_URL)

                val client = builder
                    .addInterceptor(interceptor)
                    .addInterceptor(AuthInterceptor())
                    .build()

                estimateService = Retrofit.Builder()
                    .baseUrl(BuildConfig.SPRING_API_URL)
                    .client(client)
                    .addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder().setLenient().create()
                        )
                    )
                    .build()
                    .create(EstimateService::class.java)
            }

            return estimateService!!
        }
    }
}