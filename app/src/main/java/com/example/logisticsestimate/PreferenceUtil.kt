package com.example.logisticsestimate

import android.content.Context
import android.content.SharedPreferences

/**
 * SharedPreferences 객체를 통해서 토큰을 저장, 삭제, 수정할 수 있도록 한다.
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

    fun setUid(uid : Long) {
        prefs.edit().putLong("uid", uid).apply()
    }

    fun getUid(num : Long) : Long{
        return prefs.getLong("uid", num)
    }

    fun removeNickname() {
        prefs.edit().remove("nickname").apply()
    }

    fun setNickname(nickname : String) {
        prefs.edit().putString("nickname", nickname).apply()
    }

    fun getNickname(str : String) : String {
        return prefs.getString("nickname", str)!!
    }

    fun removeAccessToken() {
        prefs.edit().remove("access_token").apply()
    }

    fun setAccessToken(str : String?) {
        prefs.edit().putString("access_token", "Bearer $str").apply()
    }

    fun getAccessToken(str: String) : String {
        return prefs.getString("access_token", str).toString()
    }
}