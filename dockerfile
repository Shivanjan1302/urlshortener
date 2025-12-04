# ============================
#   BUILD STAGE
# ============================
FROM maven:3.8.7-openjdk-17 AS build
WORKDIR /app

# Copy pom.xml and download deps first (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Package the Spring Boot jar
RUN mvn clean package -DskipTests

# ============================
#   RUNTIME STAGE
# ============================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy built jar
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
