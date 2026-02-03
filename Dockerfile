# ---------- BUILD ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY habit-tracker/pom.xml ./pom.xml
RUN mvn dependency:go-offline

COPY habit-tracker/src ./src
RUN mvn clean package -DskipTests

# ---------- RUNTIME ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
