package com.example.logisticsestimate.data.remote.model.board

import com.google.gson.annotations.SerializedName

data class BoardRecommendDto(
    @SerializedName("boardId") val boardId: Long
)