@echo off
setlocal enabledelayedexpansion

cd /d "%~dp0"

echo ========================================
echo DeharPlayer Build Diagnostics
echo ========================================
echo.

echo [1] Checking Gradle installation...
where gradlew.bat >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo [OK] Gradle wrapper found
) else (
    echo [FAIL] Gradle wrapper not found
    exit /b 1
)

echo.
echo [2] Checking Android SDK...
if defined ANDROID_HOME (
    echo [OK] ANDROID_HOME = %ANDROID_HOME%
) else (
    echo [WARN] ANDROID_HOME not set, checking local.properties...
    if exist local.properties (
        echo [OK] local.properties exists
    )
)

echo.
echo [3] Running gradle tasks...
call gradlew.bat tasks --all 2>&1 | findstr /C:"Build tasks" /C:"ERROR" /C:"error" /C:"FAILURE"

echo.
echo [4] Attempting to build debug APK with verbose output...
echo.
call gradlew.bat assembleDebug --info 2>&1 | tee build.log

echo.
echo [5] Build completed. Check build.log for full output.
echo.
if exist app\build\outputs\apk\debug\app-debug.apk (
    echo [SUCCESS] Debug APK created!
    dir app\build\outputs\apk\debug\app-debug.apk
) else (
    echo [FAILED] Debug APK not found. Check build.log for errors.
)

pause
