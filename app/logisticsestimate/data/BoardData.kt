package com.example.logisticsestimate.data

import com.google.gson.annotations.SerializedName

data class BoardData(
    @SerializedName("category") var category: Int,
    @SerializedName("postId") var postId: Int,
    @SerializedName("uid") var uid: Int,
    @SerializedName("date") var date: String,

    @SerializedName("postTitle") var postTitle: String,
    @SerializedName("postContent") var postContent: String,
    @SerializedName("nickname") var nickname: String,
    )