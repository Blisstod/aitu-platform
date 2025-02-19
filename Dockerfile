# ЭТАП 1: Сборка Gradle
FROM gradle:7.6-jdk17 AS build
WORKDIR /app

# Копируем файлы Gradle и остальные исходники
COPY . .

# Собираем проект при помощи Gradle
RUN gradle clean build -x test --no-daemon

# ЭТАП 2: Запуск приложения
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Копируем скомпилированный jar из контейнера build
COPY --from=build /app/build/libs/*.jar app.jar

# Пробрасываем порт (если нужно)
EXPOSE 8080

# Запускаем Spring Boot-приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
