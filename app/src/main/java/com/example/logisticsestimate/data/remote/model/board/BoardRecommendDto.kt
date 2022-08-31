package com.example.logisticsestimate.data.remote.model.board

import com.google.gson.annotations.SerializedName

/**
 * 추천할 게시물 ID
 */
data class BoardRecommendDto(
    @SerializedName("boardId") val boardId: Long
)