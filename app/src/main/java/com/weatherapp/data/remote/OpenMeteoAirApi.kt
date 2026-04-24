package com.weatherapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import com.weatherapp.data.model.AirQualityResponse

interface OpenMeteoAirApi {
    @GET("v1/air-quality")
    suspend fun getAirQuality(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "us_aqi,pm10,pm2_5,o3,no2",
        @Query("timezone") timezone: String = "auto"
    ): AirQualityResponse
}
