FROM openjdk:17-jdk-slim

LABEL "org.opencontainers.image.authors"="pima.com"

RUN apt-get update && apt-get install -y curl

WORKDIR /app

COPY target/paymentservice-0.0.1-SNAPSHOT.jar paymentservice.jar

ENTRYPOINT ["java","-jar","paymentservice.jar"]