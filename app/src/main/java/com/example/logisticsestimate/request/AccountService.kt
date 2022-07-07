package com.example.logisticsestimate.request

import com.example.logisticsestimate.data.AccountSignInDto
import com.example.logisticsestimate.data.AccountRequestDto
import com.example.logisticsestimate.data.AccountResponseDto
import com.example.logisticsestimate.data.TokenDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// 토큰발급이 필요하면 /api, 필요 없으면 /
interface AccountService {
    @POST("/account/sign_up")
    fun getSignUp(@Body accountRequestDto: AccountRequestDto) : Call<AccountResponseDto>

    @POST("/account/authenticate")
    fun getSignIn(@Body accountSignInDto: AccountSignInDto) : Call<TokenDto>

    @POST("/account/withdraw") // 중복확인
    fun getWithdraw(@Body accountRequestDto: AccountRequestDto) : Call<AccountResponseDto>

    @POST("/auth/account/exist") // 탈퇴
    fun getExit(@Header("token") token: String, @Body accountSignInDto: AccountSignInDto) : Call<Boolean>

    @POST("/auth/account/reset") // 비밀번호 재설정
    fun getReset() // ID/PW 찾기는 본인 확인 질문으로 넣을 예정

    @POST("/auth/account/update")
    fun updateAccount(@Header("token") token: String, @Body accountRequestDto: AccountRequestDto) : Call<AccountResponseDto>
}