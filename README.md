# [JEEOK-PROJECT] 공부한거 적용해하기

## Client Server
| 서버 | 설명 |
| --- | --- |
| [JEEOK-CLIENT-VUE](https://github.com/heechul90/project-jeeok/tree/main/jeeok-client-vue) | API 라우팅 |

## API Gateway Server
| 서버 | 설명 |
| --- | --- |
| [APIGATEWAY-SERVER](https://github.com/heechul90/project-jeeok/tree/main/apigateway-server) | API 라우팅 |

## Config Server
| 서버 | 설명 |
| --- | --- |
| [CONFIG-SERVER](https://github.com/heechul90/project-jeeok/tree/main/config-server) | config 설정들 모아서 관리 |

## rest api server
| 서버 | 설명 |
| --- | --- |
| [MEMBER-SERVER](https://github.com/heechul90/project-jeeok/tree/main/member-server) | JWT 토큰을 이용한 로그인, 회원가입 구현 및 회원 관리 |
| [JEEOKLOG-SERVER](https://github.com/heechul90/project-jeeok/tree/main/jeeoklog-server) | 게시물 CRUD 개발 |

## 아키텍처 요약
| 기술스택 | 개발환경 |
| --- | --- |
| Spring | - String Boot 2.7.x </br> - Java 11 </br> - Gradle </br> - Spring Web Mvc </br> - Spring Security |
| Spring Cloud | - Eureka </br> - Gateway </br> - Config |
| Authenticate | - JWT (Json Web Token) |
| ORM | - JPA </br> - Querydsl |
| Database | - MariaDB </br> - Redis |
| Test | - Spring RestDocs </br> - JUnit5 |

## 시스템 아키텍처
![img.png](img.png)

