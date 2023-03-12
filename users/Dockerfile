FROM bellsoft/liberica-openjdk-alpine:17

ENV POSTGRES $POSTGRES
ENV EUREKA_URI $EUREKA_URI
WORKDIR /opt
COPY build/libs/users-0.0.1-SNAPSHOT.jar ./app.jar
CMD java $JAVA_OPTIONS -jar app.jar
