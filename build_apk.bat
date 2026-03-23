@echo off
echo ========================================
echo PDF Random Navigator APK Builder
echo ========================================
echo.

echo Checking for Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java not found. Please install Java JDK first.
    echo Download from: https://adoptium.net/temurin/releases/
    pause
    exit /b 1
)

echo Java found!
echo.

echo Setting JAVA_HOME...
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.x.x-hotspot
if not exist "%JAVA_HOME%" (
    echo Trying alternative Java paths...
    for /f "tokens=*" %%i in ('dir /b /ad "C:\Program Files\Eclipse Adoptium\jdk-*" 2^>nul') do (
        set JAVA_HOME=C:\Program Files\Eclipse Adoptium\%%i
        if exist "%JAVA_HOME%" goto found_java
    )
    for /f "tokens=*" %%i in ('dir /b /ad "C:\Program Files\Java\jdk-*" 2^>nul') do (
        set JAVA_HOME=C:\Program Files\Java\%%i
        if exist "%JAVA_HOME%" goto found_java
    )
    echo ERROR: Could not find Java installation
    pause
    exit /b 1
)

:found_java
echo Using JAVA_HOME: %JAVA_HOME%
echo.

echo Building APK...
call gradlew.bat assembleDebug

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo BUILD SUCCESSFUL!
    echo ========================================
    echo APK Location: app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo You can now install this APK on your Android device!
    pause
) else (
    echo.
    echo ========================================
    echo BUILD FAILED
    echo ========================================
    echo Please check the error messages above.
    pause
)
