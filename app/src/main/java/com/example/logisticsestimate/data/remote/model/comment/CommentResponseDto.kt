package com.example.logisticsestimate.data.remote.model.comment

import com.google.gson.annotations.SerializedName

/**
 * 조회된 댓글 목록
 */
data class CommentResponseDto(
    @SerializedName("comments") val comments: ArrayList<CommentData>
    )