package com.example.antitheft.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import com.example.antitheft.R
import com.example.antitheft.utils.APP_TAG

open class BaseService : Service() {

    private var isAlarmPlaying = false
    private var mediaPlayer: MediaPlayer? = null
    private var wakeLock: PowerManager.WakeLock? = null

    // Screen BroadcastReceiver
    private val screenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF -> stopAlarm()
                Intent.ACTION_USER_PRESENT -> stopAlarm()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::AlarmWakeLock")

        // Register the screen receiver
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_USER_PRESENT)
        }
        registerReceiver(screenReceiver, filter)
    }

    protected fun playAlarm() {
        if (!isAlarmPlaying) {
            isAlarmPlaying = true
            wakeLock?.acquire()

            val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

            // Initialize MediaPlayer with the system's default alarm sound
            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@BaseService, alarmUri)
                isLooping = true
                prepare() // Prepare MediaPlayer to play
                start()
            }
        }
    }


    protected fun stopAlarm() {
        if (isAlarmPlaying) {
            isAlarmPlaying = false

            // Stop and release MediaPlayer
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null

            // Release the wake lock
            wakeLock?.release()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenReceiver)
        stopAlarm()
    }


}
