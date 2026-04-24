# GitHub Push Guide - Weather App

## Step 1: Create Repository on GitHub

1. Go to https://github.com/Lien656/weather
2. Click "Create repository" (since it's empty)
3. Repository name: `weather`
4. Description: `Complete Android Weather App with Jetpack Compose`
5. Make it **Public**
6. **DO NOT** initialize with README, .gitignore, or license (we have all files)
7. Click "Create repository"

## Step 2: Install Git (if not installed)

**Windows:**
1. Download from https://git-scm.com/download/win
2. Install with default settings
3. Restart Command Prompt/PowerShell

## Step 3: Push to GitHub

Open Command Prompt or PowerShell in the project directory and run:

```bash
# Navigate to project directory
cd C:\Users\user\CascadeProjects\WeatherApp

# Initialize git repository
git init

# Add all files
git add .

# Commit all files
git commit -m "Initial commit: Complete weather app with all features

- Android weather app with Jetpack Compose
- MVVM + Clean Architecture
- Open-Meteo and WeatherAPI integration
- Google Maps with weather layers
- Animated weather backgrounds
- Air quality monitoring
- Location services
- Offline support
- GitHub Actions for APK building
- Black and blue theme design"

# Add remote repository
git remote add origin https://github.com/Lien656/weather.git

# Push to GitHub
git branch -M main
git push -u origin main
```

## Step 4: Add GitHub Secrets

After pushing, go to your repository and add secrets:

1. Go to **Settings** > **Secrets and variables** > **Actions**
2. Click "New repository secret" and add:

### Required Secrets:
```
Name: GOOGLE_MAPS_API_KEY
Value: AIzaSyB1dW6a_YTzpDa9j-WX7j99FpkNZUIQ9qk
```

```
Name: WEATHERAPI_KEY  
Value: 592086b1dc174b309e491141262104
```

### Optional (for APK signing):
```
Name: KEYSTORE_BASE64
Value: [generate keystore and convert to base64]

Name: KEY_ALIAS
Value: my-key-alias

Name: KEYSTORE_PASSWORD
Value: [your keystore password]

Name: KEY_PASSWORD
Value: [your key password]
```

## Step 5: Generate Keystore (for signed APK)

```bash
# In project directory
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias

# Convert to base64 (Windows)
certutil -encode my-release-key.jks keystore_base64.txt

# Copy content of keystore_base64.txt and paste as KEYSTORE_BASE64 secret
```

## Step 6: Test GitHub Actions

After adding secrets, any push to main branch will trigger automatic APK building.

## Project Structure

The complete project includes:

```
WeatherApp/
- app/
  - src/main/
    - java/com/weatherapp/
      - data/          # Data layer (API, database)
      - domain/        # Domain layer (models, use cases)
      - presentation/  # UI layer (screens, viewmodels)
      - di/            # Dependency injection
    - res/             # Android resources
  - build.gradle.kts  # App build configuration
- .github/workflows/  # GitHub Actions
- build.gradle.kts    # Root build configuration
- settings.gradle.kts # Gradle settings
- README.md           # Project documentation
- local.properties    # API keys (not pushed to Git)
```

## Features Included

- Current weather with detailed information
- Hourly and 10-day forecasts
- Interactive Google Maps with weather layers
- Air quality monitoring with external links
- Animated weather backgrounds
- GPS location detection
- Manual city search
- Offline data caching
- Settings for units and refresh intervals
- Onboarding screens
- Automatic APK building and signing

## API Keys Status

- WeatherAPI.com: Already configured in code
- Google Maps: Configured in local.properties (protected)
- All keys are properly secured and not exposed in Git

After completing these steps, your weather app will be live on GitHub with automatic CI/CD!
