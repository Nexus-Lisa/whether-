package com.weatherapp.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

object WeatherIcons {
    
    fun getWeatherIcon(weatherCode: Int): ImageVector {
        return when {
            // Clear sky
            weatherCode == 0 -> Icons.Default.WbSunny
            // Mainly clear
            weatherCode == 1 -> Icons.Default.WbSunny
            // Partly cloudy
            weatherCode == 2 -> Icons.Default.CloudQueue
            // Overcast
            weatherCode == 3 -> Icons.Default.Cloud
            // Fog
            weatherCode in 45..48 -> Icons.Default.CloudQueue
            // Drizzle
            weatherCode in 51..57 -> Icons.Default.Grain
            // Rain
            weatherCode in 61..67 -> Icons.Default.Grain
            // Snow
            weatherCode in 71..77 -> Icons.Default.AcUnit
            // Showers
            weatherCode in 80..82 -> Icons.Default.Grain
            // Thunderstorm
            weatherCode in 95..99 -> Icons.Default.FlashOn
            else -> Icons.Default.WbSunny
        }
    }
    
    fun getWeatherIconOutlined(weatherCode: Int): ImageVector {
        return when {
            // Clear sky
            weatherCode == 0 -> Icons.Outlined.WbSunny
            // Mainly clear
            weatherCode == 1 -> Icons.Outlined.WbSunny
            // Partly cloudy
            weatherCode == 2 -> Icons.Outlined.CloudQueue
            // Overcast
            weatherCode == 3 -> Icons.Outlined.Cloud
            // Fog
            weatherCode in 45..48 -> Icons.Outlined.CloudQueue
            // Drizzle
            weatherCode in 51..57 -> Icons.Outlined.Grain
            // Rain
            weatherCode in 61..67 -> Icons.Outlined.Grain
            // Snow
            weatherCode in 71..77 -> Icons.Outlined.AcUnit
            // Showers
            weatherCode in 80..82 -> Icons.Outlined.Grain
            // Thunderstorm
            weatherCode in 95..99 -> Icons.Outlined.FlashOn
            else -> Icons.Outlined.WbSunny
        }
    }
    
    fun getWindDirectionIcon(degrees: Int): ImageVector {
        return when {
            degrees in 0..22 || degrees in 338..360 -> Icons.Default.East
            degrees in 23..67 -> Icons.Default.SouthEast
            degrees in 68..112 -> Icons.Default.South
            degrees in 113..157 -> Icons.Default.SouthWest
            degrees in 158..202 -> Icons.Default.West
            degrees in 203..247 -> Icons.Default.NorthWest
            degrees in 248..292 -> Icons.Default.North
            degrees in 293..337 -> Icons.Default.NorthEast
            else -> Icons.Default.East
        }
    }
}
