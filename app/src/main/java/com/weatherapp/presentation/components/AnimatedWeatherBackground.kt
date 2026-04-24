package com.weatherapp.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.*
import kotlin.random.Random

@Composable
fun AnimatedWeatherBackground(
    weatherCode: Int,
    modifier: Modifier = Modifier
) {
    val particles = remember { mutableStateListOf<Particle>() }
    val animationProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "weather_animation"
    )

    LaunchedEffect(weatherCode) {
        particles.clear()
        when {
            com.weatherapp.domain.model.WeatherCodes.isRainy(weatherCode) -> {
                repeat(50) {
                    particles.add(
                        Particle(
                            x = Random.nextFloat(),
                            y = Random.nextFloat(),
                            speed = Random.nextFloat() * 0.02f + 0.01f,
                            size = Random.nextFloat() * 4f + 2f,
                            type = ParticleType.RAIN
                        )
                    )
                }
            }
            com.weatherapp.domain.model.WeatherCodes.isSnowy(weatherCode) -> {
                repeat(30) {
                    particles.add(
                        Particle(
                            x = Random.nextFloat(),
                            y = Random.nextFloat(),
                            speed = Random.nextFloat() * 0.01f + 0.005f,
                            size = Random.nextFloat() * 6f + 3f,
                            type = ParticleType.SNOW
                        )
                    )
                }
            }
            com.weatherapp.domain.model.WeatherCodes.isCloudy(weatherCode) -> {
                repeat(5) {
                    particles.add(
                        Particle(
                            x = Random.nextFloat(),
                            y = Random.nextFloat() * 0.3f,
                            speed = Random.nextFloat() * 0.002f + 0.001f,
                            size = Random.nextFloat() * 100f + 50f,
                            type = ParticleType.CLOUD
                        )
                    )
                }
            }
            com.weatherapp.domain.model.WeatherCodes.isThunderstorm(weatherCode) -> {
                repeat(60) {
                    particles.add(
                        Particle(
                            x = Random.nextFloat(),
                            y = Random.nextFloat(),
                            speed = Random.nextFloat() * 0.03f + 0.02f,
                            size = Random.nextFloat() * 4f + 2f,
                            type = ParticleType.RAIN
                        )
                    )
                }
            }
        }
    }

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        particles.forEach { particle ->
            particle.y += particle.speed * animationProgress
            if (particle.y > 1f) {
                particle.y = -0.1f
                particle.x = Random.nextFloat()
            }

            when (particle.type) {
                ParticleType.RAIN -> drawRainDrop(particle)
                ParticleType.SNOW -> drawSnowflake(particle)
                ParticleType.CLOUD -> drawCloud(particle)
            }
        }
    }
}

data class Particle(
    var x: Float,
    var y: Float,
    val speed: Float,
    val size: Float,
    val type: ParticleType
)

enum class ParticleType {
    RAIN, SNOW, CLOUD
}

private fun DrawScope.drawRainDrop(particle: Particle) {
    val x = particle.x * size.width
    val y = particle.y * size.height
    
    drawLine(
        color = Color(0xFF4FC3F7).copy(alpha = 0.6f),
        start = Offset(x, y),
        end = Offset(x, y + particle.size),
        strokeWidth = 2f
    )
}

private fun DrawScope.drawSnowflake(particle: Particle) {
    val x = particle.x * size.width
    val y = particle.y * size.height
    
    drawCircle(
        color = Color.White.copy(alpha = 0.8f),
        radius = particle.size,
        center = Offset(x, y)
    )
}

private fun DrawScope.drawCloud(particle: Particle) {
    val x = particle.x * size.width
    val y = particle.y * size.height
    
    drawCircle(
        color = Color.Gray.copy(alpha = 0.3f),
        radius = particle.size,
        center = Offset(x, y)
    )
    drawCircle(
        color = Color.Gray.copy(alpha = 0.3f),
        radius = particle.size * 0.8f,
        center = Offset(x + particle.size * 0.5f, y)
    )
    drawCircle(
        color = Color.Gray.copy(alpha = 0.3f),
        radius = particle.size * 0.7f,
        center = Offset(x - particle.size * 0.5f, y)
    )
}
