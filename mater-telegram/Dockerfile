# Используем официальный образ OpenJDK в качестве базового
FROM openjdk:21-jdk

# Определяем рабочую директорию в контейнере
WORKDIR /app

# Копируем jar файл приложения в контейнер
COPY target/mater-telegram-0.0.1-SNAPSHOT.jar /app/mater-telegram.jar

# Указываем команду для запуска Spring Boot приложения
ENTRYPOINT ["java", "-jar", "/app/mater-telegram.jar"]

# Указываем порт, на котором будет работать приложение
EXPOSE 8080