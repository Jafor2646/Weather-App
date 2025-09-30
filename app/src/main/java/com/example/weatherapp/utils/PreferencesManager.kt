package com.example.weatherapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.weatherapp.data.model.WeatherResponse
import com.google.gson.Gson

class PreferencesManager(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_CACHED_WEATHER = "cached_weather"
        private const val KEY_LAST_LOCATION = "last_location"
        private const val KEY_CACHE_TIMESTAMP = "cache_timestamp"
        private const val CACHE_VALIDITY_TIME = 10 * 60 * 1000L // 10 minutes
    }

    fun saveWeatherData(weatherResponse: WeatherResponse) {
        val weatherJson = gson.toJson(weatherResponse)
        preferences.edit()
            .putString(KEY_CACHED_WEATHER, weatherJson)
            .putLong(KEY_CACHE_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }

    fun getCachedWeatherData(): WeatherResponse? {
        val weatherJson = preferences.getString(KEY_CACHED_WEATHER, null)
        val timestamp = preferences.getLong(KEY_CACHE_TIMESTAMP, 0)

        return if (weatherJson != null && isCacheValid(timestamp)) {
            try {
                gson.fromJson(weatherJson, WeatherResponse::class.java)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    fun saveLastLocation(latitude: Double, longitude: Double) {
        preferences.edit()
            .putString(KEY_LAST_LOCATION, "$latitude,$longitude")
            .apply()
    }

    fun getLastLocation(): Pair<Double, Double>? {
        val location = preferences.getString(KEY_LAST_LOCATION, null)
        return location?.let {
            val parts = it.split(",")
            if (parts.size == 2) {
                try {
                    Pair(parts[0].toDouble(), parts[1].toDouble())
                } catch (e: Exception) {
                    null
                }
            } else null
        }
    }

    private fun isCacheValid(timestamp: Long): Boolean {
        return System.currentTimeMillis() - timestamp < CACHE_VALIDITY_TIME
    }

    fun clearCache() {
        preferences.edit()
            .remove(KEY_CACHED_WEATHER)
            .remove(KEY_CACHE_TIMESTAMP)
            .apply()
    }
}
