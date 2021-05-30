package com.weather.howstheweather.di

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.weather.howstheweather.repositories.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherRepository() = WeatherRepository()
}