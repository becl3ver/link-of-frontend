package com.example.logisticsestimate.data.remote.model.comment

/**
 * 수신한 댓글, 대댓글을 저장
 */
data class Comment(
    val type: Int,
    val content: String?,
    val date: String?,
    val nickname: String?,
    val commentId: Long?,
    val uid: Long?,
    val parentUid: Long?,
    val parentCommentId: Long?
)