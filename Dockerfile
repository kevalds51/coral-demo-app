# Stage 1: Build the application
FROM gradle:7.4.2-jdk17 AS build
WORKDIR /app
COPY . /app
RUN gradle clean build

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
#FROM docker.repo.splunkdev.net/observability/signalfx-base/alp-3.16-corretto21:1.11.49

WORKDIR /app

# Environment variable to choose the agent: 'splunk' or 'appdynamics'
# ENV AGENT_TYPE=splunk

# Download the Splunk OTEL agent
ADD https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent.jar /app/splunk-otel-javaagent.jar

# Copy the built application JAR from the build stage
COPY --from=build /app/build/libs/*.jar /app/myapp.jar

# Expose the port the application runs on (example: 8080)
EXPOSE 8080

# Conditional entrypoint script based on the AGENT_TYPE environment variable
ENTRYPOINT ["/bin/sh", "-c", "\
  if [ \"$AGENT_TYPE\" = 'splunk' ]; then \
    java -javaagent:/app/splunk-otel-javaagent.jar -jar /app/myapp.jar; \
  elif [ \"$AGENT_TYPE\" = 'appdynamics' ]; then \
    java -javaagent:/app/appd-javaagent/javaagent.jar -jar /app/myapp.jar; \
  else \
    java -jar /app/myapp.jar; \
  fi"]
