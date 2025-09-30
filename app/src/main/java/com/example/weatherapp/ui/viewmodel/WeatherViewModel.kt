package com.example.weatherapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.LocationManager
import com.example.weatherapp.utils.PreferencesManager
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WeatherRepository()
    private val preferencesManager = PreferencesManager(application)
    private val locationManager = LocationManager(application)

    private val _weatherData = MutableLiveData<WeatherResponse?>()
    val weatherData: LiveData<WeatherResponse?> = _weatherData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        // Load cached data on initialization
        loadCachedWeatherData()
    }

    private fun loadCachedWeatherData() {
        val cachedData = preferencesManager.getCachedWeatherData()
        if (cachedData != null) {
            _weatherData.value = cachedData
        } else {
            // If no cached data, try to get weather for last known location
            val lastLocation = preferencesManager.getLastLocation()
            lastLocation?.let { (lat, lon) ->
                fetchWeatherByLocation(lat, lon)
            }
        }
    }

    fun fetchCurrentLocationWeather() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val location = locationManager.getCurrentLocation()
                if (location != null) {
                    preferencesManager.saveLastLocation(location.latitude, location.longitude)
                    fetchWeatherByLocation(location.latitude, location.longitude)
                } else {
                    _isLoading.value = false
                    _errorMessage.value = "Unable to get current location"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Location error: ${e.message}"
            }
        }
    }

    fun fetchWeatherByLocation(latitude: Double, longitude: Double) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            repository.getCurrentWeatherByLocation(latitude, longitude)
                .onSuccess { weatherResponse ->
                    _weatherData.value = weatherResponse
                    preferencesManager.saveWeatherData(weatherResponse)
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _isLoading.value = false
                    _errorMessage.value = "Failed to fetch weather: ${exception.message}"
                }
        }
    }

    fun fetchWeatherByCity(cityName: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            repository.getCurrentWeatherByCity(cityName)
                .onSuccess { weatherResponse ->
                    _weatherData.value = weatherResponse
                    preferencesManager.saveWeatherData(weatherResponse)
                    preferencesManager.saveLastLocation(
                        weatherResponse.coord.lat,
                        weatherResponse.coord.lon
                    )
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _isLoading.value = false
                    _errorMessage.value = "Failed to fetch weather: ${exception.message}"
                }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
