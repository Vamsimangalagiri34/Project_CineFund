@echo off
echo Starting CineFund Microservices...
echo.

echo Starting User Service (Port 8084)...
start "User Service" cmd /k "cd user-service && mvn spring-boot:run"

echo Waiting 10 seconds before starting next service...
timeout /t 10 /nobreak >nul

echo Starting Movie Service (Port 8082)...
start "Movie Service" cmd /k "cd movie-service && mvn spring-boot:run"

echo Waiting 10 seconds before starting next service...
timeout /t 10 /nobreak >nul

echo Starting Funding Service (Port 8083)...
start "Funding Service" cmd /k "cd funding-service && mvn spring-boot:run"

echo Waiting 10 seconds before starting next service...
timeout /t 10 /nobreak >nul

echo Starting API Gateway (Port 8080)...
start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run"

echo.
echo All services are starting up...
echo Check the individual terminal windows for startup progress.
echo.
echo Services will be available at:
echo - API Gateway: http://localhost:8080
echo - User Service: http://localhost:8081
echo - Movie Service: http://localhost:8082
echo - Funding Service: http://localhost:8083
echo.
echo Frontend: Open frontend/index.html in your browser
pause
