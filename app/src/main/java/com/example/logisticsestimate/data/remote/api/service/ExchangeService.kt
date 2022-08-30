package com.example.logisticsestimate.data.remote.api.service

import com.example.logisticsestimate.data.remote.model.exchange.ExchangeRateDto
import retrofit2.Call
import retrofit2.http.*

interface ExchangeService {
    @GET("/site/program/financial/exchangeJSON")
    fun getExchangeRate(
        @Query("authkey") authKey : String,            // 필수
        @Query("searchdate") searchDate : String,      // 선택
        @Query("data") data : String                   // 필수
    ) : Call<ExchangeRateDto>
}