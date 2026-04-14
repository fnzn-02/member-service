# OnAir - 회원 서비스

Spring Boot 기반의 JWT 인증 회원 서비스입니다. 이메일 인증, 로그인/로그아웃, 마이페이지, 비밀번호 찾기 기능을 제공합니다.

<br>

## 화면

| 메인 (라이트) | 메인 (다크) |
|---|---|
| ![main-light](docs/images/main-light.png) | ![main-dark](docs/images/main-dark.png) |

| 회원가입 | 로그인 |
|---|---|
| ![signup](docs/images/signup.png) | ![login](docs/images/login.png) |

| 마이페이지 (라이트) | 마이페이지 (다크) |
|---|---|
| ![mypage-light](docs/images/mypage-light.png) | ![mypage-dark](docs/images/mypage-dark.png) |

| 비밀번호 찾기 |
|---|
| ![forgot-password](docs/images/forgot-password.png) |

<br>

## 기술 스택

**백엔드**
- Java 17, Spring Boot 3
- Spring Security + JWT
- Redis (이메일 인증번호 관리)
- MySQL, Spring Data JPA
- Lombok

**프론트엔드**
- React, Vite
- Tailwind CSS
- Axios

<br>

## 주요 기능

- **회원가입** — 이메일 인증번호 발송 및 검증 후 가입 완료
- **로그인** — JWT 토큰 발급 및 로컬 스토리지 저장
- **마이페이지** — 닉네임 변경, 비밀번호 변경, 회원 탈퇴
- **비밀번호 찾기** — 이메일 인증번호로 비밀번호 재설정
- **다크모드** — 라이트/다크 테마 전환

<br>

## 프로젝트 구조

```
member-service/
├── src/
│   └── main/java/com/example/member_service/
│       ├── config/          # Security, CORS 설정
│       ├── controller/      # API 엔드포인트
│       ├── dto/             # 요청/응답 DTO
│       ├── entity/          # JPA 엔티티
│       ├── exception/       # 전역 예외 처리
│       ├── jwt/             # JWT 생성 및 필터
│       ├── repository/      # JPA Repository
│       └── service/         # 비즈니스 로직
└── member-frontend/
    └── src/
        ├── api/             # Axios API 함수
        ├── components/      # 공통 컴포넌트 (Header)
        └── pages/           # 페이지 컴포넌트
```

<br>

## 환경 변수

백엔드 실행 전 아래 환경 변수를 설정해야 합니다.

```
DB_PASSWORD=데이터베이스_비밀번호
JWT_SECRET_KEY=JWT_시크릿_키
MAIL_USERNAME=메일_계정
MAIL_PASSWORD=메일_비밀번호
```

<br>

## 실행 방법

**백엔드**
```bash
./gradlew bootRun
```

**프론트엔드**
```bash
cd member-frontend
npm install
npm run dev
```

> 백엔드: `http://localhost:8080`  
> 프론트엔드: `http://localhost:5173`
