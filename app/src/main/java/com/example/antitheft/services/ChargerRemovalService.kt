package com.example.antitheft.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.example.antitheft.utils.NotificationHelper

class ChargerRemovalService: BaseService() {

    private val chargerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_POWER_DISCONNECTED) {
                playAlarm()
            }
        }
    }




    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForeground(1, NotificationHelper.createNotification(this, "Charger Removal Detection Started"))

        val filter = IntentFilter(Intent.ACTION_POWER_DISCONNECTED)
        registerReceiver(chargerReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(chargerReceiver)
    }
}