FROM amazoncorretto:21
WORKDIR /app

# 컨텍스트 기준 상대 경로
COPY build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Duser.timezone=GMT+9", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
