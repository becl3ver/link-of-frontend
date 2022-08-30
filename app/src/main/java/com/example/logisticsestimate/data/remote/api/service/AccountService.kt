package com.example.logisticsestimate.data.remote.api.service

import com.example.logisticsestimate.data.remote.model.login.*
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * 회원 정보 통신
 */

interface AccountService {
    @POST("/auth/account/sign_up")
    fun signUp(@Header("Authorization") token: String, @Body accountRequestDto: AccountRequestDto): Call<AccountResponseDto>

    @POST("/account/authenticate")
    fun signIn(@Body accountSignInDto: AccountSignInDto): Call<TokenDto>

    @POST("/account/duplication")
    fun checkDuplication(@Body accountRequestDto: AccountRequestDto): Call<AccountResponseDto>

    @POST("/auth/account/withdrawal")
    fun withdrawal(@Header("Authorization") token: String, @Body accountSignInDto: AccountSignInDto): Call<Boolean>

    @POST("/auth/account/update")
    fun updateAccount(@Header("Authorization") token: String, @Body accountRequestDto: AccountRequestDto): Call<AccountResponseDto>

    @POST("/account/email")
    fun checkEmail(@Body emailDto: EmailDto): Call<EmailTokenDto>

    @POST("/email-auth/account/email/code")
    fun checkEmailCode(@Header("Authorization") token: String, @Body codeDto: CodeDto): Call<EmailTokenDto>

    @POST("/account/password/email")
    fun identifyByEmail(@Body emailDto: EmailDto): Call<EmailTokenDto>

    @POST("/email-auth/account/password/email/code")
    fun identificationCode(@Header("Authorization") token: String, @Body codeDto: CodeDto): Call<EmailTokenDto>

    @POST("/auth/password/reset")
    fun passwordReset(@Header("Authorization") token: String, @Body passwordDto: PasswordDto): Call<String>

}