package com.example.logisticsestimate.data.remote.model.comment

/**
 * 특정 댓글의 대댓글 정보
 */
data class NestedCommentData(
    val nickname: String,
    val uid: Long,
    val date: String,
    val content: String,
    val id: Long
)