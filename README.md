# PDF Random Navigator

An Android utility that opens PDF files to random pages with double-click navigation.

## Features

- **Random Page Navigation**: Opens PDF files to a random page
- **Double-Click Navigation**: Double-tap anywhere on the PDF to jump to another random page
- **Recent File Persistence**: Automatically reopens the most recently used PDF file when launched
- **File Picker**: Easy selection of PDF files from device storage
- **Page Information**: Displays current page number and total pages

## How to Use

1. **Open the App**: Launch the PDF Random Navigator
2. **Select PDF**: Tap "Select PDF File" to choose a PDF from your device
3. **Random Navigation**: 
   - The app automatically opens to a random page
   - Double-tap anywhere on the PDF to jump to another random page
   - Use the "Random Page" button for manual random navigation
4. **Automatic Reopen**: The next time you launch the app, it will automatically open the last PDF you used

## Technical Details

- **Minimum SDK**: Android 5.0 (API 21)
- **Target SDK**: Android 14 (API 34)
- **PDF Library**: Android PDF Viewer (barteksc/android-pdf-viewer)
- **Language**: Kotlin
- **Architecture**: Single Activity with ViewBinding

## Permissions

- `READ_EXTERNAL_STORAGE`: Required to access PDF files on device storage
- `MANAGE_EXTERNAL_STORAGE`: Required for broader file access on newer Android versions

## Installation

1. Clone this repository
2. Open in Android Studio
3. Build and run on an Android device or emulator

## File Structure

```
app/src/main/
├── java/com/example/pdfrandomnavigator/
│   └── MainActivity.kt                 # Main activity with PDF functionality
├── res/
│   ├── layout/
│   │   └── activity_main.xml          # Main UI layout
│   ├── values/
│   │   ├── strings.xml                # App strings
│   │   ├── colors.xml                 # Color definitions
│   │   └── themes.xml                 # App theme
│   └── xml/
│       ├── backup_rules.xml           # Backup rules
│       └── data_extraction_rules.xml  # Data extraction rules
└── AndroidManifest.xml                # App manifest
```

## Key Components

### MainActivity.kt
- Handles PDF loading and display
- Implements double-click detection for random page jumps
- Manages SharedPreferences for recent file persistence
- Provides file picker integration

### Random Navigation Logic
- Uses `java.util.Random` for page selection
- Handles edge cases (single-page PDFs)
- Smooth page transitions with visual feedback

### File Persistence
- Stores last used PDF URI in SharedPreferences
- Automatically loads recent file on app launch
- Handles invalid URIs gracefully

## License

This project is open source and available under the MIT License.
