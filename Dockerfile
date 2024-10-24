FROM openjdk:17-jdk-slim
#FROM docker.repo.splunkdev.net/observability/signalfx-base/alp-3.16-corretto21:1.11.49

WORKDIR /app

# Download the Splunk OpenTelemetry Java agent
ADD https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent.jar /app/splunk-otel-javaagent.jar

# Copy the built JAR file from the previous stage
COPY --from=build /app/build/libs/*.jar /app/coral-demo-app.jar

# Expose the port the application runs on (e.g., 8080)
EXPOSE 8080

# Run the application with the Splunk OpenTelemetry Java agent
ENTRYPOINT ["java", "-javaagent:/app/splunk-otel-javaagent.jar", "-jar", "./coral-demo-app.jar"]
