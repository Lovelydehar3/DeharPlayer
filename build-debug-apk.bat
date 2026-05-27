@echo off
setlocal enabledelayedexpansion

cd /d "%~dp0"

echo ========================================
echo DeharPlayer - Building Debug APK
echo ========================================
echo.

REM Clean
echo [*] Cleaning...
call gradlew.bat clean

REM Build
echo [*] Building Debug APK...
call gradlew.bat assembleDebug

if %ERRORLEVEL% equ 0 (
    echo.
    echo ========================================
    echo [SUCCESS] Build completed!
    echo ========================================
    echo.
    if exist app\build\outputs\apk\debug\app-debug.apk (
        echo APK Location:
        dir app\build\outputs\apk\debug\app-debug.apk
    )
) else (
    echo.
    echo ========================================
    echo [FAILED] Build failed with error %ERRORLEVEL%
    echo ========================================
    echo.
    echo Run this for more details:
    echo   gradlew.bat assembleDebug --stacktrace
)

pause
