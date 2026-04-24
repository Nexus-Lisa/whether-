package com.weatherapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Air
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.rememberCameraPositionState
import com.weatherapp.presentation.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherMapScreen(
    weatherViewModel: com.weatherapp.presentation.viewmodel.WeatherViewModel,
    onNavigateBack: () -> Unit
) {
    val weatherState by weatherViewModel.weatherState.collectAsState()
    var selectedLayer by remember { mutableStateOf("precipitation") }
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(weatherState) {
        if (weatherState is com.weatherapp.presentation.viewmodel.WeatherState.Success) {
            val weather = (weatherState as com.weatherapp.presentation.viewmodel.WeatherState.Success).weather
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(weather.latitude, weather.longitude),
                    10f
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather Map", color = Color.White) },
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
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    mapType = MapType.NORMAL,
                    isMyLocationEnabled = true
                ),
                onMapClick = { latLng ->
                    // Handle map click to show weather at that location
                    weatherViewModel.loadWeatherForLocation(latLng.latitude, latLng.longitude, "Selected Location")
                }
            ) {
                // Add weather overlay tiles based on selected layer
                when (selectedLayer) {
                    "precipitation" -> {
                        addPrecipitationTiles()
                    }
                    "temperature" -> {
                        addTemperatureTiles()
                    }
                    "wind" -> {
                        addWindTiles()
                    }
                }
            }

            // Layer selection buttons
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A1A1A)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "Weather Layers",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(8.dp)
                        )
                        
                        FilterChip(
                            onClick = { selectedLayer = "precipitation" },
                            label = { Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Opacity, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Precipitation")
                            }},
                            selected = selectedLayer == "precipitation",
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1E90FF),
                                selectedLabelColor = Color.White
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        FilterChip(
                            onClick = { selectedLayer = "temperature" },
                            label = { Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Thermostat, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Temperature")
                            }},
                            selected = selectedLayer == "temperature",
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1E90FF),
                                selectedLabelColor = Color.White
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        FilterChip(
                            onClick = { selectedLayer = "wind" },
                            label = { Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Air, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Wind")
                            }},
                            selected = selectedLayer == "wind",
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1E90FF),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}

private fun addPrecipitationTiles() {
    // This would add precipitation tile overlay
    // Implementation would use TileOverlay with Open-Meteo precipitation tiles
}

private fun addTemperatureTiles() {
    // This would add temperature tile overlay
    // Implementation would use TileOverlay with Open-Meteo temperature tiles
}

private fun addWindTiles() {
    // This would add wind tile overlay
    // Implementation would use TileOverlay with Open-Meteo wind tiles
}
