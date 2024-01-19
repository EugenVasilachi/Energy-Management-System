FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/user-0.0.1-SNAPSHOT.jar user-app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/user-app.jar"]