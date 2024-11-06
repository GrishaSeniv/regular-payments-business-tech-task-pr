FROM eclipse-temurin:21

WORKDIR /app

COPY target/regular-payments-business-0.0.1-SNAPSHOT.jar /app/regular-payments-business-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "regular-payments-business-0.0.1-SNAPSHOT.jar"]
