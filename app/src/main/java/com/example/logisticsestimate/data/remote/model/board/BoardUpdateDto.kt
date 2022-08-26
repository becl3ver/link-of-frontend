package com.example.logisticsestimate.data.remote.model.board

import com.google.gson.annotations.SerializedName

/**
 * 특정 boardId에 해당하는 글 갱신
 */
data class BoardUpdateDto(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("category") val category: Int,
    @SerializedName("boardId") val boardId: Long
)