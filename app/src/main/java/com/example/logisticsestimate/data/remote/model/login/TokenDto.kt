package com.example.logisticsestimate.data.remote.model.login

/**
 * 로그인 결과
 */
data class TokenDto(
    val uid: Long,
    val nickname: String,
    val token: String
    )