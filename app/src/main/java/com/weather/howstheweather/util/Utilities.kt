package com.weather.howstheweather.util

import android.Manifest
import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat.getSystemService
import pub.devrel.easypermissions.EasyPermissions


object Utilities {
    fun hasLocationPermission(context: Context) =
            EasyPermissions.hasPermissions(context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

    // method to check
    // if location is enabled
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}