package com.weatherapp.presentation.screens

import androidx.compose.animation.core.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    var canProceed by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        canProceed = false
        delay(1000) // Wait 1 second before allowing to proceed
        canProceed = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Welcome", color = Color.White) },
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
            Column {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { page ->
                    OnboardingPage(page = page)
                }

                // Page indicators
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(3) { page ->
                        val isSelected = pagerState.currentPage == page
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .padding(horizontal = 4.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) Color(0xFF00BFFF) else Color.Gray
                                )
                        )
                    }
                }

                // Navigation buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (pagerState.currentPage > 0) {
                        TextButton(
                            onClick = { 
                                if (canProceed) {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                }
                            },
                            enabled = canProceed
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Previous", color = Color.White)
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.width(100.dp))
                    }

                    if (pagerState.currentPage < 2) {
                        TextButton(
                            onClick = { 
                                if (canProceed) {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            },
                            enabled = canProceed
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Next", color = Color.White)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                            }
                        }
                    } else {
                        Button(
                            onClick = onOnboardingComplete,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1E90FF)
                            ),
                            enabled = canProceed
                        ) {
                            Text("Get Started", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingPage(page: Int) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1000),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (page) {
            0 -> OnboardingPageContent(
                icon = Icons.Default.Map,
                title = "Interactive Weather Map",
                description = "Explore weather layers including precipitation, temperature, and wind patterns on an interactive map. Tap any location to get detailed weather information.",
                scale = scale
            )
            1 -> OnboardingPageContent(
                icon = Icons.Default.Air,
                title = "Air Quality Monitoring",
                description = "Stay informed about air quality in your area with real-time AQI data and health recommendations. Get detailed information and links to external resources.",
                scale = scale
            )
            2 -> OnboardingPageContent(
                icon = Icons.Default.DateRange,
                title = "Extended Forecasts",
                description = "Access detailed hourly forecasts and 10-day weather predictions to plan your activities with confidence.",
                scale = scale
            )
        }
    }
}

@Composable
fun OnboardingPageContent(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    scale: Float
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = Color(0xFF00BFFF),
        modifier = Modifier
            .size(120.dp)
            .padding(bottom = 32.dp)
    )

    Text(
        text = title,
        color = Color.White,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    Text(
        text = description,
        color = Color.Gray,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        lineHeight = 24.sp
    )
}
