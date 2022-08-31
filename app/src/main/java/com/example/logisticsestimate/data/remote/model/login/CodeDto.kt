package com.example.logisticsestimate.data.remote.model.login

import com.google.gson.annotations.SerializedName

/**
 * 이메일 인증 코드
 */
data class CodeDto(
    @SerializedName("code") val code : String
)
