package com.example.logisticsestimate.data.remote.model.board

import com.google.gson.annotations.SerializedName

/**
 * 조회된 글 목록
 */
data class BoardResponseDto(
    @SerializedName("category") var category: Int,
    @SerializedName("boards") var boards: ArrayList<BoardData>,
)