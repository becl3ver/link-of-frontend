package com.example.logisticsestimate.data.board

import com.google.gson.annotations.SerializedName

data class BoardDeleteDto(
    @SerializedName("boardId") val boardId : Long
)
