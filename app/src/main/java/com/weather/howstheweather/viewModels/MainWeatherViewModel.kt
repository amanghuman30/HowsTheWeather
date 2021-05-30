package com.weather.howstheweather.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.howstheweather.R
import com.weather.howstheweather.api.Resource
import com.weather.howstheweather.models.CurrentWeatherModel
import com.weather.howstheweather.repositories.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainWeatherViewModel @Inject constructor(val weatherRepository: WeatherRepository) : ViewModel() {

    val currentWeatherLiveData : MutableLiveData<Resource<CurrentWeatherModel>> = MutableLiveData()

    fun getCurrentWeather(lat : Double, lng: Double) {
        viewModelScope.launch {
            currentWeatherLiveData.postValue(Resource.Loading())
            val response = weatherRepository.getCurrentWeather(lat, lng)
            currentWeatherLiveData.postValue(handleCurrentWeatherResponse(response))
        }
    }

    fun handleCurrentWeatherResponse(response: Response<CurrentWeatherModel>) : Resource<CurrentWeatherModel> {
        if(response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(),null)
    }
}