package co.kruzr.bernoulli.managers

import android.bluetooth.BluetoothAdapter
import android.content.Context
import co.kruzr.bernoulli.Settings
import co.kruzr.bernoulli.annotation.RequiresSetting

/**
 * A class to overlook settings state check related work.
 */
internal class SettingsManager constructor(private val context : Context){

    /**
     * Checks whether a given setting's state (enabled or disabled) is matching what is specified in a
     * RequiresSetting annotation.
     *
     * @param requiresSetting the Settings type to check
     * @return whether the setting's state (enabled or disabled) is matching the
     * settingsStateMismatchPolicy of the Setting.
     */
    fun isSettingsStateMatching(requiresSetting: RequiresSetting): Boolean {

        var isEnabled = false

        isEnabled = when (requiresSetting.setting) {

            Settings.GPS -> GPSManager(context).isGPSTurnedOn

            Settings.BLUETOOTH -> {
                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                bluetoothAdapter != null && bluetoothAdapter.isEnabled
            }

            Settings.AUTO_ROTATE -> android.provider.Settings.System.getInt(context.contentResolver,
                    android.provider.Settings.System.ACCELEROMETER_ROTATION, 0) == 1

            Settings.INTERNET_ANY -> NetworkManager(context).isInternetAvailable

            Settings.INTERNET_WIFI -> NetworkManager(context).isWifiAvailable

            Settings.INTERNET_MOBILE_DATA -> NetworkManager(context).isMobileInternetAvailable

            Settings.AIRPLANE_MODE -> android.provider.Settings.Global.getInt(context.contentResolver,
                    android.provider.Settings.Global.AIRPLANE_MODE_ON, 0) != 0
        }

        return requiresSetting.shouldBeEnabled == isEnabled
    }
}