package com.example.logisticsestimate.data.board

import com.google.gson.annotations.SerializedName

data class BoardRequestDto(
    @SerializedName("keyword") var keyword: String?,
    @SerializedName("range") var range: Long?,

    @SerializedName("category") var category: Int?,
    @SerializedName("size") var size: Int?,
    @SerializedName("list") var list: ArrayList<Long>?
    )