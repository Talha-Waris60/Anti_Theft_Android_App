package com.example.antitheft.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.antitheft.databinding.ActivityMainBinding
import com.example.antitheft.services.ChargerRemovalService
import com.example.antitheft.services.MotionDetectionService
import com.example.antitheft.services.PocketRemovalService
import com.example.antitheft.utils.APP_TAG


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Log.d(APP_TAG, "Permission Granted")
            } else {
                Log.d(APP_TAG, "Permission Denied")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setUpListener()
    }

    private fun init() {
        requestNotificationPermission()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }
    }

    private fun setUpListener() {

        binding.buttonPocketRemoval.setOnClickListener {
            val intent = Intent(this, PocketRemovalService::class.java)
            startService(intent)
        }

        binding.buttonChargerRemoval.setOnClickListener {
            val intent = Intent(this, ChargerRemovalService::class.java)
            startService(intent)
        }

        binding.buttonMotionDetection.setOnClickListener {
            Log.d(APP_TAG, "buttonMotionDetection")
            val intent = Intent(this, MotionDetectionService::class.java)
            startService(intent)
        }
    }
}