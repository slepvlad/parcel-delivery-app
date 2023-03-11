FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /opt

ENV EUREKA_URI $EUREKA_URI

COPY build/libs/gateway-0.0.1-SNAPSHOT.jar ./app.jar
CMD java $JAVA_OPTIONS -jar app.jar
