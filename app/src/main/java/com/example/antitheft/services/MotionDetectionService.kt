package com.example.antitheft.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import com.example.antitheft.utils.APP_TAG
import com.example.antitheft.utils.NotificationHelper

class MotionDetectionService: BaseService()  {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    private val motionListener = object : SensorEventListener {
        private var lastX = 0f
        private var lastY = 0f
        private var lastZ = 0f

        override fun onSensorChanged(event: SensorEvent?) {
            val deltaX = Math.abs(lastX - event!!.values[0])
            val deltaY = Math.abs(lastY - event.values[1])
            val deltaZ = Math.abs(lastZ - event.values[2])

            Log.d(APP_TAG, "DeltaX: $deltaX, DeltaY: $deltaY, DeltaZ: $deltaZ")

            if (deltaX > 2 || deltaY > 2 || deltaZ > 2) {
                playAlarm()
            }

            lastX = event.values[0]
            lastY = event.values[1]
            lastZ = event.values[2]
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(APP_TAG, "Service started")
        startForeground(2, NotificationHelper.createNotification(this, "Motion Detection Started"))
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        sensorManager.registerListener(motionListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(motionListener)
        stopAlarm()
    }
}