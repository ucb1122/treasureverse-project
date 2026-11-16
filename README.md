# Treasureverse

수집품을 판매하고, 구매 희망을 등록하고, 전시와 경매를 탐색하는 컬렉터 플랫폼입니다.

## 통합 개발 환경 실행

Docker Desktop을 실행한 뒤 프로젝트 루트에서 다음 명령을 실행합니다.

```powershell
docker compose up --build
```

처음 실행은 이미지와 의존성을 내려받고 백엔드를 빌드하므로 시간이 걸릴 수 있습니다.
서비스가 준비되면 다음 주소를 사용합니다.

| 서비스 | 주소 |
| --- | --- |
| 프론트엔드 | http://localhost:5173 |
| 백엔드 Swagger UI | http://localhost:8080/swagger-ui.html |
| MinIO 콘솔 | http://localhost:9001 |

실행을 중단하려면 `Ctrl + C`를 누릅니다. 백그라운드 실행은 아래 명령을 사용합니다.

```powershell
docker compose up --build -d
```

종료는 다음과 같습니다.

```powershell
docker compose down
```

데이터베이스와 MinIO 데이터를 포함해 초기화할 때만 다음 명령을 사용합니다.

```powershell
docker compose down -v
```

기본 계정이나 포트 등의 값을 바꾸려면 `.env.example`을 `.env`로 복사해 수정합니다. `.env` 파일은 Git에 올리지 않습니다.
