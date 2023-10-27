FROM maven:3.8.3-openjdk-17-slim AS build

WORKDIR /feedapp-2

COPY pom.xml ./

RUN mvn dependency:go-offline -B

COPY src src/

RUN mvn package && ls -la /feedapp-2/target

FROM openjdk:17-jdk-slim

WORKDIR /feedapp-2

COPY --from=build /feedapp-2/target/feedapp-0.0.1-SNAPSHOT.jar /feedapp-2/feedapp-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "/feedapp-2/feedapp-0.0.1-SNAPSHOT.jar"]
