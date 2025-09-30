package com.example.weatherapp

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import com.example.weatherapp.utils.TimeUtils
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                getCurrentLocationWeather()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                getCurrentLocationWeather()
            }
            else -> {
                // No location access granted.
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupObservers()
        setupClickListeners()

        // Update time-based UI elements
        updateTimeBasedUI()
    }

    private fun setupUI() {
        // Set initial date and greeting
        binding.tvDate.text = TimeUtils.getCurrentDateTime()
        binding.tvGreeting.text = TimeUtils.getGreetingMessage()
    }

    private fun setupObservers() {
        weatherViewModel.weatherData.observe(this) { weatherResponse ->
            weatherResponse?.let {
                updateWeatherUI(it)
            }
        }

        weatherViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        weatherViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                weatherViewModel.clearError()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnCurrentLocation.setOnClickListener {
            checkLocationPermissionAndGetWeather()
        }

        binding.btnAddLocation.setOnClickListener {
            showAddLocationDialog()
        }
    }

    private fun checkLocationPermissionAndGetWeather() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted
                getCurrentLocationWeather()
            }
            else -> {
                // Request permission
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun getCurrentLocationWeather() {
        weatherViewModel.fetchCurrentLocationWeather()
    }

    private fun showAddLocationDialog() {
        val editText = EditText(this)
        editText.hint = "Enter city name"

        AlertDialog.Builder(this)
            .setTitle("Add Location")
            .setMessage("Enter the name of the city")
            .setView(editText)
            .setPositiveButton("Add") { _, _ ->
                val cityName = editText.text.toString().trim()
                if (cityName.isNotEmpty()) {
                    weatherViewModel.fetchWeatherByCity(cityName)
                } else {
                    Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateWeatherUI(weatherResponse: com.example.weatherapp.data.model.WeatherResponse) {
        with(binding) {
            // Update city name
            tvCityName.text = weatherResponse.name

            // Update date
            tvDate.text = TimeUtils.getCurrentDateTime()

            // Update temperature
            tvTemperature.text = "${weatherResponse.main.temp.roundToInt()}°C"

            // Update greeting
            tvGreeting.text = TimeUtils.getGreetingMessage()

            // Update weather details
            tvHumidity.text = "${weatherResponse.main.humidity}%"
            tvWind.text = "${weatherResponse.wind.speed.roundToInt()} km/h"
            tvFeelsLike.text = "${weatherResponse.main.feelsLike.roundToInt()}°C"

            // Update weather icon and background based on time
            updateTimeBasedUI()
        }
    }

    private fun updateTimeBasedUI() {
        val isDayTime = TimeUtils.isDayTime()

        with(binding) {
            if (isDayTime) {
                // Day time UI
                main.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.light_sky_blue))
                ivWeatherIcon.setImageResource(R.drawable.ic_sun)

                // Set all text colors to black for day time
                tvCityName.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_day))
                tvDate.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_day))
                tvTemperature.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_day))
                tvGreeting.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_day))
                tvHumidity.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_day))
                tvWind.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_day))
                tvFeelsLike.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_day))
                tvHumidityLabel.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_day))
                tvWindLabel.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_day))
                tvFeelsLikeLabel.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_day))
            } else {
                // Night time UI
                main.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.dark_grey))
                ivWeatherIcon.setImageResource(R.drawable.ic_moon)

                // Set all text colors to white for night time
                tvCityName.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_night))
                tvDate.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_night))
                tvTemperature.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_night))
                tvGreeting.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_night))
                tvHumidity.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_night))
                tvWind.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_night))
                tvFeelsLike.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_night))
                tvHumidityLabel.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_night))
                tvWindLabel.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_night))
                tvFeelsLikeLabel.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_color_night))
            }

            // Update greeting message
            tvGreeting.text = TimeUtils.getGreetingMessage()
        }
    }
}