FROM docker.io/openjdk:21-jdk
WORKDIR /app
COPY ./target/bounded-context-canvas-management-*.jar bounded-context-canvas-management.jar
CMD java -jar bounded-context-canvas-management.jar
EXPOSE 8084