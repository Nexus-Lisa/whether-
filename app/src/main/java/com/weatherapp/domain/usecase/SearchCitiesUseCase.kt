package com.weatherapp.domain.usecase

import com.weatherapp.domain.model.City
import com.weatherapp.domain.model.Result
import com.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class SearchCitiesUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(query: String): Result<List<City>> {
        return if (query.length >= 2) {
            weatherRepository.searchCities(query)
        } else {
            Result.Success(emptyList())
        }
    }
}
