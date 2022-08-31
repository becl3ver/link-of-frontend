package com.example.logisticsestimate.data.remote.model.login

import com.google.gson.annotations.SerializedName

/**
 * 사용자가 패스워드를 망각했을 경우, 패스워드 갱신에 사용
 */
data class PasswordDto(
    @SerializedName("password") val password: String
)