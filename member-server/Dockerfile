FROM openjdk:11
VOLUME /tmp
COPY build/libs/member-server-1.0.0.jar member-server.jar
ENTRYPOINT ["java", "-jar", "member-server.jar"]