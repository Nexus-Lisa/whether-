package com.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val admin1: String?,
    val isCurrentLocation: Boolean = false,
    val addedTimestamp: Long = System.currentTimeMillis()
)
