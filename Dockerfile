# Etapa 1: Build
FROM bellsoft/liberica-openjdk-alpine:21.0.1 AS builder
ARG JAR_FILE=target/trade-0.0.1-SNAPSHOT.jar
WORKDIR /app
COPY ${JAR_FILE} app.jar


# Etapa 2: Runtime
FROM bellsoft/liberica-runtime-container:jre-slim-musl AS final

# Criar usuário não-root
RUN adduser --uid 1000 --system --home /app --shell /bin/sh appuser


WORKDIR /app
COPY --from=builder --chown=appuser:appuser /app/app.jar app.jar

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]
