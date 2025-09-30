package com.example.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(Date())
    }

    fun isDayTime(): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return hour in 6..18
    }

    fun getGreetingMessage(): String {
        return if (isDayTime()) {
            "Good Morning"
        } else {
            "Good Night"
        }
    }
}
