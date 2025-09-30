package com.example.earbudsbattery

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * A simple broadcast receiver that's registered in the manifest to listen for
 * android.bluetooth.device.action.BATTERY_LEVEL_CHANGED when supported by devices.
 *
 * Not all earbuds or Android versions will broadcast battery levels. This app
 * attempts to display values when available.
 */
class BluetoothBatteryReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        val action = intent.action ?: return
        if (action == "android.bluetooth.device.action.BATTERY_LEVEL_CHANGED") {
            val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            val battery = intent.getIntExtra("android.bluetooth.device.extra.BATTERY_LEVEL", -1)
            Log.d("BTBatteryReceiver", "Device=${device?.address} name=${device?.name} level=$battery")
            // Nothing else here — MainActivity also registers receiver to update UI.
        }
    }
}
