#Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

#Information around who maintains the image
LABEL "org.opencontainers.image.authors"="pima.com"

# Install curl for health checks (optional)
RUN apt-get update && apt-get install -y curl

# Set working directory
WORKDIR /app

#Add the application's jar to the image
COPY target/coreservice-0.0.1-SNAPSHOT.jar coreservice.jar

#execute the application when creating container
ENTRYPOINT ["java","-jar","coreservice.jar"]