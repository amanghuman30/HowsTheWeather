package com.weather.howstheweather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.*
import com.weather.howstheweather.R
import com.weather.howstheweather.api.Resource
import com.weather.howstheweather.models.CurrentWeatherModel
import com.weather.howstheweather.util.Constants
import com.weather.howstheweather.util.Utilities
import com.weather.howstheweather.viewModels.MainWeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_current.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class CurrentFragment : Fragment(R.layout.fragment_current), EasyPermissions.PermissionCallbacks{

    val weatherViewModel : MainWeatherViewModel by viewModels()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        requestPermissions()

        weatherViewModel.currentWeatherLiveData.observe(viewLifecycleOwner, { resource ->
            when(resource) {
                is Resource.Success -> {
                    progressBarCurrent.visibility = View.GONE
                    updateCurrentWeather(resource.data)
                }
                is Resource.Loading -> {
                    progressBarCurrent.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    progressBarCurrent.visibility = View.GONE
                    Toast.makeText(requireContext(),getString(R.string.weather_api_error), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun updateCurrentWeather(weather : CurrentWeatherModel?) {
        weather?.let {
            var temp = "${weather.main?.temp?.toString()}ºC"
            tvTemperature.text = temp

            var dayTemp = "${weather.main?.temp_max?.toString()}ºC"
            tvDay.text = dayTemp

            var nightTemp = "${weather.main?.temp_min?.toString()}ºC"
            tvNight.text = nightTemp

            var tempFeelsLike = "Feels Like ${weather.main?.feels_like?.toString()}ºC"
            tvTempFeelsLike.text = tempFeelsLike

            weather.weather?.let {
                if(it.size > 0) {
                    val description = it[0].description
                    tvCloudSunDescription.text = description
                    if(description!= null && description.contains("cloud")) {
                        ivCloudSun.setImageResource(R.drawable.ic_cloudy)
                    } else if(description != null && description.contains("wind")) {
                        ivCloudSun.setImageResource(R.drawable.ic_windy)
                    } else {
                        ivCloudSun.setImageResource(R.drawable.ic_day)
                    }
                }
            }

            tvLocation.text = weather.name

            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("EEE, MMM d");
            val date = dateFormat.format(calendar.time);
            tvDateTime.text = date
        }
    }

    private fun requestPermissions() {
        if(Utilities.hasLocationPermission(requireContext())) {
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
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
            if(!Utilities.isLocationEnabled(requireContext())) {
                Toast.makeText(requireContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(),"Retrieving location", Toast.LENGTH_SHORT).show()
            }
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