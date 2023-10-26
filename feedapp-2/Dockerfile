FROM openjdk:17-jdk-slim

WORKDIR /feedapp-2

COPY target/feedapp-0.0.1-SNAPSHOT.jar /feedapp-2/feedapp-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "/feedapp-2/feedapp-0.0.1-SNAPSHOT.jar"]
