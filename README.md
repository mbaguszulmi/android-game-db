# Games For You

Android App that provides Game information according to RAWG API. Developed with Bottom - Up approach.
Tested with unit test and instrumented test. Build with the best practice of Android Architecture component.

Techs:
- MVVM (Android Architecture Component)
- Data Binding
- Flow
- Hilt
- Glide
- Retrofit2
- Datastore
- Timber
- Room
- GoogleMaps
- Mockito

## Features

- Get the list of available games
- Search games
- View the game details
- Add a game to favorite
- Remove a game from favorite

## Run App

1. Open the project in Android Studio
2. Build gradle
3. Connect android device
4. Run the project

## Unit Testing

You can perform unit test directly from Android Studio. Or you can run this command to test all unit testing

```
./gradlew test
```

## Instrumentation Testing

Android device connection is needed for this test. You can perform instrumented test directly from Android Studio. 
Or you can run this command to test all instrumented testing

```
./gradlew connectedAndroidTest
```
