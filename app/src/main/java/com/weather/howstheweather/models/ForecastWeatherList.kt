package com.weather.howstheweather.models

data class ForecastWeatherList(
    val city: City?,
    val cnt: Int?,
    val cod: String?,
    val list: List<WeatherOfTheDay>?,
    val message: Int?
)