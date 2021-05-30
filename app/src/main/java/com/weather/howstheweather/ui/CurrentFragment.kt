package com.weather.howstheweather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.weather.howstheweather.R
import com.weather.howstheweather.util.Constants
import com.weather.howstheweather.util.Utilities
import com.weather.howstheweather.viewModels.MainWeatherViewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject


class CurrentFragment : Fragment(R.layout.fragment_current), EasyPermissions.PermissionCallbacks{

    val weatherViewModel : MainWeatherViewModel by viewModels()

    @Inject lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
    }

    private fun requestPermissions() {
        if(Utilities.hasLocationPermission(requireContext()))
            return

        EasyPermissions.requestPermissions(this,
            "Location Permission required for the app to function.",
            Constants.PERMISSION_REQUEST_CODE,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        requestLocationUpdates()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        if(Utilities.hasLocationPermission(requireContext())) {
            val request = LocationRequest.create().apply {
                interval = Constants.LOCATION_UPDATE_INTERVAL
                fastestInterval = Constants.FASTEST_LOCATION_INTERVAL
                priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            }
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private val locationCallback = object : LocationCallback (){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.locations?.let { locations ->
                for (location in locations) {
                    weatherViewModel.getCurrentWeather(location.latitude, location.longitude)
                }
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }
    }

}