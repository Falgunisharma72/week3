# Container image for IssueService (Milestone 2).
# Build the JAR first:   mvn -DskipTests package
# Build the image:       docker build -t its-issue-service .
# Run it locally:        docker run -p 8083:8083 <image>
# Database / Eureka / peer URLs are overridden with environment variables
# (SPRING_DATASOURCE_URL, EUREKA_CLIENT_SERVICEURL_DEFAULTZONE, ...) - see the ECS task definitions.
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY target/*.jar app.jar
USER app
EXPOSE 8083
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
