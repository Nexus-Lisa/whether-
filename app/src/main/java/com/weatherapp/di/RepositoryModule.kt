package com.weatherapp.di

import com.weatherapp.data.repository.WeatherRepositoryImpl
import com.weatherapp.data.repository.AirQualityRepository as AirQualityRepositoryImpl
import com.weatherapp.domain.repository.WeatherRepository
import com.weatherapp.domain.repository.AirQualityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindAirQualityRepository(
        airQualityRepositoryImpl: AirQualityRepositoryImpl
    ): AirQualityRepository
}
