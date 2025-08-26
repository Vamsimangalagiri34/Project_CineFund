@echo off
echo Restarting Funding Service...
echo.

echo Starting Funding Service (Port 8083)...
start "Funding Service" cmd /k "cd funding-service && mvn spring-boot:run"

echo.
echo Funding Service is starting up...
echo Check the terminal window for startup progress.
echo Service will be available at: http://localhost:8083
pause
