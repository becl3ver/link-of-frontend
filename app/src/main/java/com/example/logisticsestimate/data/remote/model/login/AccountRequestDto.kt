package com.example.logisticsestimate.data.remote.model.login

import com.google.gson.annotations.SerializedName

/**
 * 회원가입 정보
 */
data class AccountRequestDto(
    @SerializedName("loginId") var id: String?,
    @SerializedName("name") var name: String?,
    @SerializedName("password") var password: String?,
    @SerializedName("nickname") var nickname: String?
    )