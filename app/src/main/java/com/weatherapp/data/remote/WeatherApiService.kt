package com.weatherapp.data.remote

import com.weatherapp.data.model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current_weather") currentWeather: Boolean = true,
        @Query("hourly") hourly: String = "temperature_2m,relativehumidity_2m,precipitation_probability,rain,windspeed_10m,winddirection_10m,uv_index",
        @Query("daily") daily: String = "weathercode,temperature_2m_max,temperature_2m_min,precipitation_probability_max,windspeed_10m_max,winddirection_10m_dominant,sunrise,sunset,uv_index_max",
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse

    @GET("v1/air-quality")
    suspend fun getAirQuality(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = "us_aqi,pm10,pm2_5",
        @Query("timezone") timezone: String = "auto"
    ): AirQualityResponse

    @GET("v1/search")
    suspend fun searchCities(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "ru"
    ): GeocodingResponse
}

interface WeatherApiBackupService {
    @GET("v1/forecast.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 10,
        @Query("aqi") aqi: String = "yes"
    ): WeatherAPIResponse
}
