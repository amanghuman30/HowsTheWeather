package com.weather.howstheweather.viewModels

import androidx.lifecycle.ViewModel
import com.weather.howstheweather.repositories.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(val weatherRepository: WeatherRepository) : ViewModel() {



}