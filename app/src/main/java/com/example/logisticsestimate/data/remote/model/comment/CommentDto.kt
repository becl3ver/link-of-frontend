package com.example.logisticsestimate.data.remote.model.comment

import com.google.gson.annotations.SerializedName

/**
 * 새로 작성되는 댓글
 * 1. 댓글인 경우 parentId는 게시글 ID
 * 2. 대댓글인 경우 parentId는 댓글 ID
 */
data class CommentDto(
    @SerializedName("content") val content: String,
    @SerializedName("parentId") val parentId: Long,
    @SerializedName("is_nested") val isNested: Boolean
)
