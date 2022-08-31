package com.example.logisticsestimate.data.remote.model.login

import com.google.gson.annotations.SerializedName

/**
 * 인증코드를 수신받기 위한 사용자의 메일 주소
 */
data class EmailDto(
    @SerializedName("email") val email: String
)
