package com.example.logisticsestimate.data

import com.google.gson.annotations.SerializedName

// 회원가입 성공, 실패
data class AccountResponseDto(
    @SerializedName("result") var result: String
    )