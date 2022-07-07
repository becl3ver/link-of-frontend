package com.example.logisticsestimate.data

import com.google.gson.annotations.SerializedName

data class CommentRequestDto(
    @SerializedName("board_id") var boardId : Long
    )