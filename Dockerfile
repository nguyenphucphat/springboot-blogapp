FROM eclipse-temurin:17

LABEL maintainer="nguyenphucphat111999@gmail.com"

WORKDIR /app

COPY target/springboot-blog-application-rest-api-0.0.1-SNAPSHOT.jar /app/springboot-blog-app-docker.jar

ENTRYPOINT ["java", "-jar", "springboot-blog-app-docker.jar"]