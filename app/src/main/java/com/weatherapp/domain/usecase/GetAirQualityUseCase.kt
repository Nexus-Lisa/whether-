package com.weatherapp.domain.usecase

import com.weatherapp.domain.model.AirQuality
import com.weatherapp.domain.model.Result
import com.weatherapp.domain.repository.AirQualityRepository
import javax.inject.Inject

class GetAirQualityUseCase @Inject constructor(
    private val airQualityRepository: AirQualityRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<AirQuality> {
        return try {
            val airQuality = airQualityRepository.getCurrentAirQuality(latitude, longitude)
            Result.Success(airQuality)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to get air quality")
        }
    }
}
