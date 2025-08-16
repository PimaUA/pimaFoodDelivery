#Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

#Information around who maintains the image
LABEL "org.opencontainers.image.authors"="pima.com"

# Install curl for health checks (optional)
RUN apt-get update && apt-get install -y curl

#Add the application's jar to the image
COPY target/coreService-0.0.1-SNAPSHOT.jar coreService.jar

#execute the application when creating container
ENTRYPOINT ["java","-jar","coreService.jar"]