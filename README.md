# [JEEOK-PROJECT] Spring Cloud 를 활용한 Back-End 개발 프로젝트

## Client Server
| 서버 | 설명 |
| --- | --- |
| [JEEOK-CLIENT-VUE](https://github.com/heechul90/project-jeeok/tree/main/jeeok-client-vue) | Front end 서버 |

## API Gateway Server
| 서버 | 설명 |
| --- | --- |
| [APIGATEWAY-SERVER](https://github.com/heechul90/project-jeeok/tree/main/apigateway-server) | 모든 요청을 통과하는 API 라우팅 서버 |

## Config Server
| 서버 | 설명 |
| --- | --- |
| [CONFIG-SERVER](https://github.com/heechul90/project-jeeok/tree/main/config-server) | 각 서버의 application.yml을 관리하는 서버 |

## rest api server
| 서버 | 설명 |
| --- | --- |
| [MEMBER-SERVER](https://github.com/heechul90/project-jeeok/tree/main/member-server) | JWT 토큰을 이용한 로그인/로그아웃, 회원가입 및 회원 관리 |
| [JEEOKLOG-SERVER](https://github.com/heechul90/project-jeeok/tree/main/jeeoklog-server) | 게시물 CRUD 개발 |
| [JEEOKSHOP-STORE-SERVER](https://github.com/heechul90/project-jeeok/tree/main/jeeokshop-store-server) | 관리자 및 매니저 스토어, 상품 관리 기능 개발 <br/> 유저 스토어 목록 확인 및 찜하기 등록 기능 개발 |
| [JEEOKSHOP-ORDER-SERVER](https://github.com/heechul90/project-jeeok/tree/main/jeeokshop-order-server) | 관리자 및 매니저 주문 관리 기능 <br/> 유저 상품 주문 기능 |
| [JEEOKSHOP-DELIVERY-SERVER](https://github.com/heechul90/project-jeeok/tree/main/jeeokshop-delivery-server) | 주문시 배송 추가 기능 <br/> 관리자 배송 관리 기능 |

## 사용 기술스텍
| 기술스택 | 개발환경 |
| --- | --- |
| Spring | - String Boot 2.7.x </br> - Java 11 </br> - Gradle </br> - Spring Web Mvc </br> - Spring Security |
| Spring Cloud | - Eureka </br> - Gateway </br> - Config </br> - OpenFeign |
| Authenticate | - JWT (Json Web Token) |
| ORM | - JPA </br> - Querydsl |
| Database | - MariaDB </br> - Redis |
| Test | - Spring RestDocs </br> - JUnit5 </br> - Mock |
| Message Queue | - Kafka |

## 시스템 아키텍처
![img_1.png](img_1.png)

