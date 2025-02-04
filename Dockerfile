FROM docker.io/openjdk:17.0.2
WORKDIR /app
COPY ./target/bounded-context-canvas-management-*.jar bounded-context-canvas-management.jar
CMD java -jar bounded-context-canvas-management.jar
EXPOSE 8084