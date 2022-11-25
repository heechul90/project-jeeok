# JEEOK-APIGATEWAY-SERVER
Spring Cloud Gateway(SCG) 라이브러리를 사용하여 JEEOK-PROJECT의 API Gateway 역할을 담당합니다.

## 프로젝트 환경
| 기술 | 개발환경 |
| --- | --- |
| Spring Boot | - String Boot 2.7.5 </br> - Java 11 </br> - Gradle |
| Spring Cloud | - API Gateway </br> - Eureka Client </br> - Config Client |
| Monitoring | - Spring Cloud Sleuth </br> - Zipkin |

##필터
- JWT 토큰 체크
- 권한 체크
  - 매니저 (/manager/**)
  - 배달원 (/rider/**)
  - 관리자 (/admin/**)
