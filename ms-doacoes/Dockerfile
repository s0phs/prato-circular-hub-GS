FROM maven:3.9.16-eclipse-temurin-25 AS build
WORKDIR /opt/app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre
LABEL authors="sophia" \
      MAINTAINER="Sophia" \
      application="ms-doacoes" \
      version="1.0"
WORKDIR /opt/app
COPY --from=build /opt/app/target/*.jar /opt/app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]