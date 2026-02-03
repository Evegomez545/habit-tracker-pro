# ---------- build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# copia o pom.xml que está dentro da pasta habit-tracker
COPY habit-tracker/pom.xml .
RUN mvn dependency:go-offline

# copia o src que está dentro da pasta habit-tracker
COPY habit-tracker/src ./src
RUN mvn clean package -DskipTests

# ---------- run ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
