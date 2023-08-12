FROM eclipse-temurin:17

WORKDIR /app

COPY target/swapi-proxy-0.0.1-SNAPSHOT.jar swapi-proxy-0.0.1.jar

CMD ["java", "-jar", "swapi-proxy-0.0.1.jar"]