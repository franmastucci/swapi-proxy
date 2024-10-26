# Etapa 1: Construcción
FROM maven:3.8.1-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Construcción de la Imagen Final
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=builder /app/target/swapi-proxy-0.0.1-SNAPSHOT.jar swapi-proxy-0.0.1.jar

CMD ["java", "-jar", "swapi-proxy-0.0.1.jar"]