package com.example.logisticsestimate.data.remote.model.comment

import com.google.gson.annotations.SerializedName

/**
 * 삭제하기 위한 댓글의 ID
 */
data class CommentDeleteDto(
    @SerializedName("commentId") val commentId: Long
)