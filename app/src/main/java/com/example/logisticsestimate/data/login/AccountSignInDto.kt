package com.example.logisticsestimate.data.login

import com.google.gson.annotations.SerializedName

data class AccountSignInDto(
    @SerializedName("loginId") var id : String,
    @SerializedName("password") var password : String
    )