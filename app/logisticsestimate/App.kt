package com.example.logisticsestimate

import android.app.Application
import android.content.Context

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