package com.example.logisticsestimate.data.remote.api.service

import com.example.logisticsestimate.data.remote.model.estimate.EstimateRequestDto
import com.example.logisticsestimate.data.remote.model.estimate.EstimateResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 견적 조회 결과 통신
 */
interface EstimateService {
    @POST("/api/post-api/request")
    fun requestEstimateResult(
        @Body estimateRequestDto: EstimateRequestDto
    ): Call<EstimateResponseDto>
}