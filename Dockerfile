FROM amazoncorretto:21
WORKDIR /app

COPY /app/build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Duser.timezone=GMT+9", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]