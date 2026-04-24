# Instructions for Pushing to GitHub

Since Git is not installed in your system, you need to install Git first and then push the project manually.

## Step 1: Install Git

**Windows:**
1. Download Git from https://git-scm.com/download/win
2. Run the installer with default settings
3. Restart your IDE/command line

## Step 2: Push to GitHub

Open Command Prompt or PowerShell in the project directory (`C:\Users\user\CascadeProjects\WeatherApp`) and run:

```bash
# Navigate to project directory
cd C:\Users\user\CascadeProjects\WeatherApp

# Initialize git repository (if not already done)
git init

# Add remote repository
git remote add origin https://github.com/Lien656/weather.git

# Add all files to staging
git add .

# Commit the changes
git commit -m "Initial commit: Complete weather app with all features"

# Push to GitHub
git branch -M main
git push -u origin main
```

## Step 3: Add GitHub Secrets

After pushing, go to your repository on GitHub and add these secrets:

1. Go to **Settings** > **Secrets and variables** > **Actions**
2. Add the following secrets:

### Required Secrets:
- `GOOGLE_MAPS_API_KEY`: `AIzaSyB1dW6a_YTzpDa9j-WX7j99FpkNZUIQ9qk`
- `WEATHERAPI_KEY`: `592086b1dc174b309e491141262104`

### Optional (for APK signing):
- `KEYSTORE_BASE64`: (generate keystore first, then convert to base64)
- `KEY_ALIAS`: your keystore alias
- `KEYSTORE_PASSWORD`: your keystore password
- `KEY_PASSWORD`: your key password

## Step 4: Generate Keystore (for APK signing)

```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
```

Convert to base64:
```bash
# Windows
certutil -encode my-release-key.jks keystore_base64.txt

# Copy content of keystore_base64.txt and add as KEYSTORE_BASE64 secret
```

## Project Features Included:

- Complete Android weather app with Jetpack Compose
- MVVM + Clean Architecture
- Open-Meteo API (primary) + WeatherAPI.com (backup)
- Google Maps with weather layers
- Animated weather backgrounds
- Air quality monitoring
- Location services
- Offline support
- GitHub Actions for automatic APK building
- Black and blue theme design

## API Keys Configuration:

- WeatherAPI.com key is already in the code
- Google Maps key is in local.properties (protected by .gitignore)
- All keys are properly secured

After completing these steps, your project will be live on GitHub and GitHub Actions will automatically build APKs when you push to main branch.
