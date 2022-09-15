FROM eclipse-temurin:11-jre-jammy

WORKDIR /app

COPY app/build/libs/app*SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
