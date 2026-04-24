package com.weatherapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weatherapp.domain.model.WeatherCodes
import com.weatherapp.presentation.components.WeatherIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    weatherViewModel: com.weatherapp.presentation.viewmodel.WeatherViewModel,
    onNavigateBack: () -> Unit
) {
    val weatherState by weatherViewModel.weatherState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("10-Day Forecast", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
        ) {
            val state = weatherState
            when (state) {
                is com.weatherapp.presentation.viewmodel.WeatherState.Success -> {
                    ForecastContent(weather = state.weather)
                }
                else -> {
                    Text(
                        text = "Loading forecast...",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun ForecastContent(weather: com.weatherapp.domain.model.Weather) {
    val safeItemCount = minOf(
        weather.dailyTime.size,
        weather.dailyMaxTemp.size,
        weather.dailyMinTemp.size,
        weather.dailyWeatherCode.size,
        weather.dailyPrecipitation.size
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(safeItemCount) { index ->
            DailyForecastItem(
                date = weather.dailyTime[index],
                maxTemp = weather.dailyMaxTemp[index].toInt(),
                minTemp = weather.dailyMinTemp[index].toInt(),
                weatherCode = weather.dailyWeatherCode[index],
                precipitation = weather.dailyPrecipitation[index]
            )
        }
    }
}

@Composable
fun DailyForecastItem(
    date: String,
    maxTemp: Int,
    minTemp: Int,
    weatherCode: Int,
    precipitation: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date and weather description
            Column(
                modifier = Modifier.weight(1f)
            ) {
                val dayName = try {
                    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    val parsedDate = sdf.parse(date)
                    val dayFormat = java.text.SimpleDateFormat("EEEE", java.util.Locale.getDefault())
                    dayFormat.format(parsedDate ?: date)
                } catch (e: Exception) {
                    date
                }
                
                Text(
                    text = dayName,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = WeatherCodes.getWeatherDescription(weatherCode),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            
            // Weather icon
            Icon(
                imageVector = getWeatherIcon(weatherCode),
                contentDescription = "Weather",
                tint = getWeatherIconColor(weatherCode),
                modifier = Modifier.size(32.dp)
            )
            
            // Temperature
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${maxTemp}°",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${minTemp}°",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            
            // Precipitation
            if (precipitation > 0) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = "${precipitation}%",
                        color = Color(0xFF00BFFF),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun getWeatherIcon(weatherCode: Int) = WeatherIcons.getWeatherIcon(weatherCode)

@Composable
fun getWeatherIconColor(weatherCode: Int) = when {
    com.weatherapp.domain.model.WeatherCodes.isClear(weatherCode) -> Color.Yellow
    com.weatherapp.domain.model.WeatherCodes.isCloudy(weatherCode) -> Color.Gray
    com.weatherapp.domain.model.WeatherCodes.isRainy(weatherCode) -> Color(0xFF00BFFF)
    com.weatherapp.domain.model.WeatherCodes.isSnowy(weatherCode) -> Color.White
    com.weatherapp.domain.model.WeatherCodes.isThunderstorm(weatherCode) -> Color(0xFFFFD700)
    com.weatherapp.domain.model.WeatherCodes.isFoggy(weatherCode) -> Color.LightGray
    else -> Color.Yellow
}
