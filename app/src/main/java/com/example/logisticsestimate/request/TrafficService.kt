package com.example.logisticsestimate.request

import com.example.logisticsestimate.data.TrafficResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrafficService {
    @GET("/")
    fun getShipCoordinateById(@Query("v") v : Int, @Query("shipid") shipid : Int, @Query("protocol") protocol : String) : Call<TrafficResponseDto>

    @GET("/")
    fun getShipCoordinateByImo(@Query("v") v : Int, @Query("imo") imo : Int, @Query("protocol") protocol : String) : Call<TrafficResponseDto>

    @GET("/")
    fun getShipCoordinateByMmsi(@Query("v") v : Int, @Query("mmsi") mmsi : Int, @Query("protocol") protocol : String) : Call<TrafficResponseDto>
}