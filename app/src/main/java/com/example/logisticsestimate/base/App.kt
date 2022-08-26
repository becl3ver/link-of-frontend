package com.example.logisticsestimate.base

import android.app.Application
import android.content.Context
import com.example.logisticsestimate.utils.PreferenceUtil

/**
 * 전역으로 사용할 Context, PreferenceUtil 초기화
 */
class App: Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
        prefs = PreferenceUtil(applicationContext)
    }
}