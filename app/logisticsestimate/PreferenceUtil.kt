package com.example.logisticsestimate

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun removeAccessToken() {
        prefs.edit().remove("access_token").apply()
    }

    fun setAccessToken(str : String?) {
        prefs.edit().putString("access_token", str).apply()
    }

    fun getAccessToken(str: String) : String {
        return prefs.getString("access_token", str).toString()
    }
}