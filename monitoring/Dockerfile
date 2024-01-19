FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/monitoring-0.0.1-SNAPSHOT.jar monitoring-app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/monitoring-app.jar"]