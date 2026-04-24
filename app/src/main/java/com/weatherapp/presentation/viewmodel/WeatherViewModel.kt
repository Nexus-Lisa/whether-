package com.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.domain.model.AirQuality
import com.weatherapp.domain.model.City
import com.weatherapp.domain.model.Result
import com.weatherapp.domain.model.Weather
import com.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.weatherapp.domain.usecase.GetAirQualityUseCase
import com.weatherapp.domain.usecase.SearchCitiesUseCase
import com.weatherapp.domain.repository.WeatherRepository
import com.weatherapp.domain.repository.AirQualityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getAirQualityUseCase: GetAirQualityUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val weatherRepository: WeatherRepository,
    private val airQualityRepository: AirQualityRepository
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val _airQualityState = MutableStateFlow<AirQualityState>(AirQualityState.Loading)
    val airQualityState: StateFlow<AirQualityState> = _airQualityState.asStateFlow()

    private val _citiesState = MutableStateFlow<CitiesState>(CitiesState.Loading)
    val citiesState: StateFlow<CitiesState> = _citiesState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCity = MutableStateFlow<City?>(null)
    val selectedCity: StateFlow<City?> = _selectedCity.asStateFlow()

    init {
        loadCities()
    }

    fun loadWeatherForLocation(latitude: Double, longitude: Double, cityName: String = "Unknown") {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            _airQualityState.value = AirQualityState.Loading

            val weatherResult = getCurrentWeatherUseCase(latitude, longitude)
            when (weatherResult) {
                is Result.Success -> {
                    val weather = weatherResult.data.copy(cityName = cityName)
                    _weatherState.value = WeatherState.Success(weather)
                    loadAirQuality(latitude, longitude)
                }
                is Result.Error -> {
                    _weatherState.value = WeatherState.Error(weatherResult.message)
                    // Try to load cached data
                    val cachedWeather = weatherRepository.getCachedWeather(cityName)
                    if (cachedWeather != null) {
                        _weatherState.value = WeatherState.Success(cachedWeather)
                        _airQualityState.value = AirQualityState.Error("Offline mode")
                    }
                }
                is Result.Loading -> {
                    // Already set to Loading above
                }
            }
        }
    }

    fun searchCities(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            if (query.length >= 2) {
                _citiesState.value = CitiesState.Loading
                val result = searchCitiesUseCase(query)
                when (result) {
                    is Result.Success -> {
                        _citiesState.value = CitiesState.Success(result.data)
                    }
                    is Result.Error -> {
                        _citiesState.value = CitiesState.Error(result.message)
                    }
                    is Result.Loading -> {
                        _citiesState.value = CitiesState.Loading
                    }
                }
            } else {
                _citiesState.value = CitiesState.Success(emptyList())
            }
        }
    }

    fun selectCity(city: City) {
        _selectedCity.value = city
        loadWeatherForLocation(city.latitude, city.longitude, city.name)
        viewModelScope.launch {
            weatherRepository.saveCity(city)
        }
    }

    private fun loadCities() {
        viewModelScope.launch {
            weatherRepository.getAllCities().collect { cities ->
                _citiesState.value = CitiesState.Success(cities)
            }
        }
    }

    fun deleteCity(cityName: String) {
        viewModelScope.launch {
            weatherRepository.deleteCity(cityName)
        }
    }

    private fun loadAirQuality(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val result = getAirQualityUseCase(latitude, longitude)
            when (result) {
                is com.weatherapp.domain.model.Result.Success -> {
                    _airQualityState.value = AirQualityState.Success(result.data)
                }
                is com.weatherapp.domain.model.Result.Error -> {
                    _airQualityState.value = AirQualityState.Error(result.message)
                }
                is com.weatherapp.domain.model.Result.Loading -> {
                    _airQualityState.value = AirQualityState.Loading
                }
            }
        }
    }
}

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val weather: Weather) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

sealed class AirQualityState {
    object Loading : AirQualityState()
    data class Success(val airQuality: AirQuality) : AirQualityState()
    data class Error(val message: String) : AirQualityState()
}

sealed class CitiesState {
    object Loading : CitiesState()
    data class Success(val cities: List<City>) : CitiesState()
    data class Error(val message: String) : CitiesState()
}
