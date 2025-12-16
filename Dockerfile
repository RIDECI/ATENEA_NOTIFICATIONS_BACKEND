# Etapa de construcción (Build Stage)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compilamos el proyecto omitiendo los tests para agilizar la construcción en contenedores
RUN mvn clean package -DskipTests

# Etapa de ejecución (Runtime Stage)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiamos el JAR generado en la etapa anterior
# Nota: El nombre del jar depende del artifactId y version en pom.xml
COPY --from=build /app/target/ATENEA_NOTIFICATIONS_BACKEND-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080 (puerto por defecto de Spring Boot)
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
