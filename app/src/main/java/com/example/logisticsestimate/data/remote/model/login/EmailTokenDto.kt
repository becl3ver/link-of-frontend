package com.example.logisticsestimate.data.remote.model.login

import com.google.gson.annotations.SerializedName

/**
 * 이메일 인증을 통해서 발급받는 JToken
 */
data class EmailTokenDto(
    @SerializedName("token") val token: String
)