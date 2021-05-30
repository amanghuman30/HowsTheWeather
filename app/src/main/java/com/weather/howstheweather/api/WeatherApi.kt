package com.weather.howstheweather.api

import com.weather.howstheweather.BuildConfig
import com.weather.howstheweather.models.CurrentWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat : String,
        @Query("lng") lng : String,
        @Query("appid") appId : String = BuildConfig.WEATHER_API_KEY
    ) : Response<CurrentWeatherModel>

}