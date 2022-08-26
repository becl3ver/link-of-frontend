package com.example.logisticsestimate.data.remote.model.board

import com.google.gson.annotations.SerializedName

/**
 * 특정한 게시글을 삭제하기 위해, 게시글 ID를 전송
 */
data class BoardDeleteDto(
    @SerializedName("boardId") val boardId: Long
)
