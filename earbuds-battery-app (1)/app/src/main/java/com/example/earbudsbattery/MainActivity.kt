package com.example.earbudsbattery

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private lateinit var leftTv: TextView
    private lateinit var rightTv: TextView
    private lateinit var caseTv: TextView
    private lateinit var statusTv: TextView

    private val requiredPermissions = mutableListOf<String>().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        add(Manifest.permission.ACCESS_FINE_LOCATION)
    }.toTypedArray()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        // no-op; UI will update when devices send broadcasts
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        leftTv = findViewById(R.id.leftBattery)
        rightTv = findViewById(R.id.rightBattery)
        caseTv = findViewById(R.id.caseBattery)
        statusTv = findViewById(R.id.status)

        if (!hasPermissions()) {
            requestPermissionLauncher.launch(requiredPermissions)
        }

        // Register the receiver programmatically too (in-case)
        val filter = IntentFilter("android.bluetooth.device.action.BATTERY_LEVEL_CHANGED")
        registerReceiver(batteryReceiver, filter)
    }

    private fun hasPermissions(): Boolean {
        return requiredPermissions.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            val action = intent.action
            if (action == "android.bluetooth.device.action.BATTERY_LEVEL_CHANGED") {
                // Some devices include device parcelable and battery level extra.
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                val battery = intent.getIntExtra("android.bluetooth.device.extra.BATTERY_LEVEL", -1)
                val name = device?.name ?: intent.getStringExtra("device_name") ?: "Unknown"

                runOnUiThread {
                    statusTv.text = "Device: $name"
                    if (battery >= 0) {
                        // Heuristic: many earbuds report separately. We show a generic value.
                        leftTv.text = "Left: $battery%"
                        rightTv.text = "Right: $battery%"
                        caseTv.text = "Case: $battery%"
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
    }
}
