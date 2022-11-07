# MEMBER-SERVER

## 프로젝트 환경
| 기술 | 개발환경 |
| --- | --- |
| Spring Boot | - String Boot 2.7.5 </br> - Java 11 </br> - Gradle |
| Spring Data | - String Data JPA |
| Spring Cloud | - Eureka Client </br> - Config |
| Authenticate | - JWT (Json Web Token) 0.11.2 |
| ORM | - JPA </br> - Querydsl |
| Database | - MariaDB </br> - Redis |
| Test | - Spring RestDocs </br> - JUnit5 |

## API 문서
| 서비스 | 설계서 |
| --- | --- |
| MEMBER-SERVER | [[링크]](https://heechul90.github.io/docs/api/jeeok-project/member-server-API-문서/) |

## 기능

- 회원
  - 회원 목록 조회
  - 회원 단건 조회
  - 회원 저장
  - 회원 수정
  - 회원 삭제
- 회원 로그인
  - 로그인
  - 토큰 조회
  - 로그아웃