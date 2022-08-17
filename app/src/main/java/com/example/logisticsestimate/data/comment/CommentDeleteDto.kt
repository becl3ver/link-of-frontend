package com.example.logisticsestimate.data.comment

import com.google.gson.annotations.SerializedName

data class CommentDeleteDto(
    @SerializedName("commentId") val commentId : Long
)