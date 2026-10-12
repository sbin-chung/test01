# 메모장 연습 프로젝트

바이브 코딩 연습용 웹 프로그램. 회원가입, 로그인/로그아웃, 내 메모 CRUD.
사용자는 태블릿에서 Railway 배포 주소로 결과를 확인한다.

## 스택
- Spring Boot 3.5 (Java 21), Gradle
- 화면: Thymeleaf (서버 렌더링, 별도 프론트 없음)
- DB: PostgreSQL + Spring Data JPA (`ddl-auto: update`로 테이블 자동 생성)
- 인증: Spring Security, 비밀번호는 BCrypt
- 배포: Railway (루트의 Dockerfile로 빌드)

## 폴더 구조
- `src/main/java/com/example/memo/` 아래 기능별 패키지: `user/`, `memo/`, `config/`
- 각 기능 패키지 안에서 Controller → Service → Repository 레이어를 나눈다
- 화면: `src/main/resources/templates/` (기능별 하위 폴더), 공통 CSS는 `static/css/style.css`

## 규칙
- 한 번에 기능 하나씩 만들고 기능마다 커밋한다
- DB 접속 정보, 비밀 키는 코드에 적지 않고 환경변수(`PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`)로만 받는다
- 메모는 항상 로그인한 사용자 id로 걸러서 조회한다. 다른 사람 메모 id로 접근하면 404
- 화면은 모바일 우선(좁은 화면 기준)으로 만든다
- 화면 문구는 한국어

## 명령
- 테스트: `./gradlew test` (테스트는 H2 인메모리 DB 사용)
- 로컬 실행: `./gradlew bootRun` (로컬 PostgreSQL 필요)
