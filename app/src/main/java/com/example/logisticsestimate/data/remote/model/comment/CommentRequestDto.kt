package com.example.logisticsestimate.data.remote.model.comment

import com.google.gson.annotations.SerializedName

/**
 * 댓글 목록 요청을 위한 게시글 ID
 */
data class CommentRequestDto(
    @SerializedName("boardId") var boardId: Long,
    )