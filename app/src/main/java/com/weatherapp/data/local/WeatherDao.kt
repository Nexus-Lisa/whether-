package com.weatherapp.data.local

import androidx.room.*
import com.weatherapp.data.local.entity.WeatherEntity
import com.weatherapp.data.local.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE cityName = :cityName")
    suspend fun getWeatherByCity(cityName: String): WeatherEntity?

    @Query("SELECT * FROM weather ORDER BY timestamp DESC")
    fun getAllWeather(): Flow<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("DELETE FROM weather WHERE cityName = :cityName")
    suspend fun deleteWeather(cityName: String)

    @Query("DELETE FROM weather WHERE timestamp < :timestamp")
    suspend fun deleteOldWeather(timestamp: Long)
}

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY isCurrentLocation DESC, addedTimestamp ASC")
    fun getAllCities(): Flow<List<CityEntity>>

    @Query("SELECT * FROM cities WHERE isCurrentLocation = 1 LIMIT 1")
    suspend fun getCurrentLocationCity(): CityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    @Query("UPDATE cities SET isCurrentLocation = 0 WHERE isCurrentLocation = 1")
    suspend fun clearCurrentLocation()

    @Query("DELETE FROM cities WHERE name = :name")
    suspend fun deleteCity(name: String)

    @Query("SELECT * FROM cities WHERE name LIKE '%' || :query || '%' OR country LIKE '%' || :query || '%'")
    suspend fun searchCities(query: String): List<CityEntity>
}
