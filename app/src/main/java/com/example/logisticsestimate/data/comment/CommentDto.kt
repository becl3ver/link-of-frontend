package com.example.logisticsestimate.data.comment

import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("content") val content : String,
    @SerializedName("parentId") val parentId : Long?,
    @SerializedName("is_nested") val isNested : Boolean
)
