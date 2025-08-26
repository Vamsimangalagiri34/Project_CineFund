@echo off
echo Checking CineFund Services Status...
echo.

echo Checking User Service (Port 8084)...
curl -s -o nul -w "%%{http_code}" http://localhost:8084/api/users > nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ User Service is running on port 8084
) else (
    echo ✗ User Service is NOT running on port 8084
)

echo.
echo Checking Movie Service (Port 8082)...
curl -s -o nul -w "%%{http_code}" http://localhost:8082/api/movies > nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Movie Service is running on port 8082
) else (
    echo ✗ Movie Service is NOT running on port 8082
)

echo.
echo Checking Funding Service (Port 8083)...
curl -s -o nul -w "%%{http_code}" http://localhost:8083/api/funding > nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Funding Service is running on port 8083
) else (
    echo ✗ Funding Service is NOT running on port 8083
)

echo.
echo Checking API Gateway (Port 8090)...
curl -s -o nul -w "%%{http_code}" http://localhost:8090 > nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ API Gateway is running on port 8090
) else (
    echo ✗ API Gateway is NOT running on port 8090
)

echo.
echo Testing API Gateway routing...
curl -s -o nul -w "%%{http_code}" http://localhost:8090/api/users > nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ API Gateway routing is working
) else (
    echo ✗ API Gateway routing is NOT working
)

echo.
echo Service check complete!
pause
