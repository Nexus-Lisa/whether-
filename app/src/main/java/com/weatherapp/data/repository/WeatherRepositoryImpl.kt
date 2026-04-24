package com.weatherapp.data.repository

import com.weatherapp.data.local.WeatherDao
import com.weatherapp.data.local.entity.WeatherEntity
import com.weatherapp.data.local.entity.CityEntity
import com.weatherapp.data.remote.WeatherApiService
import com.weatherapp.data.remote.WeatherApiBackupService
import com.weatherapp.domain.model.Weather
import com.weatherapp.domain.model.AirQuality
import com.weatherapp.domain.model.City
import com.weatherapp.domain.repository.WeatherRepository
import com.weatherapp.domain.model.Result
import com.weatherapp.data.model.AirQualityResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val weatherApiBackupService: WeatherApiBackupService,
    private val weatherDao: WeatherDao
) : WeatherRepository {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): Result<Weather> {
        return try {
            val response = weatherApiService.getCurrentWeather(latitude, longitude)
            val weather = mapToWeather(response)
            Result.Success(weather)
        } catch (e: Exception) {
            // Try backup API
            try {
                val location = "$latitude,$longitude"
                val backupResponse = weatherApiBackupService.getCurrentWeather(
                    apiKey = com.weatherapp.BuildConfig.WEATHERAPI_KEY,
                    location = location
                )
                val weather = mapBackupToWeather(backupResponse)
                Result.Success(weather)
            } catch (backupException: Exception) {
                Result.Error(backupException.message ?: "Unknown error")
            }
        }
    }

    override suspend fun getAirQuality(latitude: Double, longitude: Double): Result<AirQuality> {
        return try {
            val response = weatherApiService.getAirQuality(latitude, longitude)
            val airQuality = mapToAirQuality(response)
            Result.Success(airQuality)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun searchCities(query: String): Result<List<City>> {
        return try {
            val response = weatherApiService.searchCities(query)
            val cities = response.results.map { mapToCity(it) }
            Result.Success(cities)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun saveWeather(weather: Weather) {
        val entity = mapToWeatherEntity(weather)
        weatherDao.insertWeather(entity)
    }

    override suspend fun getCachedWeather(cityName: String): Weather? {
        return weatherDao.getWeatherByCity(cityName)?.let { mapToWeather(it) }
    }

    override fun getAllCities(): Flow<List<City>> {
        return flowOf(emptyList()) // Simplified for now
    }

    override suspend fun saveCity(city: City, isCurrentLocation: Boolean) {
        // Simplified - no city storage for now
    }

    override suspend fun deleteCity(cityName: String) {
        weatherDao.deleteWeather(cityName)
    }

    private fun mapToWeather(response: com.weatherapp.data.model.WeatherResponse): Weather {
        val currentHour = response.hourly.time.indexOfFirst { 
            it.startsWith(response.currentWeather.time.substring(0, 13)) 
        }.coerceAtLeast(0)

        return Weather(
            cityName = "Unknown", // Will be set by caller
            latitude = response.latitude,
            longitude = response.longitude,
            temperature = response.currentWeather.temperature,
            weatherCode = response.currentWeather.weatherCode,
            windSpeed = response.currentWeather.windSpeed,
            windDirection = response.currentWeather.windDirection,
            humidity = response.hourly.humidity.getOrNull(currentHour) ?: 0,
            pressure = 1013.25, // Not available in Open-Meteo current
            visibility = 10.0, // Not available in Open-Meteo current
            uvIndex = response.hourly.uvIndex.getOrNull(currentHour) ?: 0.0,
            sunrise = response.daily.sunrise.firstOrNull() ?: "",
            sunset = response.daily.sunset.firstOrNull() ?: "",
            timestamp = System.currentTimeMillis(),
            hourlyTime = response.hourly.time,
            hourlyTemperature = response.hourly.temperature,
            hourlyPrecipitation = response.hourly.precipitationProbability,
            dailyTime = response.daily.time,
            dailyMaxTemp = response.daily.temperatureMax,
            dailyMinTemp = response.daily.temperatureMin,
            dailyWeatherCode = response.daily.weatherCode,
            dailyPrecipitation = response.daily.precipitationProbabilityMax
        )
    }

    private fun mapBackupToWeather(response: com.weatherapp.data.model.WeatherAPIResponse): Weather {
        return Weather(
            cityName = response.location.name,
            latitude = response.location.lat,
            longitude = response.location.lon,
            temperature = response.current.tempC,
            weatherCode = response.current.condition.code,
            windSpeed = response.current.windKph,
            windDirection = response.current.windDegree,
            humidity = response.current.humidity,
            pressure = response.current.pressureMb,
            visibility = response.current.visKm,
            uvIndex = response.current.uv,
            sunrise = "", // Not available in this format
            sunset = "", // Not available in this format
            timestamp = System.currentTimeMillis(),
            hourlyTime = response.forecast.forecastDay.flatMap { day ->
                day.hour.map { it.time }
            },
            hourlyTemperature = response.forecast.forecastDay.flatMap { day ->
                day.hour.map { it.tempC }
            },
            hourlyPrecipitation = response.forecast.forecastDay.flatMap { day ->
                day.hour.map { it.chanceOfRain }
            },
            dailyTime = response.forecast.forecastDay.map { it.date },
            dailyMaxTemp = response.forecast.forecastDay.map { it.day.maxTempC },
            dailyMinTemp = response.forecast.forecastDay.map { it.day.minTempC },
            dailyWeatherCode = response.forecast.forecastDay.map { it.day.condition.code },
            dailyPrecipitation = response.forecast.forecastDay.map { it.day.dailyChanceOfRain }
        )
    }

    private fun mapToAirQuality(response: com.weatherapp.data.model.AirQualityResponse): AirQuality {
        val currentIndex = response.hourly.usAqi.indexOfFirst { it > 0 }.coerceAtLeast(0)
        return AirQuality(
            aqi = response.hourly.usAqi.getOrNull(currentIndex) ?: 0,
            pm10 = response.hourly.pm10.getOrNull(currentIndex) ?: 0.0,
            pm25 = response.hourly.pm25.getOrNull(currentIndex) ?: 0.0,
            o3 = response.hourly.o3.getOrNull(currentIndex) ?: 0.0,
            no2 = response.hourly.no2.getOrNull(currentIndex) ?: 0.0,
            timestamp = System.currentTimeMillis()
        )
    }

    private fun mapToCity(result: com.weatherapp.data.model.GeocodingResult): City {
        return City(
            name = result.name,
            latitude = result.latitude,
            longitude = result.longitude,
            country = result.country,
            admin1 = result.admin1
        )
    }

    private fun mapToWeatherEntity(weather: Weather): WeatherEntity {
        return WeatherEntity(
            cityName = weather.cityName,
            latitude = weather.latitude,
            longitude = weather.longitude,
            temperature = weather.temperature,
            weatherCode = weather.weatherCode,
            windSpeed = weather.windSpeed,
            windDirection = weather.windDirection,
            humidity = weather.humidity,
            pressure = weather.pressure,
            visibility = weather.visibility,
            uvIndex = weather.uvIndex,
            sunrise = weather.sunrise,
            sunset = weather.sunset,
            timestamp = weather.timestamp,
            hourlyTime = weather.hourlyTime,
            hourlyTemperature = weather.hourlyTemperature,
            hourlyPrecipitation = weather.hourlyPrecipitation,
            dailyTime = weather.dailyTime,
            dailyMaxTemp = weather.dailyMaxTemp,
            dailyMinTemp = weather.dailyMinTemp,
            dailyWeatherCode = weather.dailyWeatherCode,
            dailyPrecipitation = weather.dailyPrecipitation
        )
    }

    private fun mapToWeather(entity: WeatherEntity): Weather {
        return Weather(
            cityName = entity.cityName,
            latitude = entity.latitude,
            longitude = entity.longitude,
            temperature = entity.temperature,
            weatherCode = entity.weatherCode,
            windSpeed = entity.windSpeed,
            windDirection = entity.windDirection,
            humidity = entity.humidity,
            pressure = entity.pressure,
            visibility = entity.visibility,
            uvIndex = entity.uvIndex,
            sunrise = entity.sunrise,
            sunset = entity.sunset,
            timestamp = entity.timestamp,
            hourlyTime = entity.hourlyTime,
            hourlyTemperature = entity.hourlyTemperature,
            hourlyPrecipitation = entity.hourlyPrecipitation,
            dailyTime = entity.dailyTime,
            dailyMaxTemp = entity.dailyMaxTemp,
            dailyMinTemp = entity.dailyMinTemp,
            dailyWeatherCode = entity.dailyWeatherCode,
            dailyPrecipitation = entity.dailyPrecipitation
        )
    }

    private fun mapToCity(entity: CityEntity): City {
        return City(
            name = entity.name,
            latitude = entity.latitude,
            longitude = entity.longitude,
            country = entity.country,
            admin1 = entity.admin1
        )
    }
}
