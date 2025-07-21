# Etapa 1: Build
FROM bellsoft/liberica-openjdk-alpine:21.0.1 as builder
ARG JAR_FILE=target/trade-0.0.1-SNAPSHOT.jar
WORKDIR /app
COPY ${JAR_FILE} app.jar


# Etapa 2: Runtime
FROM bellsoft/liberica-runtime-container:jre-slim-musl as final

# Criar usuário não-root
RUN adduser --system --home /app --shell /bin/sh appuser

WORKDIR /app
COPY --from=builder /app/app.jar app.jar

# Mudar dono dos arquivos para o novo usuário (se necessário)
RUN chown -R appuser:appuser /app

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]
