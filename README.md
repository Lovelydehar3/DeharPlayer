# Dehar Player

Dehar Player is a clean Android video player built with Kotlin, Jetpack Compose, and Android Media3 ExoPlayer. It is an original project; it does not contain copied code or assets from MX Player, modded APKs, or any third-party closed-source player.

## App Behavior

- No `INTERNET` permission.
- No ads, trackers, telemetry, OTP, login, or forced update system.
- No cloud backup for app preferences.
- Local storage permission is used only to find and play videos already on the device.
- Optional 4-digit passcode is stored only on the device as a one-way SHA-256 hash.

## Features

- Folder-based local video library from Android MediaStore.
- Dark player UI with play/pause, previous/next, seek bar, screen lock, aspect ratio, and playback speed.
- Double-tap left/right to seek 10 seconds.
- Horizontal swipe to seek.
- Left vertical swipe for brightness and right vertical swipe for volume.
- Playback resume for each video.
- Automatic subtitle discovery for matching subtitle files in the same folder.
- Opens `video/*` files shared from Android file managers.

## Project Status

- Debug APK builds successfully.
- Gradle wrapper is included for GitHub/CI builds.
- GitHub Actions workflow is included at `.github/workflows/android.yml`.
- Generated files, APK outputs, signing keys, local SDK paths, and build caches are ignored by Git.

## Build

```powershell
.\gradlew.bat assembleDebug
```

The APK is created at:

```text
app\build\outputs\apk\debug\app-debug.apk
```

For release publishing, create your own keystore and signing config locally. Do not commit keystores, passwords, generated APKs, or the reference `MX Player Pro.apk`.
