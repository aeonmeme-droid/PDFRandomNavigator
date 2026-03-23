@echo off
echo ========================================
echo Installing Java JDK (Portable)
echo ========================================
echo.

echo Downloading portable Java JDK...
echo This may take a few minutes...

powershell -Command "& {Invoke-WebRequest -Uri 'https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.12%%2B7/OpenJDK17U-jdk_x64_windows_hotspot_17.0.12_7.zip' -OutFile 'jdk17.zip'}"

if %errorlevel% equ 0 (
    echo Extracting Java JDK...
    powershell -Command "& {Expand-Archive -Path 'jdk17.zip' -DestinationPath '.' -Force}"
    
    echo Setting up environment...
    for /f "tokens=*" %%i in ('dir /b /ad jdk* 2^>nul') do (
        set JAVA_HOME=%cd%\%%i
        set PATH=%cd%\%%i\bin;%PATH%
        echo JAVA_HOME set to: %JAVA_HOME%
    )
    
    echo Cleaning up...
    del jdk17.zip
    
    echo.
    echo ========================================
    echo Java installation complete!
    echo ========================================
    echo Now you can build the APK.
    pause
) else (
    echo ERROR: Failed to download Java
    echo Please download manually from: https://adoptium.net/temurin/releases/
    pause
)
