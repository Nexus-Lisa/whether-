package com.weatherapp.domain.model

data class Weather(
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: Double,
    val weatherCode: Int,
    val windSpeed: Double,
    val windDirection: Int,
    val humidity: Int,
    val pressure: Double,
    val visibility: Double,
    val uvIndex: Double,
    val sunrise: String,
    val sunset: String,
    val timestamp: Long,
    val hourlyTime: List<String>,
    val hourlyTemperature: List<Double>,
    val hourlyPrecipitation: List<Int>,
    val dailyTime: List<String>,
    val dailyMaxTemp: List<Double>,
    val dailyMinTemp: List<Double>,
    val dailyWeatherCode: List<Int>,
    val dailyPrecipitation: List<Int>
)

data class AirQuality(
    val aqi: Int,
    val pm10: Double,
    val pm25: Double,
    val o3: Double,
    val no2: Double,
    val timestamp: Long
)

data class City(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val admin1: String?
)

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

data class WeatherCondition(
    val code: Int,
    val description: String,
    val icon: String
)

object WeatherCodes {
    private val weatherMap = mapOf(
        0 to "Clear sky",
        1 to "Mainly clear",
        2 to "Partly cloudy",
        3 to "Overcast",
        45 to "Fog",
        48 to "Fog",
        51 to "Light drizzle",
        53 to "Moderate drizzle",
        55 to "Dense drizzle",
        56 to "Light freezing drizzle",
        57 to "Dense freezing drizzle",
        61 to "Light rain",
        63 to "Moderate rain",
        65 to "Heavy rain",
        66 to "Light freezing rain",
        67 to "Heavy freezing rain",
        71 to "Light snow",
        73 to "Moderate snow",
        75 to "Heavy snow",
        77 to "Snow grains",
        80 to "Light showers",
        81 to "Moderate showers",
        82 to "Violent showers",
        85 to "Light snow showers",
        86 to "Heavy snow showers",
        95 to "Thunderstorm",
        96 to "Thunderstorm with hail",
        99 to "Thunderstorm with heavy hail"
    )

    fun getWeatherDescription(code: Int): String {
        return weatherMap[code] ?: "Unknown"
    }

    fun isClear(code: Int): Boolean = code in 0..1
    fun isCloudy(code: Int): Boolean = code in 2..3
    fun isFoggy(code: Int): Boolean = code in 45..48
    fun isRainy(code: Int): Boolean = code in 51..67 || code in 80..82
    fun isSnowy(code: Int): Boolean = code in 71..77 || code in 85..86
    fun isThunderstorm(code: Int): Boolean = code in 95..99
}

object AirQualityIndex {
    fun getAQICategory(aqi: Int): String {
        return when (aqi) {
            in 0..50 -> "Хорошо"
            in 51..100 -> "Умеренно"
            in 101..150 -> "Вредно для чувствительных групп"
            in 151..200 -> "Вредно"
            in 201..300 -> "Очень вредно"
            else -> "Опасно"
        }
    }

    fun getAQIColor(aqi: Int): String {
        return when (aqi) {
            in 0..50 -> "#4CAF50" // Зеленый
            in 51..100 -> "#FFC107" // Желтый
            in 101..150 -> "#FF9800" // Оранжевый
            in 151..200 -> "#F44336" // Красный
            in 201..300 -> "#9C27B0" // Фиолетовый
            else -> "#795548" // Бордовый
        }
    }

    fun getAQIRecommendation(aqi: Int): String {
        return when (aqi) {
            in 0..50 -> "Отличное время для прогулок"
            in 51..100 -> "Нормально, но чувствительным людям стоит быть осторожными"
            in 101..150 -> "Сократите длительное пребывание на улице"
            in 151..200 -> "Используйте маску, избегайте активностей на воздухе"
            in 201..300 -> "Оставайтесь дома, включите очиститель воздуха"
            else -> "Экстренные ситуации, немедленно эвакуация"
        }
    }
}
