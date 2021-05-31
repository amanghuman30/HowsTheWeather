package com.weather.howstheweather.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.howstheweather.api.Resource
import com.weather.howstheweather.models.ForecastWeatherList
import com.weather.howstheweather.repositories.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(val weatherRepository: WeatherRepository) : ViewModel() {

    val forecastListLiveData : MutableLiveData<Resource<ForecastWeatherList>> = MutableLiveData()

    fun getForecastData(lat : Double, lng : Double) {
        forecastListLiveData.postValue(Resource.Loading())

        viewModelScope.launch {
            val response = weatherRepository.getNextWeekForecast(lat, lng)
            forecastListLiveData.postValue(handleForecastApiResult(response))
        }
    }

    fun handleForecastApiResult(response : Response<ForecastWeatherList>) : Resource<ForecastWeatherList> {
        if(response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }
}