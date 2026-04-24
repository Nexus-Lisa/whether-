package com.weatherapp.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weatherapp.domain.model.AirQualityIndex
import com.weatherapp.domain.model.WeatherCodes
import com.weatherapp.presentation.components.*
import com.weatherapp.presentation.components.AirQualityCard
import com.weatherapp.presentation.components.WeatherIcons
import com.weatherapp.presentation.viewmodel.LocationViewModel
import com.weatherapp.presentation.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel,
    locationViewModel: LocationViewModel,
    onNavigateToForecast: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val weatherState by weatherViewModel.weatherState.collectAsState()
    val airQualityState by weatherViewModel.airQualityState.collectAsState()
    val locationState by locationViewModel.locationState.collectAsState()

    LaunchedEffect(locationState) {
        if (locationState is com.weatherapp.presentation.viewmodel.LocationState.Success) {
            val city = (locationState as com.weatherapp.presentation.viewmodel.LocationState.Success).city
            weatherViewModel.loadWeatherForLocation(city.latitude, city.longitude, city.name)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        weatherState.let { state ->
            when (state) {
                is com.weatherapp.presentation.viewmodel.WeatherState.Loading -> {
                    LoadingIndicator()
                }
                is com.weatherapp.presentation.viewmodel.WeatherState.Success -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        EnhancedAnimatedWeatherBackground(
                            weatherCode = state.weather.weatherCode,
                            modifier = Modifier.fillMaxSize()
                        )
                        WeatherContent(
                            weather = state.weather,
                            airQualityState = airQualityState,
                            onNavigateToForecast = onNavigateToForecast,
                            onNavigateToMap = onNavigateToMap,
                            onNavigateToSettings = onNavigateToSettings
                        )
                    }
                }
                is com.weatherapp.presentation.viewmodel.WeatherState.Error -> {
                    ErrorContent(
                        message = state.message,
                        onRetry = { locationViewModel.getCurrentLocation() }
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherContent(
    weather: com.weatherapp.domain.model.Weather,
    airQualityState: com.weatherapp.presentation.viewmodel.AirQualityState,
    onNavigateToForecast: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header with location and settings
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = weather.cityName,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = WeatherCodes.getWeatherDescription(weather.weatherCode),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
            IconButton(onClick = onNavigateToSettings) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Current temperature
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${weather.temperature.toInt()}°",
                color = Color(0xFF00BFFF),
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Feels like ${weather.temperature.toInt()}°",
                color = Color.Gray,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Weather details grid
        WeatherDetailsGrid(weather = weather)

        Spacer(modifier = Modifier.height(24.dp))

        // Air Quality
        airQualityState.let { aqState ->
            when (aqState) {
                is com.weatherapp.presentation.viewmodel.AirQualityState.Success -> {
                    AirQualityCard(
                        airQuality = aqState.airQuality,
                        cityName = weather.cityName,
                        onClick = { /* Navigate to details */ }
                    )
                }
                is com.weatherapp.presentation.viewmodel.AirQualityState.Error -> {
                    Text(
                        text = aqState.message,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                is com.weatherapp.presentation.viewmodel.AirQualityState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF00BFFF)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Hourly forecast preview
        HourlyForecastPreview(
            hourlyTime = weather.hourlyTime.take(12),
            hourlyTemperature = weather.hourlyTemperature.take(12),
            hourlyPrecipitation = weather.hourlyPrecipitation.take(12)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onNavigateToForecast,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E90FF)
                )
            ) {
                Text("10-Day Forecast", color = Color.White)
            }
            Button(
                onClick = onNavigateToMap,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E90FF)
                )
            ) {
                Text("Weather Map", color = Color.White)
            }
        }
    }
}

@Composable
fun WeatherDetailsGrid(weather: com.weatherapp.domain.model.Weather) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Weather Details",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Air, contentDescription = "Wind", tint = Color(0xFF00BFFF))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${weather.windSpeed.toInt()} km/h",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Wind",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Opacity, contentDescription = "Humidity", tint = Color(0xFF00BFFF))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${weather.humidity}%",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Humidity",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Speed, contentDescription = "Pressure", tint = Color(0xFF00BFFF))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${weather.pressure.toInt()} hPa",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Pressure",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Visibility, contentDescription = "Visibility", tint = Color(0xFF00BFFF))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${weather.visibility.toInt()} km",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Visibility",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.WbSunny, contentDescription = "UV Index", tint = Color(0xFF00BFFF))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = weather.uvIndex.toInt().toString(),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "UV Index",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.WbTwilight, contentDescription = "Sunrise", tint = Color(0xFF00BFFF))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = weather.sunrise.substringAfter("T").substringBefore(":"),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Sunrise",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.NightsStay, contentDescription = "Sunset", tint = Color(0xFF00BFFF))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = weather.sunset.substringAfter("T").substringBefore(":"),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Sunset",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AirQualityCard(airQualityState: com.weatherapp.presentation.viewmodel.AirQualityState, cityName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Air Quality",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            when (airQualityState) {
                is com.weatherapp.presentation.viewmodel.AirQualityState.Success -> {
                    val aqi = airQualityState.airQuality.aqi
                    val category = AirQualityIndex.getAQICategory(aqi)
                    val color = try {
                        Color(android.graphics.Color.parseColor(AirQualityIndex.getAQIColor(aqi)))
                    } catch (e: Exception) {
                        Color.Gray
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "AQI: $aqi",
                                color = color,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = category,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Text(
                                text = AirQualityIndex.getAQIRecommendation(aqi),
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                        
                        Button(
                            onClick = { /* Open website */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1E90FF)
                            )
                        ) {
                            Text("More on website", color = Color.White)
                        }
                    }
                }
                is com.weatherapp.presentation.viewmodel.AirQualityState.Error -> {
                    Text(
                        text = airQualityState.message,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                is com.weatherapp.presentation.viewmodel.AirQualityState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF00BFFF)
                    )
                }
            }
        }
    }
}

@Composable
fun HourlyForecastPreview(
    hourlyTime: List<String>,
    hourlyTemperature: List<Double>,
    hourlyPrecipitation: List<Int>
) {
    val safeItemCount = minOf(hourlyTime.size, hourlyTemperature.size, hourlyPrecipitation.size, 12)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Hourly Forecast",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(safeItemCount) { index ->
                    HourlyItem(
                        time = hourlyTime[index].substringAfter("T").substringBefore(":"),
                        temperature = hourlyTemperature[index].toInt(),
                        precipitation = hourlyPrecipitation[index]
                    )
                }
            }
        }
    }
}

@Composable
fun HourlyItem(time: String, temperature: Int, precipitation: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = time,
            color = Color.Gray,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Icon(
                WeatherIcons.getWeatherIcon(weatherCode = 0), // Default to clear for hourly
                contentDescription = "Weather",
                tint = if (precipitation > 30) Color(0xFF00BFFF) else Color.Yellow,
                modifier = Modifier.size(24.dp)
            )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${temperature}°",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        if (precipitation > 0) {
            Text(
                text = "${precipitation}%",
                color = Color(0xFF00BFFF),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFF00BFFF),
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun ErrorContent(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E90FF)
                )
            ) {
                Text("Retry", color = Color.White)
            }
        }
    }
}
