# ---------- BUILD (compila o projeto) ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o arquivo pom.xml da pasta habit-tracker para dentro do container
COPY habit-tracker/pom.xml ./pom.xml

# Baixa as dependências
RUN mvn dependency:go-offline

# Copia o código da pasta habit-tracker
COPY habit-tracker/src ./src

# Compila sem rodar os testes
RUN mvn clean package -DskipTests

# ---------- RUN (executa o projeto) ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia o .jar compilado
COPY --from=build /app/target/*.jar app.jar

# Porta que a aplicação vai usar
EXPOSE 8080

# Comando para iniciar
ENTRYPOINT ["java","-jar","app.jar"]
