FROM bellsoft/liberica-openjdk-alpine:17

ENV POSTGRES $POSTGRES
ENV EUREKA_URI $EUREKA_URI
ENV RABBITMQ $RABBITMQ
WORKDIR /opt
COPY build/libs/delivery-tasks-0.0.1-SNAPSHOT.jar ./app.jar
CMD java $JAVA_OPTIONS -jar app.jar
