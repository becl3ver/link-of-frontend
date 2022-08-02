package com.example.logisticsestimate.data

data class CommentData(
    val nickname: String,
    val uid : Long,
    val date : String,
    val content : String,
    val id : Long,
    val nestedComments : ArrayList<NestedCommentData>
)