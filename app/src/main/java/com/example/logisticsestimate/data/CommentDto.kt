package com.example.logisticsestimate.data

import com.google.gson.annotations.SerializedName

data class CommentDto( // 토큰 필요
    @SerializedName("content") val content : String,
    @SerializedName("parentId") val parentId : Long,
    @SerializedName("is_nested") val isNested : Boolean
)
