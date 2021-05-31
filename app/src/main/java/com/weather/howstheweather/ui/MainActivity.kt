package com.weather.howstheweather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.*
import com.weather.howstheweather.R
import com.weather.howstheweather.util.Constants
import com.weather.howstheweather.util.Utilities
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var userLocationLiveData : MutableLiveData<Location> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.my_weather_toolbar_title)

        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        requestPermissions()
    }

    private fun requestPermissions() {
        if(Utilities.hasLocationPermission(this)) {
            requestLocationUpdates()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Location Permission required for the app to function.",
                Constants.PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
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
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        if(Utilities.hasLocationPermission(this)) {
            val request = LocationRequest.create().apply {
                interval = Constants.LOCATION_UPDATE_INTERVAL
                fastestInterval = Constants.FASTEST_LOCATION_INTERVAL
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
            if(!Utilities.isLocationEnabled(this)) {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            } else {
                Toast.makeText(this,"Retrieving location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.locations?.let { locations ->
                for (location in locations) {
                    userLocationLiveData.postValue(location)
                }
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }
    }

}