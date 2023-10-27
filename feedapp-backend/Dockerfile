FROM maven:3.8.3-openjdk-17-slim AS build

WORKDIR /feedapp-backend

COPY pom.xml ./

RUN mvn dependency:go-offline -B

COPY src src/

RUN mvn package && ls -la /feedapp-backend/target

FROM openjdk:17-jdk-slim

WORKDIR /feedapp-backend

COPY --from=build /feedapp-backend/target/feedapp-0.0.1-SNAPSHOT.jar /feedapp-backend/feedapp-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "/feedapp-backend/feedapp-0.0.1-SNAPSHOT.jar"]
