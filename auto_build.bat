@echo off
echo ========================================
echo AUTO APK Builder for PDF Random Navigator
echo ========================================
echo.

echo Step 1: Downloading portable Java JDK...
powershell -Command "& {$ProgressPreference='SilentlyContinue'; try { Invoke-WebRequest -Uri 'https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_windows-x64_bin.zip' -OutFile 'openjdk-17.zip' -UseBasicParsing } catch { Write-Host 'Download failed, trying alternative...'; Invoke-WebRequest -Uri 'https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.2%%2B8/OpenJDK17U-jdk_x64_windows_hotspot_17.0.2_8.zip' -OutFile 'openjdk-17.zip' -UseBasicParsing }}"

if exist openjdk-17.zip (
    echo Step 2: Extracting Java JDK...
    powershell -Command "& {Expand-Archive -Path 'openjdk-17.zip' -DestinationPath '.' -Force}"
    
    echo Step 3: Setting up Java environment...
    for /f "tokens=*" %%i in ('dir /b /ad jdk* 2^>nul') do (
        set JAVA_HOME=%cd%\%%i
        set PATH=%cd%\%%i\bin;%PATH%
        echo JAVA_HOME set to: %JAVA_HOME%
        goto java_ready
    )
    
    :java_ready
    echo Step 4: Verifying Java installation...
    "%JAVA_HOME%\bin\java.exe" -version
    
    echo Step 5: Building APK...
    set JAVA_HOME=%JAVA_HOME%
    set PATH=%JAVA_HOME%\bin;%PATH%
    
    call gradlew.bat assembleDebug
    
    if %errorlevel% equ 0 (
        echo.
        echo ========================================
        echo BUILD SUCCESSFUL! APK IS READY!
        echo ========================================
        echo APK Location: app\build\outputs\apk\debug\app-debug.apk
        echo.
        echo You can now install this APK on your Android device!
        echo File size: 
        dir "app\build\outputs\apk\debug\app-debug.apk"
        pause
    ) else (
        echo.
        echo ========================================
        echo BUILD FAILED - Trying GitHub Method
        echo ========================================
        echo.
        echo Auto-build failed. Use GitHub method instead:
        echo 1. Go to https://github.com
        echo 2. Create repository: PDFRandomNavigator
        echo 3. Upload all files
        echo 4. Run Actions workflow
        echo 5. Download APK in 2-3 minutes
        pause
    )
    
    echo Step 6: Cleaning up...
    del openjdk-17.zip
    
) else (
    echo ERROR: Could not download Java JDK
    echo.
    echo Alternative solutions:
    echo 1. Use GitHub Auto-Build (recommended)
    echo 2. Install Java manually from https://adoptium.net/
    echo 3. Use online Android builder
    pause
)
