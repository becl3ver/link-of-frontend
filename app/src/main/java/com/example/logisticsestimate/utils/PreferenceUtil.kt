package com.example.logisticsestimate.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * 토큰, 닉네임, ID, PW 등을 저장, 삭제, 수정할 수 있도록 한다.
 */
class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun removeString(key: String) {
        prefs.edit().remove(key).apply()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
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
}