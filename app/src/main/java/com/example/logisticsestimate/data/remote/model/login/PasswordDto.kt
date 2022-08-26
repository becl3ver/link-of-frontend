package com.example.logisticsestimate.data.remote.model.login

import com.google.gson.annotations.SerializedName

data class PasswordDto(
    @SerializedName("password") val password: String
)