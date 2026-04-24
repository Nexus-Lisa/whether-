package com.weatherapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "current_weather") val currentWeather: WeatherCurrentWeather,
    @Json(name = "hourly") val hourly: WeatherHourlyData,
    @Json(name = "daily") val daily: WeatherDailyData,
    @Json(name = "timezone") val timezone: String
)

@JsonClass(generateAdapter = true)
data class WeatherCurrentWeather(
    @Json(name = "temperature") val temperature: Double,
    @Json(name = "windspeed") val windSpeed: Double,
    @Json(name = "winddirection") val windDirection: Int,
    @Json(name = "weathercode") val weatherCode: Int,
    @Json(name = "time") val time: String
)

@JsonClass(generateAdapter = true)
data class WeatherHourlyData(
    @Json(name = "time") val time: List<String>,
    @Json(name = "temperature_2m") val temperature: List<Double>,
    @Json(name = "relativehumidity_2m") val humidity: List<Int>,
    @Json(name = "precipitation_probability") val precipitationProbability: List<Int>,
    @Json(name = "rain") val rain: List<Double>,
    @Json(name = "windspeed_10m") val windSpeed: List<Double>,
    @Json(name = "winddirection_10m") val windDirection: List<Int>,
    @Json(name = "uv_index") val uvIndex: List<Double>
)

@JsonClass(generateAdapter = true)
data class WeatherDailyData(
    @Json(name = "time") val time: List<String>,
    @Json(name = "weathercode") val weatherCode: List<Int>,
    @Json(name = "temperature_2m_max") val temperatureMax: List<Double>,
    @Json(name = "temperature_2m_min") val temperatureMin: List<Double>,
    @Json(name = "precipitation_probability_max") val precipitationProbabilityMax: List<Int>,
    @Json(name = "windspeed_10m_max") val windSpeedMax: List<Double>,
    @Json(name = "winddirection_10m_dominant") val windDirectionDominant: List<Int>,
    @Json(name = "sunrise") val sunrise: List<String>,
    @Json(name = "sunset") val sunset: List<String>,
    @Json(name = "uv_index_max") val uvIndexMax: List<Double>
)

@JsonClass(generateAdapter = true)
data class GeocodingResponse(
    val results: List<GeocodingResult>
)

@JsonClass(generateAdapter = true)
data class GeocodingResult(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val admin1: String?
)

@JsonClass(generateAdapter = true)
data class AirQualityResponse(
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "hourly") val hourly: AirQualityHourlyData
)

@JsonClass(generateAdapter = true)
data class AirQualityHourlyData(
    @Json(name = "time") val time: List<String>,
    @Json(name = "us_aqi") val usAqi: List<Int>,
    @Json(name = "pm10") val pm10: List<Double>,
    @Json(name = "pm2_5") val pm25: List<Double>,
    @Json(name = "o3") val o3: List<Double>,
    @Json(name = "no2") val no2: List<Double>
)
