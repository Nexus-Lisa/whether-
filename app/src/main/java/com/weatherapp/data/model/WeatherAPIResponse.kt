package com.weatherapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherAPIResponse(
    @Json(name = "location") val location: Location,
    @Json(name = "current") val current: Current,
    @Json(name = "forecast") val forecast: Forecast
)

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "name") val name: String,
    @Json(name = "region") val region: String,
    @Json(name = "country") val country: String,
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double,
    @Json(name = "tz_id") val timezone: String
)

@JsonClass(generateAdapter = true)
data class Current(
    @Json(name = "temp_c") val tempC: Double,
    @Json(name = "temp_f") val tempF: Double,
    @Json(name = "condition") val condition: Condition,
    @Json(name = "wind_mph") val windMph: Double,
    @Json(name = "wind_kph") val windKph: Double,
    @Json(name = "wind_degree") val windDegree: Int,
    @Json(name = "wind_dir") val windDir: String,
    @Json(name = "pressure_mb") val pressureMb: Double,
    @Json(name = "pressure_in") val pressureIn: Double,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "cloud") val cloud: Int,
    @Json(name = "feelslike_c") val feelsLikeC: Double,
    @Json(name = "feelslike_f") val feelsLikeF: Double,
    @Json(name = "vis_km") val visKm: Double,
    @Json(name = "vis_miles") val visMiles: Double,
    @Json(name = "uv") val uv: Double
)

@JsonClass(generateAdapter = true)
data class Condition(
    @Json(name = "text") val text: String,
    @Json(name = "icon") val icon: String,
    @Json(name = "code") val code: Int
)

@JsonClass(generateAdapter = true)
data class Forecast(
    @Json(name = "forecastday") val forecastDay: List<ForecastDay>
)

@JsonClass(generateAdapter = true)
data class ForecastDay(
    @Json(name = "date") val date: String,
    @Json(name = "day") val day: Day,
    @Json(name = "hour") val hour: List<Hour>
)

@JsonClass(generateAdapter = true)
data class Day(
    @Json(name = "maxtemp_c") val maxTempC: Double,
    @Json(name = "maxtemp_f") val maxTempF: Double,
    @Json(name = "mintemp_c") val minTempC: Double,
    @Json(name = "mintemp_f") val minTempF: Double,
    @Json(name = "avgtemp_c") val avgTempC: Double,
    @Json(name = "avgtemp_f") val avgTempF: Double,
    @Json(name = "maxwind_mph") val maxWindMph: Double,
    @Json(name = "maxwind_kph") val maxWindKph: Double,
    @Json(name = "totalprecip_mm") val totalPrecipMm: Double,
    @Json(name = "totalprecip_in") val totalPrecipIn: Double,
    @Json(name = "daily_chance_of_rain") val dailyChanceOfRain: Int,
    @Json(name = "daily_chance_of_snow") val dailyChanceOfSnow: Int,
    @Json(name = "condition") val condition: Condition,
    @Json(name = "uv") val uv: Double
)

@JsonClass(generateAdapter = true)
data class Hour(
    @Json(name = "time") val time: String,
    @Json(name = "temp_c") val tempC: Double,
    @Json(name = "temp_f") val tempF: Double,
    @Json(name = "condition") val condition: Condition,
    @Json(name = "wind_mph") val windMph: Double,
    @Json(name = "wind_kph") val windKph: Double,
    @Json(name = "wind_degree") val windDegree: Int,
    @Json(name = "wind_dir") val windDir: String,
    @Json(name = "pressure_mb") val pressureMb: Double,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "cloud") val cloud: Int,
    @Json(name = "feelslike_c") val feelsLikeC: Double,
    @Json(name = "feelslike_f") val feelsLikeF: Double,
    @Json(name = "windchill_c") val windchillC: Double,
    @Json(name = "windchill_f") val windchillF: Double,
    @Json(name = "heatindex_c") val heatindexC: Double,
    @Json(name = "heatindex_f") val heatindexF: Double,
    @Json(name = "dewpoint_c") val dewpointC: Double,
    @Json(name = "dewpoint_f") val dewpointF: Double,
    @Json(name = "will_it_rain") val willItRain: Int,
    @Json(name = "chance_of_rain") val chanceOfRain: Int,
    @Json(name = "will_it_snow") val willItSnow: Int,
    @Json(name = "chance_of_snow") val chanceOfSnow: Int,
    @Json(name = "vis_km") val visKm: Double,
    @Json(name = "vis_miles") val visMiles: Double,
    @Json(name = "uv") val uv: Double
)
