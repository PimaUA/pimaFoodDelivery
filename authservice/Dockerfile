FROM openjdk:17-jdk-slim

LABEL "org.opencontainers.image.authors"="pima.com"

RUN apt-get update && apt-get install -y curl

WORKDIR /app

COPY target/authservice-0.0.1-SNAPSHOT.jar authservice.jar

ENTRYPOINT ["java","-jar","authservice.jar"]