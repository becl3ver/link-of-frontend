package com.example.logisticsestimate.data.comment

import com.google.gson.annotations.SerializedName

data class CommentResponseDto(
    @SerializedName("comments") val comments : ArrayList<CommentData>
    )