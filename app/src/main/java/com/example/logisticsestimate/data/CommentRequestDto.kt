package com.example.logisticsestimate.data

import com.google.gson.annotations.SerializedName

data class CommentRequestDto(
    @SerializedName("boardId") var boardId : Long
    )