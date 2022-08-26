package com.example.logisticsestimate.base

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.logisticsestimate.base.App

/**
 * 앱 강제 종료 시 토큰 삭제
 */
class TerminationService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        App.prefs.removeAccessToken()
        App.prefs.removeUid()
        App.prefs.removeNickname()
        stopSelf()
    }
}