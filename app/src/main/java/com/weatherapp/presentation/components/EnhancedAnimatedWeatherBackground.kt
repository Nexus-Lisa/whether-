package com.weatherapp.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.weatherapp.domain.model.WeatherCodes
import kotlin.math.*
import kotlin.random.Random

@Composable
fun EnhancedAnimatedWeatherBackground(
    weatherCode: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "weather_animation")
    
    when {
        WeatherCodes.isClear(weatherCode) -> ClearSkyBackground(infiniteTransition, modifier)
        WeatherCodes.isCloudy(weatherCode) -> CloudyBackground(infiniteTransition, modifier)
        WeatherCodes.isRainy(weatherCode) -> RainyBackground(infiniteTransition, modifier)
        WeatherCodes.isSnowy(weatherCode) -> SnowyBackground(infiniteTransition, modifier)
        WeatherCodes.isThunderstorm(weatherCode) -> ThunderstormBackground(infiniteTransition, modifier)
        WeatherCodes.isFoggy(weatherCode) -> FoggyBackground(infiniteTransition, modifier)
        else -> ClearSkyBackground(infiniteTransition, modifier)
    }
}

@Composable
private fun ClearSkyBackground(
    transition: InfiniteTransition,
    modifier: Modifier
) {
    val sunRotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    val cloudOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = this.size.width
        val canvasHeight = this.size.height
        
        // Gradient background
        drawRect(
            color = Color(0xFF0A0A0A),
            size = Size(canvasWidth, canvasHeight)
        )
        
        // Sun rays
        rotate(sunRotation) {
            val centerX = canvasWidth * 0.8f
            val centerY = canvasHeight * 0.2f
            val sunRadius = 40.dp.toPx()
            
            // Sun glow
            for (i in 3 downTo 1) {
                drawCircle(
                    color = Color(0xFFFFD700).copy(alpha = 0.1f / i),
                    radius = sunRadius * (1.5f + i * 0.3f),
                    center = Offset(centerX, centerY),
                    blendMode = BlendMode.Screen
                )
            }
            
            // Sun core
            drawCircle(
                color = Color(0xFFFFD700),
                radius = sunRadius,
                center = Offset(centerX, centerY)
            )
        }
        
        // Floating clouds
        for (i in 0..2) {
            val cloudX = (canvasWidth * 0.1f + cloudOffset + i * canvasWidth * 0.3f) % (canvasWidth + 100.dp.toPx()) - 50.dp.toPx()
            val cloudY = canvasHeight * (0.15f + i * 0.1f)
            
            drawCloud(cloudX, cloudY, Color.White.copy(alpha = 0.3f))
        }
    }
}

@Composable
private fun CloudyBackground(
    transition: InfiniteTransition,
    modifier: Modifier
) {
    val cloudOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = this.size.width
        val canvasHeight = this.size.height
        
        // Dark gradient background
        drawRect(
            color = Color(0xFF1A1A2E),
            size = Size(canvasWidth, canvasHeight)
        )
        
        // Multiple cloud layers
        for (layer in 0..2) {
            for (i in 0..3) {
                val cloudX = (canvasWidth * 0.1f + cloudOffset + i * canvasWidth * 0.25f + layer * 50.dp.toPx()) % (canvasWidth + 100.dp.toPx()) - 50.dp.toPx()
                val cloudY = canvasHeight * (0.2f + layer * 0.15f + i * 0.1f)
                val alpha = 0.4f - layer * 0.1f
                
                drawCloud(cloudX, cloudY, Color.White.copy(alpha = alpha))
            }
        }
    }
}

@Composable
private fun RainyBackground(
    transition: InfiniteTransition,
    modifier: Modifier
) {
    val raindrops = remember { mutableStateListOf<Raindrop>() }
    
    LaunchedEffect(Unit) {
        raindrops.clear()
        repeat(100) {
            raindrops.add(
                Raindrop(
                    x = Random.nextFloat() * 2000f,
                    y = Random.nextFloat() * 2000f - 2000f,
                    speed = Random.nextFloat() * 8f + 4f,
                    length = Random.nextFloat() * 40f + 20f,
                    opacity = Random.nextFloat() * 0.6f + 0.4f
                )
            )
        }
    }
    
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = this.size.width
        val canvasHeight = this.size.height
        
        // Dark stormy background
        drawRect(
            color = Color(0xFF0F1419),
            size = Size(canvasWidth, canvasHeight)
        )
        
        // Dark clouds
        for (i in 0..2) {
            val cloudX = (canvasWidth * 0.1f + time * 0.5f + i * canvasWidth * 0.3f) % (canvasWidth + 100.dp.toPx()) - 50.dp.toPx()
            val cloudY = canvasHeight * (0.1f + i * 0.08f)
            
            drawCloud(cloudX, cloudY, Color(0xFF424242).copy(alpha = 0.8f))
        }
        
        // Rain drops
        raindrops.forEach { drop ->
            drop.y += drop.speed
            if (drop.y > canvasHeight) {
                drop.y = -drop.length
                drop.x = Random.nextFloat() * canvasWidth
            }
            
            drawRaindrop(drop)
        }
    }
}

@Composable
private fun SnowyBackground(
    transition: InfiniteTransition,
    modifier: Modifier
) {
    val snowflakes = remember { mutableStateListOf<Snowflake>() }
    
    LaunchedEffect(Unit) {
        snowflakes.clear()
        repeat(80) {
            snowflakes.add(
                Snowflake(
                    x = Random.nextFloat() * 2000f,
                    y = Random.nextFloat() * 2000f,
                    speed = Random.nextFloat() * 2f + 1f,
                    size = Random.nextFloat() * 16f + 8f,
                    rotation = Random.nextFloat() * 360f,
                    rotationSpeed = Random.nextFloat() * 2f - 1f,
                    opacity = Random.nextFloat() * 0.6f + 0.4f
                )
            )
        }
    }
    
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = this.size.width
        val canvasHeight = this.size.height
        
        // Winter gradient background
        drawRect(
            color = Color(0xFF1E3A8A),
            size = Size(canvasWidth, canvasHeight)
        )
        
        // Snow clouds
        for (i in 0..2) {
            val cloudX = (canvasWidth * 0.1f + time * 0.3f + i * canvasWidth * 0.25f) % (canvasWidth + 100.dp.toPx()) - 50.dp.toPx()
            val cloudY = canvasHeight * (0.15f + i * 0.1f)
            
            drawCloud(cloudX, cloudY, Color.White.copy(alpha = 0.7f))
        }
        
        // Snowflakes
        snowflakes.forEach { flake ->
            flake.y += flake.speed
            flake.rotation += flake.rotationSpeed
            
            if (flake.y > canvasHeight) {
                flake.y = -flake.size
                flake.x = Random.nextFloat() * canvasWidth
            }
            
            drawSnowflake(flake)
        }
    }
}

@Composable
private fun ThunderstormBackground(
    transition: InfiniteTransition,
    modifier: Modifier
) {
    val lightning by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(3000, easing = EaseInOut),
            repeatMode = RepeatMode.Restart
        )
    )
    
    val raindrops = remember { mutableStateListOf<Raindrop>() }
    
    LaunchedEffect(Unit) {
        raindrops.clear()
        repeat(120) {
            raindrops.add(
                Raindrop(
                    x = Random.nextFloat() * 2000f,
                    y = Random.nextFloat() * 2000f - 2000f,
                    speed = Random.nextFloat() * 12f + 8f,
                    length = Random.nextFloat() * 50f + 30f,
                    opacity = Random.nextFloat() * 0.7f + 0.3f
                )
            )
        }
    }
    
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = this.size.width
        val canvasHeight = this.size.height
        
        // Very dark background
        drawRect(
            color = Color(0xFF0A0A0A),
            size = Size(canvasWidth, canvasHeight)
        )
        
        // Storm clouds
        for (i in 0..3) {
            val cloudX = (canvasWidth * 0.05f + time * 0.8f + i * canvasWidth * 0.2f) % (canvasWidth + 100.dp.toPx()) - 50.dp.toPx()
            val cloudY = canvasHeight * (0.05f + i * 0.08f)
            
            drawCloud(cloudX, cloudY, Color(0xFF2C2C2C).copy(alpha = 0.9f))
        }
        
        // Lightning flashes
        if (lightning > 0.8f) {
            for (i in 0..2) {
                val startX = Random.nextFloat() * canvasWidth
                val startY = Random.nextFloat() * canvasHeight * 0.3f
                val endX = startX + Random.nextFloat() * 200.dp.toPx() - 100.dp.toPx()
                val endY = startY + Random.nextFloat() * 300.dp.toPx() + 100.dp.toPx()
                
                drawLine(
                    color = Color.White.copy(alpha = 0.9f),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 3.dp.toPx()
                )
                
                // Lightning glow
                drawLine(
                    color = Color(0xFF87CEEB).copy(alpha = 0.3f),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 8.dp.toPx()
                )
            }
        }
        
        // Heavy rain
        raindrops.forEach { drop ->
            drop.y += drop.speed
            if (drop.y > canvasHeight) {
                drop.y = -drop.length
                drop.x = Random.nextFloat() * canvasWidth
            }
            
            drawRaindrop(drop)
        }
    }
}

@Composable
private fun FoggyBackground(
    transition: InfiniteTransition,
    modifier: Modifier
) {
    val fogOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = this.size.width
        val canvasHeight = this.size.height
        
        // Gray gradient background
        drawRect(
            color = Color(0xFF2C3E50),
            size = Size(canvasWidth, canvasHeight)
        )
        
        // Fog layers
        for (layer in 0..4) {
            for (i in 0..5) {
                val fogX = (canvasWidth * 0.1f + fogOffset + i * canvasWidth * 0.15f + layer * 30.dp.toPx()) % (canvasWidth + 200.dp.toPx()) - 100.dp.toPx()
                val fogY = canvasHeight * (0.2f + layer * 0.15f)
                val alpha = 0.3f - layer * 0.05f
                
                drawCircle(
                    color = Color.White.copy(alpha = alpha),
                    radius = Random.nextFloat() * 100.dp.toPx() + 50.dp.toPx(),
                    center = Offset(fogX, fogY),
                    blendMode = BlendMode.Overlay
                )
            }
        }
    }
}

private fun DrawScope.drawCloud(x: Float, y: Float, color: Color) {
    // Main cloud body
    drawCircle(
        color = color,
        radius = 30.dp.toPx(),
        center = Offset(x, y)
    )
    
    // Cloud puffs
    drawCircle(
        color = color,
        radius = 25.dp.toPx(),
        center = Offset(x - 20.dp.toPx(), y + 5.dp.toPx())
    )
    
    drawCircle(
        color = color,
        radius = 25.dp.toPx(),
        center = Offset(x + 20.dp.toPx(), y + 5.dp.toPx())
    )
    
    drawCircle(
        color = color,
        radius = 20.dp.toPx(),
        center = Offset(x - 35.dp.toPx(), y + 10.dp.toPx())
    )
    
    drawCircle(
        color = color,
        radius = 20.dp.toPx(),
        center = Offset(x + 35.dp.toPx(), y + 10.dp.toPx())
    )
}

private fun DrawScope.drawRaindrop(drop: Raindrop) {
    drawLine(
        color = Color(0xFF4FC3F7).copy(alpha = drop.opacity),
        start = Offset(drop.x, drop.y),
        end = Offset(drop.x, drop.y + drop.length),
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawSnowflake(flake: Snowflake) {
    rotate(flake.rotation) {
        for (i in 0..5) {
            val angle = i * 60f
            val x = flake.x + cos(angle * PI.toFloat() / 180f) * flake.size
            val y = flake.y + sin(angle * PI.toFloat() / 180f) * flake.size
            
            drawLine(
                color = Color.White.copy(alpha = flake.opacity),
                start = Offset(flake.x, flake.y),
                end = Offset(x, y),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

private data class Raindrop(
    var x: Float,
    var y: Float,
    val speed: Float,
    val length: Float,
    val opacity: Float
)

private data class Snowflake(
    var x: Float,
    var y: Float,
    val speed: Float,
    val size: Float,
    var rotation: Float,
    val rotationSpeed: Float,
    val opacity: Float
)

