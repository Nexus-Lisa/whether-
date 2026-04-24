package com.weatherapp.data.repository

import com.weatherapp.data.remote.OpenMeteoAirApi
import com.weatherapp.domain.model.AirQuality
import com.weatherapp.domain.repository.AirQualityRepository
import com.weatherapp.data.model.AirQualityResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AirQualityRepository @Inject constructor(
    private val api: OpenMeteoAirApi
) : AirQualityRepository {

    override suspend fun getCurrentAirQuality(lat: Double, lon: Double): AirQuality {
        val response = api.getAirQuality(lat, lon)
        val index = 0 // Current hour
        return AirQuality(
            aqi = response.hourly.usAqi.getOrNull(index) ?: 0,
            pm10 = response.hourly.pm10.getOrNull(index) ?: 0.0,
            pm25 = response.hourly.pm25.getOrNull(index) ?: 0.0,
            o3 = response.hourly.o3.getOrNull(index) ?: 0.0,
            no2 = response.hourly.no2.getOrNull(index) ?: 0.0,
            timestamp = System.currentTimeMillis()
        )
    }
}
