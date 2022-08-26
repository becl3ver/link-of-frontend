package com.example.logisticsestimate.data.remote.api

import com.example.logisticsestimate.BuildConfig
import com.example.logisticsestimate.data.remote.api.service.BoardService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * 게시판 통신 레트로핏 객체 생성
 */
class BoardRetrofitClient {
    companion object {
        var boardService: BoardService? = null

        fun getInstance(): BoardService {
            if(boardService == null) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                val helper = SSLCertificateHelper()
                val builder = OkHttpClient.Builder()

                helper.setSSLTrust(builder, BuildConfig.SSL_CERTIFICATE_URL)

                val client = builder
                    .addInterceptor(interceptor)
                    .addInterceptor(AuthInterceptor())
                    .build()

                boardService = Retrofit.Builder()
                    .baseUrl(BuildConfig.SPRING_API_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder().setLenient().create()
                        )
                    )
                    .build()
                    .create(BoardService::class.java)
            }

            return boardService!!
        }

    }
}