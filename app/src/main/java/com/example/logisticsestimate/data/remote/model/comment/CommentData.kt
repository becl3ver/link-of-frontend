package com.example.logisticsestimate.data.remote.model.comment

/**
 * 특정 게시물의 댓글 정보
 */
data class CommentData(
    val nickname: String,
    val uid: Long,
    val date: String,
    val content: String,
    val id: Long,
    val nestedComments: ArrayList<NestedCommentData>
)