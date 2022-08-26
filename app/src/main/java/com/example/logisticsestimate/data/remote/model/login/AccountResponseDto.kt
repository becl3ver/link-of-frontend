package com.example.logisticsestimate.data.remote.model.login

import com.google.gson.annotations.SerializedName

/**
 * 회원가입 결과
 */
data class AccountResponseDto(
    @SerializedName("result") var result: String
    )