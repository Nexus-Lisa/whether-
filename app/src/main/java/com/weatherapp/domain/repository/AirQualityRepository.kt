package com.weatherapp.domain.repository

import com.weatherapp.domain.model.AirQuality

interface AirQualityRepository {
    suspend fun getCurrentAirQuality(lat: Double, lon: Double): AirQuality
}
