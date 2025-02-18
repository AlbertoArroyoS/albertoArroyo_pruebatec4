FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/travelagency-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} travelagency-contenedor.jar
ENTRYPOINT ["java", "-jar", "travelagency-contenedor.jar"]
