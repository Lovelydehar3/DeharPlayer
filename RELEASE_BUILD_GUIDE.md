# DeharPlayer Release Build Guide

## Overview

This document provides comprehensive instructions for building, validating, and releasing DeharPlayer as a production APK. It covers security measures, ProGuard configuration, and final validation steps.

## Prerequisites

- Android Studio 2024.1 or later
- Android SDK API level 36
- Java 17 (JDK 17)
- Gradle 8.10+
- Signing keystore for release builds

## Release Build Configuration

### 1. ProGuard Configuration

DeharPlayer uses ProGuard for code obfuscation and optimization in release builds. The configuration is defined in `app/proguard-rules.pro` and includes:

**Key Settings:**
- **Minification**: Enabled (`isMinifyEnabled = true`)
- **Resource Shrinking**: Enabled (`isShrinkResources = true`)
- **Debuggable**: Disabled (`isDebuggable = false`)
- **Optimization Passes**: 5 passes for maximum optimization

**Coverage:**
- All DeharPlayer core modules and feature modules
- Jetpack Compose and Material3 libraries
- Hilt dependency injection framework
- Kotlin coroutines
- Third-party libraries (Media3, ExoPlayer, Retrofit, Gson, etc.)

### 2. Build Types Configuration

```gradle
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
        isDebuggable = false
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
    debug {
        isMinifyEnabled = false
        isDebuggable = true
    }
}
```

## Building Release APK

### Method 1: Using Android Studio (Recommended)

1. **Open Build Menu**
   - Go to `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`

2. **Wait for Build Completion**
   - Monitor the build progress in the Gradle panel
   - Review any warnings in the event log

3. **Locate Output**
   - Release APK: `app/build/outputs/apk/release/app-release.apk`
   - Bundle: `app/build/outputs/bundle/release/app-release.aab`

### Method 2: Using Command Line

```bash
# Build release APK
./gradlew assembleRelease

# Build release bundle
./gradlew bundleRelease

# Build with verbose output (debugging)
./gradlew assembleRelease --debug

# Clean rebuild
./gradlew clean assembleRelease
```

### Method 3: Using PowerShell (Windows)

```powershell
# Build release APK
.\gradlew.bat assembleRelease

# View build output
Get-ChildItem -Path "app/build/outputs/apk/release/" -Recurse
```

## Pre-Release Validation

### 1. Run Validation Script

**Linux/macOS:**
```bash
chmod +x validate-apk.sh
./validate-apk.sh
```

**Windows PowerShell:**
```powershell
.\validate-apk.ps1
```

**Expected Output:**
```
✓ build.gradle.kts found
✓ ProGuard minification enabled in release build
✓ Debuggable flag set to false for release build
✓ Resource shrinking enabled
✓ proguard-rules.pro found
✓ DeharPlayer core rules included
✓ Jetpack Compose rules included
✓ Hilt DI rules included
...
Passed: 5 / 5 checks
✓ All checks passed! Ready for release build.
```

### 2. Manual Validation Checklist

- [ ] `isMinifyEnabled = true` in release build
- [ ] `isDebuggable = false` in release build
- [ ] `proguard-rules.pro` exists and contains all feature module rules
- [ ] No hardcoded API keys or secrets in source code
- [ ] AndroidManifest.xml has `android:debuggable="false"` or is not present (defaults to false)
- [ ] All 19 feature modules present
- [ ] No compilation errors
- [ ] ProGuard rules are comprehensive (check -keep directives)

### 3. Security Verification

**Check for Hardcoded Secrets:**
```bash
# Linux/macOS
grep -r "API_KEY\|SECRET_KEY\|PRIVATE_KEY" app/src/ --include="*.kt" --include="*.java"

# Windows PowerShell
Get-ChildItem -Path "app/src" -Include "*.kt", "*.java" -Recurse | `
  Select-String "API_KEY|SECRET_KEY|PRIVATE_KEY"
```

**Verify Manifest Security:**
```bash
# Check debuggable flag
grep "android:debuggable" app/src/main/AndroidManifest.xml

# Should NOT match or match false
```

## APK Analysis

### Using Android Studio

1. **Build Analyzer**
   - `Build` → `Analyze APK`
   - Select `app-release.apk`
   - Review:
     - APK size breakdown
     - Method count
     - Asset analysis

2. **Profiler**
   - Launch app in emulator/device
   - `Profiler` tab monitors:
     - CPU usage
     - Memory consumption
     - Network activity
     - Energy usage

### Command-Line Tools

```bash
# Analyze APK size
apktool d app/build/outputs/apk/release/app-release.apk

# Check methods count (requires bundletool)
bundletool dump manifest --bundle=app/build/outputs/bundle/release/app-release.aab

# Get APK size
ls -lh app/build/outputs/apk/release/app-release.apk
```

## Code Obfuscation & Stack Traces

### Reading Obfuscated Stack Traces

ProGuard generates a mapping file during build:
- **Location**: `app/build/outputs/mapping/release/mapping.txt`
- **Purpose**: Maps obfuscated class/method names to original names

**Decoding Stack Traces:**
```bash
# Using retrace tool (included in Android SDK)
$ANDROID_SDK_ROOT/tools/proguard/bin/retrace.sh -verbose mapping.txt crash.txt
```

### Example Mapping File Entry
```
com.dehar.player.feature.musicplayer.viewmodel.MusicPlayerViewModel -> a.b.c.d:
    void onSongSelected(long) -> a
    void updatePlaybackState(PlaybackState) -> b
```

## Signing APK for Release

### Create Keystore (First Time Only)

```bash
# Linux/macOS
keytool -genkey -v -keystore deharplayer.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias deharplayer

# Windows
keytool.exe -genkey -v -keystore deharplayer.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias deharplayer
```

**Prompt Inputs:**
- Password: [secure password]
- First and Last Name: DeharPlayer
- Organizational Unit: Development
- Organization: YourOrg
- City/Locality: Your City
- State/Province: Your State
- Country Code: Your Country Code (US)
- Keystore Password: [same as above]

### Configure Gradle Signing

Create `local.properties`:
```properties
RELEASE_KEYSTORE_PATH=/path/to/deharplayer.keystore
RELEASE_KEYSTORE_PASSWORD=your_keystore_password
RELEASE_KEY_ALIAS=deharplayer
RELEASE_KEY_PASSWORD=your_key_password
```

Update `app/build.gradle.kts`:
```kotlin
signingConfigs {
    create("release") {
        storeFile = file(System.getProperty("RELEASE_KEYSTORE_PATH") ?: "")
        storePassword = System.getProperty("RELEASE_KEYSTORE_PASSWORD") ?: ""
        keyAlias = System.getProperty("RELEASE_KEY_ALIAS") ?: ""
        keyPassword = System.getProperty("RELEASE_KEY_PASSWORD") ?: ""
    }
}

buildTypes {
    release {
        signingConfig = signingConfigs.getByName("release")
        isMinifyEnabled = true
        isShrinkResources = true
        isDebuggable = false
        proguardFiles(...)
    }
}
```

### Sign APK

```bash
# Automatic (with Gradle configuration above)
./gradlew assembleRelease

# Manual signing
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore deharplayer.keystore \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  deharplayer
```

## Feature Modules Validation

All 19 feature modules are included and secured:

1. ✅ **Core Modules** (4)
   - core/ui - UI components and theming
   - core/data - Data layer
   - core/domain - Business logic
   - core/common - Shared utilities

2. ✅ **Feature Modules** (15)
   - Music Library
   - Music Player
   - Video Player
   - Home
   - Lyrics
   - Subtitle
   - Settings
   - Ringtone Editor
   - SMB Browser
   - USB OTG Browser
   - Torrent Streaming
   - Chromecast/Cast Integration
   - Private Vault
   - WhatsApp Status Downloader
   - Video Editor

## ProGuard Rules Summary

### Preserved Classes
- All ViewModels
- All Repositories
- All UI Components and Screens
- All Data Models
- Android Framework classes (Activity, Service, Fragment, etc.)
- Jetpack Compose components
- Hilt DI classes

### Protected Libraries
- Retrofit & Gson (API layer)
- OkHttp (Networking)
- Room Database
- Media3/ExoPlayer
- Cast Framework
- Biometric API

### Optimization Applied
- 5 passes for maximum optimization
- Aggressive class merging
- Field optimization
- Method inlining
- Code shrinking

## Testing Release Build

### Device Testing

```bash
# Install release APK
adb install app/build/outputs/apk/release/app-release.apk

# Clear app data
adb shell pm clear com.dehar.player

# Launch app
adb shell am start -n com.dehar.player/.MainActivity

# Monitor logs
adb logcat | grep "dehar"
```

### Crash Reporting

1. **Enable crash reporting** in app settings
2. **Test crash handling** with intentional crashes
3. **Verify stack traces** decode properly using mapping.txt

## Final Checklist Before Release

- [ ] Build compiles without errors or warnings
- [ ] All validation checks pass
- [ ] ProGuard minification reduces APK by 30-50%
- [ ] Resource shrinking removes unused assets
- [ ] APK is signed and zipaligned
- [ ] App runs on target API levels (24-36)
- [ ] No hardcoded secrets in code
- [ ] Stack traces properly decode with mapping.txt
- [ ] Crash reports are readable
- [ ] All features work in release build
- [ ] ProGuard mapping.txt backed up securely
- [ ] Version code and version name incremented

## Troubleshooting

### ProGuard Warnings

```
Warning: com.example.MyClass: can't find superclass or interface
```

**Solution**: Add to proguard-rules.pro:
```
-dontwarn com.example.MyClass
```

### Compilation Errors After ProGuard

```
NoClassDefFoundError or ClassNotFoundException
```

**Solution**: 
1. Add missing class to -keep rules
2. Check ProGuard mapping file
3. Enable verbose logging: `./gradlew assembleRelease --info`

### APK Too Large

```
APK size > 100MB
```

**Solutions:**
1. Enable code shrinking
2. Compress resources
3. Remove unused dependencies
4. Check APK Analyzer for large assets

## Additional Resources

- [Official ProGuard Documentation](https://www.guardsquare.com/proguard)
- [Android Security & Privacy](https://developer.android.com/guide/topics/security)
- [Jetpack Compose Guide](https://developer.android.com/jetpack/compose)
- [Android Release Preparation](https://developer.android.com/guide/publishing/preparing)

## Support

For issues or questions about the release build process:
1. Check build logs in Android Studio
2. Review ProGuard mapping.txt for name mappings
3. Test with `-dontobfuscate` temporarily to isolate issues
4. Consult official Android documentation

---

**Last Updated**: 2026-05-26
**DeharPlayer Version**: 1.0.0
**Target API Level**: 36
**Minimum API Level**: 24
