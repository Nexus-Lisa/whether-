package com.weatherapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.weatherapp.presentation.screens.*
import com.weatherapp.presentation.viewmodel.LocationViewModel
import com.weatherapp.presentation.viewmodel.WeatherViewModel

@Composable
fun WeatherNavigation(
    weatherViewModel: WeatherViewModel,
    locationViewModel: LocationViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "weather"
    ) {
        composable("weather") {
            WeatherScreen(
                weatherViewModel = weatherViewModel,
                locationViewModel = locationViewModel,
                onNavigateToForecast = { navController.navigate("forecast") },
                onNavigateToMap = { navController.navigate("map") },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        
        composable("forecast") {
            ForecastScreen(
                weatherViewModel = weatherViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("map") {
            WeatherMapScreen(
                weatherViewModel = weatherViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("onboarding") {
            OnboardingScreen(
                onOnboardingComplete = { navController.navigate("weather") }
            )
        }
    }
}
