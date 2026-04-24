package com.weatherapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    var temperatureUnit by remember { mutableStateOf("celsius") }
    var windUnit by remember { mutableStateOf("kmh") }
    var refreshInterval by remember { mutableStateOf("1hour") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = Color.White) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Temperature Unit Setting
            SettingsCard(title = "Temperature Unit") {
                Column(Modifier.selectableGroup()) {
                    RadioButtonOption(
                        text = "Celsius (°C)",
                        selected = temperatureUnit == "celsius",
                        onClick = { temperatureUnit = "celsius" }
                    )
                    RadioButtonOption(
                        text = "Fahrenheit (°F)",
                        selected = temperatureUnit == "fahrenheit",
                        onClick = { temperatureUnit = "fahrenheit" }
                    )
                }
            }

            // Wind Unit Setting
            SettingsCard(title = "Wind Speed Unit") {
                Column(Modifier.selectableGroup()) {
                    RadioButtonOption(
                        text = "km/h",
                        selected = windUnit == "kmh",
                        onClick = { windUnit = "kmh" }
                    )
                    RadioButtonOption(
                        text = "m/s",
                        selected = windUnit == "ms",
                        onClick = { windUnit = "ms" }
                    )
                    RadioButtonOption(
                        text = "knots",
                        selected = windUnit == "knots",
                        onClick = { windUnit = "knots" }
                    )
                }
            }

            // Refresh Interval Setting
            SettingsCard(title = "Auto Refresh Interval") {
                Column(Modifier.selectableGroup()) {
                    RadioButtonOption(
                        text = "30 minutes",
                        selected = refreshInterval == "30min",
                        onClick = { refreshInterval = "30min" }
                    )
                    RadioButtonOption(
                        text = "1 hour",
                        selected = refreshInterval == "1hour",
                        onClick = { refreshInterval = "1hour" }
                    )
                    RadioButtonOption(
                        text = "3 hours",
                        selected = refreshInterval == "3hours",
                        onClick = { refreshInterval = "3hours" }
                    )
                }
            }

            // About Section
            SettingsCard(title = "About") {
                Column {
                    Text(
                        text = "Weather App",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Version 1.0.0",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Weather data provided by Open-Meteo and WeatherAPI.com",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsCard(
    title: String,
    content: @Composable () -> Unit
) {
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
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}

@Composable
fun RadioButtonOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF00BFFF)
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}
