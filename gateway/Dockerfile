FROM openjdk:8-alpine

COPY target/uberjar/gateway.jar /gateway/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/gateway/app.jar"]
