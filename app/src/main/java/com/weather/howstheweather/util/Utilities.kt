package com.weather.howstheweather.util

import android.Manifest
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

object Utilities {
    fun hasLocationPermission(context: Context) =
            EasyPermissions.hasPermissions(context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
}