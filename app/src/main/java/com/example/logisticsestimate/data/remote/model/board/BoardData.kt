package com.example.logisticsestimate.data.remote.model.board

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 개별 게시글 정보를 수신
 */
data class BoardData(
    @SerializedName("category") var category: Int,
    @SerializedName("id") var id: Long,
    @SerializedName("uid") var uid: Long,
    @SerializedName("date") var date: String,
    @SerializedName("recommend") var recommend: Int,
    @SerializedName("hasImage") var hasImage: Boolean,

    @SerializedName("boardTitle") var boardTitle: String,
    @SerializedName("boardContent") var boardContent: String,
    @SerializedName("nickname") var nickname: String,
) : Serializable