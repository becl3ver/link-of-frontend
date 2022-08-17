package com.example.logisticsestimate.data.comment

import com.google.gson.annotations.SerializedName

data class CommentRequestDto(
    @SerializedName("boardId") var boardId : Long
    )