package com.example.antitheft.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import com.example.antitheft.utils.NotificationHelper

class PocketRemovalService : BaseService() {

    private lateinit var proximitySensor: Sensor
    private lateinit var sensorManager: SensorManager

    private val proximityListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null && event.values[0] != proximitySensor.maximumRange) {
                playAlarm()
            }
        }


        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }




    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForeground(3, NotificationHelper.createNotification(this, "Pocket Removal Detection Started"))
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)!!
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(proximityListener)
        stopAlarm()
    }
}