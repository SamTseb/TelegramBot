# Используем базовый образ OpenJDK
FROM openjdk:17-jdk-slim

# Указываем рабочую директорию
WORKDIR /app

# Копируем JAR-файл приложения
COPY target/my-spring-boot-app.jar app.jar

# Указываем порт, который приложение слушает
EXPOSE 8080

# Команда запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
