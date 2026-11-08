# Treasureverse Backend

## Prerequisites

- Java 17
- Docker Desktop (for local MySQL 8.4)

## Run locally

```bash
docker compose up -d
./gradlew bootRun
```

On Windows PowerShell, run `./gradlew.bat bootRun`. Copy `.env.example` to `.env` only when Docker Compose variables need to be customized.

`AGENTS.md` and `.env` are local-only files and must not be committed.

## Health checks

- Application: `GET http://localhost:8080/api/v1/health`
- Actuator: `GET http://localhost:8080/actuator/health`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Social login

Kakao and Naver registrations are configured in `application-oauth.yml`. Keep client credentials in a local `.env` file or deployment environment variables, then activate the `oauth` profile. A social user must complete the address and phone-number onboarding step before account creation.
