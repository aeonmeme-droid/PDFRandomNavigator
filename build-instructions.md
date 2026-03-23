# Build Instructions for PDF Random Navigator

## Quick Start (Recommended)

### Option 1: Android Studio (Easiest)
1. Install Android Studio from https://developer.android.com/studio
2. Open this project in Android Studio
3. Wait for Gradle sync (automatically downloads dependencies)
4. Build → Build Bundle(s) / APK(s) → Build APK(s)
5. APK will be in: `app\build\outputs\apk\debug\app-debug.apk`

### Option 2: Command Line Build
1. Install JDK 8+ from https://adoptium.net/
2. Set JAVA_HOME environment variable
3. Run: `.\gradlew.bat assembleDebug`
4. APK location: `app\build\outputs\apk\debug\app-debug.apk`

## Installation on Android Device

1. Enable "Install from unknown sources" in Android settings
2. Transfer the APK file to your device
3. Tap the APK to install
4. Grant storage permissions when prompted

## Features
- Opens PDF to random page
- Double-tap for random navigation
- Remembers last opened PDF
- Clean Material Design UI

## Troubleshooting
- If build fails, ensure Android SDK is installed
- For permission issues, enable storage access in settings
- Large PDFs may take time to load initially
