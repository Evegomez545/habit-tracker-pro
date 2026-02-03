# ---------- BUILD ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o pom.xml do projeto
COPY habit-tracker/pom.xml ./pom.xml

# Baixa dependências primeiro (cache eficiente)
RUN mvn dependency:go-offline

# Copia o código fonte
COPY habit-tracker/src ./src

# Build do projeto
RUN mvn clean package -DskipTests

# ---------- RUNTIME ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia o JAR gerado na fase de build
COPY --from=build /app/target/*.jar app.jar

# Porta que o app vai escutar (no Render é a que ele injeta)
EXPOSE 8080

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
