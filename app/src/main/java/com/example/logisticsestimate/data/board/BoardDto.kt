package com.example.logisticsestimate.data.board

import com.google.gson.annotations.SerializedName

data class BoardDto(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("category") val category : Int
)