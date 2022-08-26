package com.example.logisticsestimate.data.remote.model.board

import com.google.gson.annotations.SerializedName

data class BoardImageRequestDto(
    @SerializedName("boardId") val boardId: Long
)