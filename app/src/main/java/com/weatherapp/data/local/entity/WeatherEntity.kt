package com.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: Double,
    val weatherCode: Int,
    val windSpeed: Double,
    val windDirection: Int,
    val humidity: Int,
    val pressure: Double,
    val visibility: Double,
    val uvIndex: Double,
    val sunrise: String,
    val sunset: String,
    val timestamp: Long,
    val hourlyTime: List<String>,
    val hourlyTemperature: List<Double>,
    val hourlyPrecipitation: List<Int>,
    val dailyTime: List<String>,
    val dailyMaxTemp: List<Double>,
    val dailyMinTemp: List<Double>,
    val dailyWeatherCode: List<Int>,
    val dailyPrecipitation: List<Int>
)
