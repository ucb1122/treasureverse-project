# Treasureverse Backend

## Prerequisites

- Java 17
- Docker Desktop (for local MySQL 8.4)

## 통합 개발 환경 실행

프로젝트 루트에서 Docker Compose로 프론트엔드, 백엔드, MariaDB, Redis, MinIO를 함께 실행합니다.

```bash
cd ..
docker compose up --build
```

Windows PowerShell에서는 프로젝트 루트에서 `docker compose up --build`를 실행합니다. Docker Compose 변수 변경이 필요할 때만 루트 `.env.example`을 `.env`로 복사합니다.

백엔드만 IDE에서 실행하려면 인프라를 먼저 준비한 후 다음 명령을 사용합니다.

```bash
docker compose -f backend/compose.yaml up -d
cd backend
./gradlew bootRun
```

`AGENTS.md` and `.env` are local-only files and must not be committed.

## Health checks

- Application: `GET http://localhost:8080/api/v1/health`
- Actuator: `GET http://localhost:8080/actuator/health`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Social login

Kakao and Naver registrations are configured in `application-oauth.yml`. Keep client credentials in a local `.env` file or deployment environment variables, then activate the `oauth` profile. A social user must complete the address and phone-number onboarding step before account creation.

### OAuth local setup

1. Register `http://localhost:8080/login/oauth2/code/kakao` in the Kakao developer console and `http://localhost:8080/login/oauth2/code/naver` in the Naver developer console.
2. Copy `backend/.env.example` to a local, ignored `.env` file and enter `KAKAO_CLIENT_ID`, `KAKAO_CLIENT_SECRET`, `NAVER_CLIENT_ID`, and `NAVER_CLIENT_SECRET`.
3. Start the backend with `SPRING_PROFILES_ACTIVE=local,oauth`. Keep `OAUTH_FRONTEND_REDIRECT_URI=http://localhost:5173/oauth/callback` for local development.

The browser enters the OAuth flow at `/oauth2/authorization/kakao` or `/oauth2/authorization/naver`. Existing social accounts receive Treasureverse JWT tokens; new users receive a single-use, ten-minute signup ticket and must provide their name, phone number, and address before a member account is created.
