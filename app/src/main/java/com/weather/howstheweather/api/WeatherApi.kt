package com.weather.howstheweather.api

import com.weather.howstheweather.BuildConfig
import com.weather.howstheweather.models.CurrentWeatherModel
import com.weather.howstheweather.models.ForecastWeatherList
import com.weather.howstheweather.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat : String,
        @Query("lon") lng : String,
        @Query("appid") appId : String = BuildConfig.WEATHER_API_KEY,
        @Query("units") units : String = Constants.API_UNIT_METRIC
    ) : Response<CurrentWeatherModel>

    @GET("/data/2.5/forecast")
    suspend fun getWeatherForecastNextDays(
        @Query("lat") lat : String,
        @Query("lon") lng : String,
        @Query("appid") appId : String = BuildConfig.WEATHER_API_KEY,
        @Query("units") units : String = Constants.API_UNIT_METRIC
    ) : Response<ForecastWeatherList>
}