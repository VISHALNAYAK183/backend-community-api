# Use a base image with JDK and Maven to build the app
FROM maven:3.8.7-eclipse-temurin-17 AS build


# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the code and build the app
COPY . .
RUN mvn clean package -DskipTests

# Use JDK base image to run the built app
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
CMD ["java", "-jar", "app.jar"]
