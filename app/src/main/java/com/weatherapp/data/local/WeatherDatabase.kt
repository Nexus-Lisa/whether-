package com.weatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.weatherapp.data.local.converter.Converters
import com.weatherapp.data.local.entity.CityEntity
import com.weatherapp.data.local.entity.WeatherEntity

@Database(
    entities = [WeatherEntity::class, CityEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun cityDao(): CityDao
}
