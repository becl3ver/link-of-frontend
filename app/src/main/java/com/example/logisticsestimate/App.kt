package com.example.logisticsestimate

import android.app.Application
import android.content.Context

/**
 * PreferenceUtil 객체에 저장된 토큰을 전체에서 참조가 가능하도록 동반 객체로 가진다.
 * context 객체는 SSL 인증 과정에서 사용하기 위해 동반 객체로 가진다.
 */
class App : Application() {
    companion object {
        lateinit var prefs : PreferenceUtil
        lateinit var context : Context
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
        prefs = PreferenceUtil(applicationContext)
        //Security.insertProviderAt(Conscrypt.newProvider(), 1);
    }
}