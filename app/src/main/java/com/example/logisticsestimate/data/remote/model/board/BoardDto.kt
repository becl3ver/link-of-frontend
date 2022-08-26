package com.example.logisticsestimate.data.remote.model.board

import com.google.gson.annotations.SerializedName

/**
 * 새로운 게시글을 작성해 백엔드로 전송
 */
data class BoardDto(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("category") val category: Int,
    @SerializedName("image") val image: String?
)