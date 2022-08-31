package com.example.logisticsestimate.data.remote.model.board

import com.google.gson.annotations.SerializedName

/**
 * 글에 포함된 이미지 요청
 */
data class BoardImageRequestDto(
    @SerializedName("boardId") val boardId: Long
)