package com.weatherapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weatherapp.domain.model.AirQuality
import com.weatherapp.domain.model.AirQualityIndex

@Composable
fun AirQualityCard(
    airQuality: AirQuality,
    cityName: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.7f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // AQI Circle Indicator
            Box(
                modifier = Modifier.size(60.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background circle with AQI color
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            color = try {
                                Color(android.graphics.Color.parseColor(AirQualityIndex.getAQIColor(airQuality.aqi)))
                            } catch (e: Exception) {
                                Color.Gray
                            },
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${airQuality.aqi}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                // AQI Label
                Text(
                    text = "AQI",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // AQI Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = AirQualityIndex.getAQICategory(airQuality.aqi),
                    color = try {
                        Color(android.graphics.Color.parseColor(AirQualityIndex.getAQIColor(airQuality.aqi)))
                    } catch (e: Exception) {
                        Color.White
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = AirQualityIndex.getAQIRecommendation(airQuality.aqi),
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Pollutant Details
                PollutantRow("PM2.5", "${airQuality.pm25} μg/m³", "Мелкие частицы")
                PollutantRow("PM10", "${airQuality.pm10} μg/m³", "Крупные частицы")
                PollutantRow("O₃", "${airQuality.o3} μg/m³", "Озон")
                PollutantRow("NO₂", "${airQuality.no2} μg/m³", "Диоксид азота")
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Arrow Icon
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Details",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun PollutantRow(name: String, value: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = Color(0xFF00BFFF),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = description,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}
