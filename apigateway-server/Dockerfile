FROM openjdk:11
VOLUME /tmp
COPY build/libs/apigateway-server-1.0.0.jar apigateway-server.jar
ENTRYPOINT ["java", "-jar", "apigateway-server.jar"]