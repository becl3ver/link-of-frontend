package com.example.logisticsestimate.data

import com.google.gson.annotations.SerializedName

data class AccountRequestDto(
    @SerializedName("loginId") var id: String,
    @SerializedName("password") var password : String,
    @SerializedName("name") var name : String,
    @SerializedName("nickname") var nickname : String
    )