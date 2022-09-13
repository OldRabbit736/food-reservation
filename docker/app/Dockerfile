FROM eclipse-temurin:11-jre-jammy

WORKDIR /app

COPY app/build/libs/app-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
