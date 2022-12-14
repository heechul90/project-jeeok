# JEEOKSHOP-DELIVERY-SERVER
주문과 동시에 배송이 저장된다.<br/>
배송 상태를 업데이트 할 수 있고, 관리자는 배송관리를 할 수 있는 REST API 서버입니다.

## 프로젝트 환경
| 기술 | 개발환경 |
| --- | --- |
| Spring Boot | - String Boot 2.7.5 </br> - Java 11 </br> - Gradle |
| Spring Data | - String Data JPA |
| Spring Cloud | - Eureka Client </br> - Config Client </br> - OpenFeign |
| ORM | - JPA </br> - Querydsl |
| Database | - MariaDB |
| Test | - Spring RestDocs </br> - JUnit5 </br> - Mock |
| Message Queue | - Kafka |
| Monitoring | - Spring Cloud Sleuth </br> - Zipkin |

## API 문서
| 서비스 | 설계서 |
| --- | --- |
| JEEOKSHOP-DELIVERY-SERVER | [[링크]](https://heechul90.github.io/docs/api/jeeok-project/jeeokshop/delivery-server-API-%EB%AC%B8%EC%84%9C/index.html) |

## 데이터베이스 설계
![img_1.png](img_1.png)

## 기능
- 라이더 모드
  - 배송
    - 배송 목록
    - 배송 상세
  - 배송 라이더
    - 내 배송 라이더 목록 조회
    - 내 배송 라이더 상세
    - 배송하기
    - 내 배송 라이더 수정
    - 배송 라이더 취소
- 관리자 모드
  - 배송
    - 배송 목록 조회
    - 배송 단건 조회
    - 배송 저장
    - 배송 수정
    - 배송 시작
    - 배송 완료
    - 배송 취소
    - 배송 삭제
  - 배송 라이더
    - 배송 라이더 저장
    - 배송 라이더 수정
    - 배송 라이더 삭제
