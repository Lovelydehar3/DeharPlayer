@echo off
setlocal enabledelayedexpansion

cd /d "%~dp0"

echo ============================================
echo DeharPlayer APK Build Script
echo ============================================
echo.

set KEYSTORE_PATH=%CD%\deharplayer.keystore
set KEYSTORE_PASSWORD=deharplayer123
set KEY_ALIAS=deharplayer
set KEY_PASSWORD=deharplayer123

REM Create keystore if it doesn't exist
if not exist "%KEYSTORE_PATH%" (
    echo [*] Creating keystore...
    echo.
    keytool -genkey -v -keystore "%KEYSTORE_PATH%" -keyalg RSA -keysize 2048 -validity 10000 -alias %KEY_ALIAS% -storepass %KEYSTORE_PASSWORD% -keypass %KEY_PASSWORD% -dname "CN=DeharPlayer,OU=Development,O=DeharPlayer,L=India,S=India,C=IN"
    echo.
)

REM Clean previous builds
echo [*] Cleaning...
call gradlew.bat clean >nul 2>&1

REM Build debug APK
echo [*] Building debug APK...
call gradlew.bat assembleDebug
if %ERRORLEVEL% equ 0 (
    echo [OK] Debug APK built: app\build\outputs\apk\debug\app-debug.apk
) else (
    echo [ERROR] Debug build failed!
    exit /b 1
)

echo.
echo [*] Building release APK...
call gradlew.bat assembleRelease
if %ERRORLEVEL% equ 0 (
    echo [OK] Release APK built: app\build\outputs\apk\release\app-release.apk
) else (
    echo [ERROR] Release build failed!
    exit /b 1
)

echo.
echo [SUCCESS] Build completed!
