package com.example.logisticsestimate.data

import com.google.gson.annotations.SerializedName

data class BoardDto( // 토큰 필요
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("category") val category : Int
)