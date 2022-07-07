package com.example.logisticsestimate.data

import com.google.gson.annotations.SerializedName

data class CommentResponseDto(
    @SerializedName("comments") var comments : ArrayList<CommentData>
    )