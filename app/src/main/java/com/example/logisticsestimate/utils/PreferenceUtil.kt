package com.example.logisticsestimate.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.logisticsestimate.data.remote.model.login.TokenDto

/**
 * 토큰, 닉네임, ID, PW 등을 저장, 삭제, 수정할 수 있도록 한다.
 */
class PreferenceUtil(context: Context) {
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        "android",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun removeString(key: String) {
        prefs.edit().remove(key).apply()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun removeAccessToken() {
        prefs.edit().remove("accessToken").apply()
    }

    fun setAccessToken(str: String?) {
        prefs.edit().putString("accessToken", "Bearer $str").apply()
    }

    fun getAccessToken(): String {
        return prefs.getString("accessToken", "")!!
    }

    fun getAccessToken(str: String): String {
        return prefs.getString("accessToken", str)!!
    }

    fun removeUid() {
        prefs.edit().remove("uid").apply()
    }

    fun setUid(uid: Long) {
        prefs.edit().putLong("uid", uid).apply()
    }

    fun getUid(): Long {
        return prefs.getLong("uid", -1)
    }

    fun removeNickname() {
        prefs.edit().remove("nickname").apply()
    }

    fun setNickname(nickname: String) {
        prefs.edit().putString("nickname", nickname).apply()
    }

    fun getNickname(): String? {
        return prefs.getString("nickname", null)
    }

    fun removeLoginId() {
        prefs.edit().remove("loginId").apply()
    }

    fun setLoginId(loginId: String) {
        prefs.edit().putString("loginId", loginId).apply()
    }

    fun getLoginId(): String {
        return prefs.getString("loginId", "")!!
    }

    fun removeName() {
        prefs.edit().remove("name").apply()
    }

    fun setName(name: String) {
        prefs.edit().putString("name", name).apply()
    }

    fun getName(): String {
        return prefs.getString("name", "")!!
    }

    fun removeEmail() {
        prefs.edit().remove("email").apply()
    }

    fun setEmail(email: String) {
        prefs.edit().putString("email", email).apply()
    }

    fun getEmail(): String {
        return prefs.getString("email", "")!!
    }

    fun setLoginData(tokenDto: TokenDto) {
        setUid(tokenDto.uid)
        setLoginId(tokenDto.id)
        setNickname(tokenDto.nickname)
        setEmail(tokenDto.email)
        setAccessToken(tokenDto.token)
        setName(tokenDto.name)
    }

    fun removeLoginData() {
        removeUid()
        removeLoginId()
        removeNickname()
        removeEmail()
        removeAccessToken()
        removeName()
    }
}