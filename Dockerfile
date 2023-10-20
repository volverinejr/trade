FROM eclipse-temurin:17-jdk-alpine as builder
ARG JAR_FILE=target/trade-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar



FROM bellsoft/liberica-runtime-container:jre-slim-musl as final
COPY --from=builder /app.jar .
ENTRYPOINT ["java","-jar","/app.jar"]