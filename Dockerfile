FROM openjdk:17-jdk-slim
COPY build/libs/*.jar /app/myapp.jar
ENV SPRING_PROFILE prod
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "/app/myapp.jar"]