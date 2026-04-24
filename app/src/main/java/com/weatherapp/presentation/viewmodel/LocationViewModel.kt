package com.weatherapp.presentation.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.weatherapp.domain.model.City
import com.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.weatherapp.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val weatherRepository: WeatherRepository,
    private val application: Application
) : ViewModel() {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _locationState = MutableStateFlow<LocationState>(LocationState.Idle)
    val locationState: StateFlow<LocationState> = _locationState.asStateFlow()

    fun getCurrentLocation() {
        when {
            ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                requestLocation()
            }
            else -> {
                _locationState.value = LocationState.PermissionRequired
            }
        }
    }

    @Suppress("MissingPermission")
    private fun requestLocation() {
        viewModelScope.launch {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val city = City(
                            name = "Current Location",
                            latitude = location.latitude,
                            longitude = location.longitude,
                            country = "",
                            admin1 = null
                        )
                        _locationState.value = LocationState.Success(city)
                        viewModelScope.launch {
                            weatherRepository.saveCity(city, isCurrentLocation = true)
                            getCurrentWeatherUseCase(location.latitude, location.longitude)
                        }
                    } else {
                        _locationState.value = LocationState.Error("Unable to get location")
                    }
                }.addOnFailureListener { exception ->
                    _locationState.value = LocationState.Error(exception.message ?: "Location error")
                }
            } catch (e: Exception) {
                _locationState.value = LocationState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class LocationState {
    object Idle : LocationState()
    object PermissionRequired : LocationState()
    data class Success(val city: City) : LocationState()
    data class Error(val message: String) : LocationState()
}
