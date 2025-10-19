FROM openjdk:21-jdk-slim

WORKDIR /app
COPY build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-Djava.security.egd=file:/dev/urandom", "-jar", "app.jar"]
