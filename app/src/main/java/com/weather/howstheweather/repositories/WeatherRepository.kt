package com.weather.howstheweather.repositories

import com.weather.howstheweather.api.ApiBuilder

class WeatherRepository {

    suspend fun getCurrentWeather( lat : Double, lng : Double) =
        ApiBuilder.weatherApi.getCurrentWeather(lat.toString(), lng.toString())

}