package com.weatherapp.domain.repository

import com.weatherapp.domain.model.*
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): Result<Weather>
    suspend fun getAirQuality(latitude: Double, longitude: Double): Result<AirQuality>
    suspend fun searchCities(query: String): Result<List<City>>
    suspend fun saveWeather(weather: Weather)
    suspend fun getCachedWeather(cityName: String): Weather?
    fun getAllCities(): Flow<List<City>>
    suspend fun saveCity(city: City, isCurrentLocation: Boolean = false)
    suspend fun deleteCity(cityName: String)
}
