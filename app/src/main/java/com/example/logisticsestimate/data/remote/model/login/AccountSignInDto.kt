package com.example.logisticsestimate.data.remote.model.login

import com.google.gson.annotations.SerializedName

/**
 * 로그인 정보
 */
data class AccountSignInDto(
    @SerializedName("loginId") var id: String,
    @SerializedName("password") var password: String
    )