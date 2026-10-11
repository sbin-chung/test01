# 메모장 (바이브 코딩 연습)

Spring Boot + Thymeleaf + PostgreSQL로 만드는 회원가입·로그인·메모장 연습 프로젝트입니다.

## Railway 배포
1. Railway에서 New Project → Deploy from GitHub repo → `test01` 선택
2. 같은 프로젝트에 PostgreSQL 추가 (New → Database → PostgreSQL)
3. 앱 서비스의 Variables에 PostgreSQL 변수를 연결: `PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`
   (Add Variable Reference로 Postgres 서비스의 같은 이름 변수를 선택)
4. 앱 서비스 Settings → Networking → Generate Domain으로 주소 만들기
5. 그 주소에 들어가 "Hello"가 보이면 성공
