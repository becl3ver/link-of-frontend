package com.example.logisticsestimate.data.board

import com.google.gson.annotations.SerializedName

data class BoardUpdateDto(
    @SerializedName("title") val title : String,
    @SerializedName("content") val content : String,
    @SerializedName("category") val category : Int,
    @SerializedName("boardId") val boardId : Long
)
