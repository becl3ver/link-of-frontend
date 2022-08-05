package com.example.logisticsestimate.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BoardData(
    @SerializedName("category") var category: Int,
    @SerializedName("id") var id: Long,
    @SerializedName("uid") var uid: Long,
    @SerializedName("date") var date: String,

    @SerializedName("boardTitle") var boardTitle: String,
    @SerializedName("boardContent") var boardContent: String,
    @SerializedName("nickname") var nickname: String,
    ) : Serializable