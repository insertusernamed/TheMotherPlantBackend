FROM maven:3.9.7-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean install -DskipTests
FROM eclipse-temurin:21-jdk-jammy
COPY --from=build /target/themotherplant-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]