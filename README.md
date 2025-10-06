# Weather App 🌤️

A modern Android weather application built with Kotlin that provides real-time weather information using the OpenWeatherMap API. The app features location-based weather data, city search functionality, and a clean, intuitive user interface.

## Features

- 🌍 **Current Location Weather**: Automatically fetch weather data for your current location
- 🔍 **City Search**: Search for weather information in any city worldwide
- 📱 **Modern UI**: Clean and intuitive Material Design interface
- 🌡️ **Detailed Weather Info**: Temperature, humidity, wind speed, and weather conditions
- 📍 **Location Services**: GPS-based location detection
- 🔄 **Real-time Updates**: Fresh weather data from OpenWeatherMap API



## Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Networking**: Retrofit2 + OkHttp3
- **Location Services**: Google Play Services Location
- **Image Loading**: Glide
- **Async Operations**: Kotlin Coroutines
- **UI**: View Binding, Material Design Components
- **Lifecycle**: Android Architecture Components (ViewModel, LiveData)

## Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 24 or higher
- OpenWeatherMap API key

## Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/WeatherApp.git
cd WeatherApp
```

### 2. Get OpenWeatherMap API Key

1. Visit [OpenWeatherMap](https://openweathermap.org/api)
2. Sign up for a free account
3. Generate your API key

### 3. Configure API Key

Create a `local.properties` file in the root directory and add your API key:

```properties
OPENWEATHER_API_KEY=your_api_key_here
```

**Important**: Never commit your `local.properties` file to version control. It's already included in `.gitignore`.

### 4. Build and Run

1. Open the project in Android Studio
2. Sync the project with Gradle files
3. Run the app on your device or emulator

## Project Structure

```
app/
├── src/main/java/com/example/weatherapp/
│   ├── data/
│   │   ├── api/           # API interfaces and services
│   │   ├── model/         # Data models
│   │   └── repository/    # Repository pattern implementation
│   ├── ui/
│   │   └── viewmodel/     # ViewModels for UI logic
│   ├── utils/             # Utility classes
│   └── MainActivity.kt    # Main activity
├── src/main/res/          # Resources (layouts, strings, etc.)
└── AndroidManifest.xml    # App configuration
```

## Permissions

The app requires the following permissions:

- `INTERNET` - For API calls to fetch weather data
- `ACCESS_FINE_LOCATION` - For precise location-based weather
- `ACCESS_COARSE_LOCATION` - For approximate location-based weather
- `ACCESS_NETWORK_STATE` - For network connectivity checks

## Dependencies

Key dependencies used in this project:

```kotlin
// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Location Services
implementation("com.google.android.gms:play-services-location:21.0.1")

// Architecture Components
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

// Image Loading
implementation("com.github.bumptech.glide:glide:4.16.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

## API Integration

This app uses the [OpenWeatherMap API](https://openweathermap.org/api) to fetch weather data. The API provides:

- Current weather data
- Weather forecasts
- Weather maps
- Historical weather data

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Security Notes

- API keys are stored in `local.properties` and not committed to version control
- The app uses HTTPS for all API communications
- Location permissions are requested at runtime with proper user consent

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [OpenWeatherMap](https://openweathermap.org/) for providing the weather API
- [Material Design](https://material.io/) for design guidelines
- [Android Developers](https://developer.android.com/) for documentation and best practices

## Support

If you encounter any issues or have questions, please:

1. Check the [Issues](https://github.com/yourusername/WeatherApp/issues) page
2. Create a new issue with detailed information about the problem
3. Include device information, Android version, and steps to reproduce

---

**Note**: Remember to replace `yourusername` with your actual GitHub username in the clone URL and issue links.
