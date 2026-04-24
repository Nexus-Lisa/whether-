package com.weatherapp.domain.usecase

import com.weatherapp.domain.model.Result
import com.weatherapp.domain.model.Weather
import com.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<Weather> {
        return try {
            val result = weatherRepository.getCurrentWeather(latitude, longitude)
            if (result is Result.Success) {
                weatherRepository.saveWeather(result.data)
            }
            result
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }
}
