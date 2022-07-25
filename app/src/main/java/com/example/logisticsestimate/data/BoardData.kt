package com.example.logisticsestimate.data

import com.google.gson.annotations.SerializedName

data class BoardData(
    @SerializedName("category") var category: Int,
    @SerializedName("postId") var postId: Long,
    @SerializedName("uid") var uid: Long,
    @SerializedName("date") var date: String,

    @SerializedName("postTitle") var postTitle: String,
    @SerializedName("postContent") var postContent: String,
    @SerializedName("nickname") var nickname: String,
    )