# 모모펫 스프링부트 버전

## 스프링 이니셜라이저 설정
![스크린샷 2023-09-15 오후 4 45 08](https://github.com/yelo-o/momopet-springboot/assets/64743180/8872c4ca-0641-42f2-85b8-12e9e4e08481)

## 사용 기능
- 자바 버전 : 11
- 스프링부트 버전 : 2.7.15
- IDE : 인텔리제이
- DB
  - 테스트 : H2
  - 운영 : Mysql
- View
  - html (thymeleaf)
- 라이브러리 관리 및 빌드 도구 : gradle-8.2.1
- 테스트
  - JUnit5
- 포트번호 : 9090

## H2 데이터베이스 설정 방법
- 초기 설정
  - jdbc:h2:~/momopet
- 아래 파일 만들어진거 확인
  - ~/momopet.mv.db
- 상시 접속 url
  - jdbc:h2:tcp://localhost/~/momopet
