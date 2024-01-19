FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/device-0.0.1-SNAPSHOT.jar device-app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/device-app.jar"]