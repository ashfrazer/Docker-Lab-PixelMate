@echo off
echo Starting first-time setup...
echo.
echo Installing OCSF jar...
call mvn install:install-file -Dfile=ocsf.jar -DgroupId=ocsf -DartifactId=ocsf -Dversion=1.0 -Dpackaging=jar
echo.
echo Building project...
echo.
call mvnw clean install -DskipTests
echo.
echo Starting Docker container...
docker-compose up -d --build
echo.
echo Setup complete! Let's play!
echo.
pause