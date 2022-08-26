package com.example.logisticsestimate.data.remote.api.service

import com.example.logisticsestimate.data.remote.model.traffic.TrafficResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * ID, IMO, MMSI 로 선박 조회
 */

interface TrafficService {
    @GET("/api/exportvessel/{api_key}/v:5/timespan:{timespan}/protocol:json/shipid:{shipid}")
    fun getShipCoordinateById(
        @Path(value = "api_key") apiKey: String,
        @Path("timespan") timespan: Int,
        @Path(value = "shipid") shipid: String
    ): Call<TrafficResponseDto>

    @GET("/api/exportvessel/{api_key}/v:5/timespan:{timespan}/protocol:json/imo:{imo}")
    fun getShipCoordinateByImo(
        @Path(value = "api_key") apiKey: String,
        @Path("timespan") timespan: Int,
        @Path(value = "imo") imo: String
    ): Call<TrafficResponseDto>

    @GET("/api/exportvessel/{api_key}/v:5/timespan:{timespan}/protocol:json/mmsi:{mmsi}")
    fun getShipCoordinateByMmsi(
        @Path(value = "api_key") apiKey: String,
        @Path("timespan") timespan: Int,
        @Path(value = "imo") imo: String
    ): Call<TrafficResponseDto>
}