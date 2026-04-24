# Weather App

A comprehensive Android weather application built with modern technologies including Jetpack Compose, MVVM architecture, and Clean Architecture principles.

## Features

- **Current Weather Display**: Detailed weather information with temperature, wind, humidity, pressure, UV index, and more
- **Interactive Weather Map**: Google Maps integration with weather overlay layers (precipitation, temperature, wind)
- **10-Day Forecast**: Extended weather predictions with daily breakdowns
- **Hourly Forecast**: 24-48 hour detailed hourly predictions
- **Air Quality Monitoring**: Real-time AQI data with health recommendations and external links
- **Location Services**: Automatic GPS location detection and manual city search
- **Animated Weather Backgrounds**: Dynamic backgrounds that change based on weather conditions
- **Offline Support**: Cached weather data available when offline
- **Settings**: Customizable units (Celsius/Fahrenheit, km/h/m/s/knots) and refresh intervals
- **Onboarding**: Introduction screens for first-time users

## Architecture

The app follows Clean Architecture principles with clear separation of concerns:

- **Presentation Layer**: UI components, ViewModels, and navigation
- **Domain Layer**: Business logic, use cases, and models
- **Data Layer**: Repository implementation, API services, and local database

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Dagger Hilt
- **Networking**: Retrofit + OkHttp + Moshi
- **Database**: Room
- **Location**: Google Play Services Location
- **Maps**: Google Maps SDK
- **Async**: Coroutines + Flow
- **Image Loading**: Coil

## API Integration

The app integrates with multiple weather APIs:

1. **Open-Meteo** (Primary)
   - Current weather and forecasts
   - Air quality data
   - Geocoding
   - Weather map tiles
   - No API key required

2. **WeatherAPI.com** (Backup)
   - Alternative weather data source
   - Requires free API key

## Setup

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17
- Android SDK (API 24+)

### 1. Clone the Repository

```bash
git clone <repository-url>
cd WeatherApp
```

### 2. Get API Keys

#### Google Maps API Key (Required)

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable **Maps SDK for Android**
4. Go to **Credentials** > **Create Credentials** > **API Key**
5. Restrict the key:
   - Application restrictions: Android apps
   - Package name: `com.weatherapp`
   - SHA-1 certificate fingerprint (get from signing certificate)
6. Copy the API key

#### WeatherAPI.com Key (Optional)

1. Register at [WeatherAPI.com](https://www.weatherapi.com/)
2. Get your free API key from the dashboard

### 3. Configure Local Properties

1. Copy `local.properties.example` to `local.properties`:
   ```bash
   cp local.properties.example local.properties
   ```

2. Edit `local.properties` and add your API keys:
   ```properties
   MAPS_API_KEY=your_google_maps_api_key_here
   WEATHERAPI_KEY=your_weatherapi_key_here
   ```

### 4. Build and Run

1. Open the project in Android Studio
2. Sync the project with Gradle files
3. Run the app on an emulator or physical device

## GitHub Actions Setup

To enable automatic APK building and signing:

### 1. Generate a Keystore

```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
```

### 2. Convert Keystore to Base64

**Linux/Mac:**
```bash
base64 -w 0 my-release-key.jks > keystore_base64.txt
```

**Windows:**
```powershell
certutil -encode my-release-key.jks keystore_base64.txt
```

### 3. Add GitHub Secrets

Go to your repository's **Settings** > **Secrets and variables** > **Actions** and add:

- `KEYSTORE_BASE64`: Content of keystore_base64.txt
- `KEY_ALIAS`: Your keystore alias (e.g., "my-key-alias")
- `KEYSTORE_PASSWORD`: Your keystore password
- `KEY_PASSWORD`: Your key password (usually same as keystore password)
- `MAPS_API_KEY`: Your Google Maps API key
- `WEATHERAPI_KEY`: Your WeatherAPI key (optional)

### 4. Enable GitHub Actions

1. Go to **Actions** tab in your repository
2. Enable GitHub Actions if not already enabled
3. Push to `main` branch to trigger the build

## Project Structure

```
app/
  src/
    main/
      java/com/weatherapp/
        data/          # Data layer
          local/       # Room database
          remote/      # API services
          repository/  # Repository implementations
        domain/        # Domain layer
          model/       # Domain models
          repository/  # Repository interfaces
          usecase/     # Use cases
        di/            # Dependency injection modules
        presentation/  # Presentation layer
          components/  # UI components
          screens/     # UI screens
          theme/       # Theme and styling
          viewmodel/   # ViewModels
      res/            # Android resources
  .github/workflows/ # GitHub Actions
```

## Key Features Implementation

### Weather Map Layers

The app displays weather data on an interactive map with the following layers:
- **Precipitation**: Real-time precipitation data from Open-Meteo tiles
- **Temperature**: Color-coded temperature map
- **Wind**: Wind speed and direction visualization

### Air Quality Integration

- Real-time AQI data from Open-Meteo Air Quality API
- Color-coded AQI categories
- Health recommendations
- Direct links to external air quality websites (aqicn.org)

### Animated Backgrounds

Dynamic backgrounds that respond to weather conditions:
- Clear sky: Subtle cloud animations
- Rain: Falling raindrops
- Snow: Falling snowflakes
- Thunderstorm: Lightning effects
- Cloudy: Moving cloud formations

### Offline Support

- Weather data cached using Room database
- Automatic cache management (old data cleanup)
- Offline mode indicator

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Troubleshooting

### Build Issues

- Ensure you have the latest Android Studio and SDK updates
- Check that all API keys are properly configured in `local.properties`
- Verify your Google Maps API key has the correct restrictions

### Map Issues

- Make sure your Google Maps API key is enabled for **Maps SDK for Android**
- Check that the package name and SHA-1 fingerprint match your key restrictions
- Ensure you have a working internet connection for map tiles

### API Issues

- Open-Meteo API doesn't require a key but may have rate limits
- WeatherAPI.com requires a valid API key if used as backup
- Check network connectivity and API endpoint availability

## Support

For issues and questions:
1. Check the troubleshooting section above
2. Search existing GitHub issues
3. Create a new issue with detailed information about your problem
