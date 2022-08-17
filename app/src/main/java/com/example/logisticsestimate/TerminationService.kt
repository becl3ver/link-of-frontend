package com.example.logisticsestimate

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class TerminationService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        App.prefs.removeAccessToken()
        App.prefs.removeUid()
        App.prefs.removeNickname()
        Toast.makeText(this@TerminationService, "termination test success", Toast.LENGTH_SHORT).show()
        stopSelf()
    }
}