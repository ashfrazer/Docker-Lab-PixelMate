FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/PixelMate.jar /app/PixelMate.jar

EXPOSE 5555:5555

CMD ["java", "-jar", "/app/PixelMate.jar"]