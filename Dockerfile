FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install -y openjdk-21-jdk maven

COPY . .

RUN mvn clean install -DskipTests

FROM openjdk:21-jdk-slim

EXPOSE 8080

COPY --from=build /target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]