# Treasureverse Frontend

Treasureverse의 컬렉터 마켓플레이스 프론트엔드입니다. Vite와 React를 사용하며,
로컬 Spring Boot 백엔드 API를 호출합니다.

## 실행

```bash
npm install
npm run dev
```

브라우저에서 `http://localhost:5173`을 엽니다.

## 백엔드 연결

기본 API 주소는 `http://localhost:8080/api/v1`입니다. 다른 주소를 사용하려면
`.env.example`을 복사해 `.env.local`을 만들고 값을 변경합니다.

```bash
VITE_API_BASE_URL=http://localhost:8080/api/v1
```

백엔드가 실행되면 판매 게시글과 구매 희망 게시글을 실제 API에서 불러옵니다.
백엔드가 꺼져 있거나 게시글이 없으면 로컬 디자인 확인을 위한 예시 컬렉션이 표시됩니다.

## 현재 연결 API

- `GET /api/v1/sale-posts`
- `GET /api/v1/purchase-posts`
- `POST /api/v1/auth/login`
