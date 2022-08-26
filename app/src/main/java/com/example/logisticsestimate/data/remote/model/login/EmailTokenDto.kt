package com.example.logisticsestimate.data.remote.model.login

import com.google.gson.annotations.SerializedName

data class EmailTokenDto(
    @SerializedName("token") val token: String
)