<p align="center">
  <h1 align="center">Everything</h1>
  <p align="center">
    <strong>A personal app for things I need that don't exist elsewhere</strong>
  </p>
  <p align="center">
    <img src="https://img.shields.io/badge/Kotlin-2.0-purple?logo=kotlin" alt="Kotlin">
    <img src="https://img.shields.io/badge/Jetpack%20Compose-Latest-green?logo=jetpackcompose" alt="Compose">
    <img src="https://img.shields.io/badge/Material%203-Expressive-blue?logo=materialdesign" alt="Material 3">
    <img src="https://img.shields.io/badge/Min%20SDK-33-orange" alt="Min SDK">
  </p>
</p>

---

## What is this?

This is a personal project where I build the apps and tools I need that either don't exist or aren't quite right for my use case. Instead of downloading 10 different apps, I'm putting everything I need into one place.

**Fair warning:** This might be completely useless to most people. It's built for my specific needs and workflow. But if something here happens to be useful to you, feel free to use it.

I'll keep adding modules as I need them.

---

## Current Modules

### Work Tracker
A simple time tracking tool for logging work hours.

- Walk In/Out with a single tap
- Calendar view to browse logs by date
- Weekly progress visualization
- Monthly, yearly, and custom range analytics
- Goal tracking (40-hour weeks)

*More modules coming as I need them.*

---

## Material 3 Expressive

Built with the latest Material Design 3 Expressive for smooth, modern UI:

| Component | Description |
|-----------|-------------|
| Shape Morphing Buttons | Buttons that animate their shape on press |
| LargeFlexibleTopAppBar | Collapsible app bar with title and subtitle |
| LinearWavyProgressIndicator | Wavy animated progress bars |
| ButtonGroup | Connected toggle buttons for selection |
| FilledTonalIconButton | Icon buttons with shape morphing |
| Spring Animations | Physics-based motion throughout |

---

## Architecture

```
app/
├── data/
│   ├── AppDatabase.kt
│   ├── WorkLog.kt
│   └── WorkLogDao.kt
├── ui/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── worktracker/
│       ├── WorkTrackerScreen.kt
│       ├── InsightsScreen.kt
│       └── WorkTrackerViewModel.kt
└── MainActivity.kt
```

---

## Tech Stack

- Kotlin 2.0
- Jetpack Compose
- Material 3 Expressive
- Room Database
- MVVM with StateFlow
- Jetpack Navigation

---

## Build

```bash
# Debug
./gradlew assembleDebug

# Release (optimized, no lag)
./gradlew assembleRelease

# Install
adb install app/build/outputs/apk/release/app-release.apk
```

---

## Requirements

- Android Studio Ladybug+
- JDK 11+
- Android SDK 36
- Min SDK 33

---

## Documentation

- [components.md](components.md) - Material 3 Expressive components reference
- [material-3-expressive-guide.md](material-3-expressive-guide.md) - Full M3 Expressive guide

---

## License

Open source under the [MIT License](LICENSE).

---

## Author

**Ivor**

*Building the apps I wish existed.*
