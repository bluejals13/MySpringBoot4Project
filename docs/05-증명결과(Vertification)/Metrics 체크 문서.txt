네. 현재 로그를 보면 **Spring Boot 4.0.8 \+ Java 17 + MariaDB \+ JPA + Tomcat이 정상 기동했고, 초기 데이터 8건도 정상 생성**됐습니다.

 바로 검증할 수 있도록 **Verification + Metrics 체크 문서** 형태로 정리하면 아래처럼 사용할 수 있습니다.

 # Spring Boot Student API — Verification & Metrics

 ## 1. 서버 기동 확인

 현재 서버:

```
Spring Boot 4.0.8
Java 17.0.20
Tomcat 11.0.24
Port: 8081
Context Path: /
Database: MariaDB 10.11.18
Database: lab_db
JPA Repository: 2개
```

 로그상 핵심 성공 메시지:

```
Tomcat started on port 8081 with context path '/'
Started MySpringBoot4Project in 68.749 seconds
Data initialization completed successfully
Created 8 students
```

 따라서 서버 자체는 정상입니다.

---

 # 2\. 기본 Health Check

 Spring Boot Actuator가 활성화되어 있고 `/actuator` 엔드포인트가 노출되어 있습니다.

 ### 전체 Actuator 확인

```
curl -i http://localhost:8081/actuator
```

 ### Health 확인

```
curl -i http://localhost:8081/actuator/health
```

 정상이면 일반적으로:

```
{
  "status": "UP"
}
```

 를 확인할 수 있습니다.

---

 # 3\. Student API Verification

 기본 API Base URL:

```
http://localhost:8081/api/students
```

 ## 3-1. 전체 학생 조회

```
curl -i http://localhost:8081/api/students
```

 정상이라면 초기 데이터 8건이 반환됩니다.

---

 ## 3-2. 특정 학생 조회

```
curl -i http://localhost:8081/api/students/1
```

 학생 ID가 `1`인 데이터가 존재하면 정상적으로 반환됩니다.

---

 ## 3-3. 학번으로 조회

 초기 데이터의 실제 학번을 알고 있다면:

```
curl -i http://localhost:8081/api/students/number/20250001
```

 처럼 테스트합니다.

 학번이 다르면 실제 초기 데이터의 학번으로 바꾸면 됩니다.

---

 # 4\. POST — 학생 생성

 학생 기본 정보만 생성:

```
curl -i -X POST http://localhost:8081/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "홍길동",
    "studentNumber": "20269999"
  }'
```

 상세정보까지 포함한다면:

```
curl -i -X POST http://localhost:8081/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "홍길동",
    "studentNumber": "20269999",
    "detailRequest": {
      "address": "서울특별시",
      "phoneNumber": "010-9999-9999",
      "email": "hong@example.com",
      "dateOfBirth": "2000-01-01"
    }
  }'
```

 여기서 확인할 핵심은:

```
Student 생성
        ↓
StudentDetail 생성
        ↓
student.setStudentDetail(...)
        ↓
cascade = ALL
        ↓
Student 저장
        ↓
StudentDetail도 저장
```

 입니다.

---

 # 5\. 중복 학번 Verification

 방금 생성한:

```
20269999
```

 를 다시 생성해봅니다.

```
curl -i -X POST http://localhost:8081/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "중복학생",
    "studentNumber": "20269999"
  }'
```

 정상적인 비즈니스 로직이라면:

```
STUDENT_NUMBER_DUPLICATE
```

 계열의 오류 응답이 발생해야 합니다.

 이 테스트는 단순 CRUD가 아니라 **BusinessException + ErrorCode까지 연결되어 있는지 확인하는 테스트**입니다.

---

 # 6\. PUT — 학생 수정

```
curl -i -X PUT http://localhost:8081/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "수정된 학생",
    "studentNumber": "20250001",
    "detailRequest": {
      "address": "서울 강남구",
      "phoneNumber": "010-1111-2222",
      "email": "updated@example.com",
      "dateOfBirth": "2001-01-01"
    }
  }'
```

 여기서 중요한 것은 `save()`를 다시 호출하지 않아도:

```
student.setName(...);
student.setStudentNumber(...);
currentDetail.setAddress(...);
```

 등의 변경사항이 **Dirty Checking**을 통해 UPDATE 되는지 확인하는 것입니다.

 로그에서:

```
Hibernate:
    update ...
```

 가 발생하는지 확인하면 됩니다.

---

 # 7\. DELETE — 학생 삭제

```
curl -i -X DELETE http://localhost:8081/api/students/1
```

 현재 Controller:

```
return ResponseEntity.ok().build();
```

 이므로 정상 삭제 시:

```
HTTP/1.1 200 OK
```

 를 기대할 수 있습니다.

 삭제 후:

```
curl -i http://localhost:8081/api/students/1
```

 을 실행해서 `RESOURCE_NOT_FOUND`가 발생하는지 확인합니다.

---

 # 8\. 존재하지 않는 학생 Verification

```
curl -i http://localhost:8081/api/students/999999
```

 정상적인 예외 처리 구조라면:

```
RESOURCE_NOT_FOUND
```

 계열의 HTTP 오류 응답을 확인할 수 있어야 합니다.

 이 테스트는:

```
Controller
   ↓
Service
   ↓
Repository
   ↓
Optional.empty()
   ↓
BusinessException
   ↓
Global Exception Handler
   ↓
HTTP Error Response
```

 가 제대로 연결되어 있는지 확인하는 것입니다.

---

 # 9\. 이메일 중복 Verification

 이미 존재하는 이메일을 다시 사용해서:

```
curl -i -X POST http://localhost:8081/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "이메일중복",
    "studentNumber": "20268888",
    "detailRequest": {
      "email": "hong@example.com"
    }
  }'
```

 확인합니다.

 Service에서:

```
studentDetailRepository.existsByEmail(...)
```

 을 사용하고 있으므로 중복 이메일이면:

```
EMAIL_DUPLICATE
```

 를 확인할 수 있어야 합니다.

---

 # 10\. 전화번호 중복 Verification

```
curl -i -X POST http://localhost:8081/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "전화번호중복",
    "studentNumber": "20267777",
    "detailRequest": {
      "phoneNumber": "010-1111-2222"
    }
  }'
```

 정상적으로 구현되어 있다면:

```
PHONE_NUMBER_DUPLICATE
```

 를 확인합니다.

---

 # 11\. Validation Verification

 `@Valid`가 제대로 작동하는지 확인합니다.

 예를 들어 필수값인 `name` 또는 `studentNumber`를 누락합니다.

```
curl -i -X POST http://localhost:8081/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "studentNumber": ""
  }'
```

 여기서 확인할 것은:

```
@RequestBody
      ↓
@Valid
      ↓
Bean Validation
      ↓
Validation Error
      ↓
Exception Handler
      ↓
HTTP Error Response
```

 입니다.

---

 # 12\. Metrics 확인

 Actuator Metrics가 활성화되어 있다면:

```
curl -i http://localhost:8081/actuator/metrics
```

 전체 Metric 목록을 확인할 수 있습니다.

 특정 Metric은:

```
curl -i http://localhost:8081/actuator/metrics/jvm.memory.used
```

 또는:

```
curl -i http://localhost:8081/actuator/metrics/process.uptime
```

 처럼 확인할 수 있습니다.

 HTTP 요청 관련 Metric을 사용한다면:

```
curl -i http://localhost:8081/actuator/metrics/http.server.requests
```

 를 확인합니다.

---

 # 13\. JVM Metrics

 ### JVM 메모리

```
curl -s http://localhost:8081/actuator/metrics/jvm.memory.used
```

 ### JVM 최대 메모리

```
curl -s http://localhost:8081/actuator/metrics/jvm.memory.max
```

 ### JVM Thread

```
curl -s http://localhost:8081/actuator/metrics/jvm.threads.live
```

 ### 프로세스 가동 시간

```
curl -s http://localhost:8081/actuator/metrics/process.uptime
```

---

 # 14\. HTTP Request Metrics

 API를 몇 번 호출한 뒤:

```
curl -s http://localhost:8081/actuator/metrics/http.server.requests
```

 를 확인합니다.

 특정 URI 정보를 조회할 필요가 있다면 Metric의 `availableTags`를 확인할 수 있습니다.

 즉:

```
curl 요청
    ↓
Controller
    ↓
Service
    ↓
Repository
    ↓
HTTP 응답
    ↓
Actuator Metrics
```

 라는 흐름을 확인할 수 있습니다.

---

 # 15\. JPA / Database Verification

 현재 로그에서 이미 DB 연결은 성공했습니다.

 핵심 로그:

```
HikariPool-1 - Added connection org.mariadb.jdbc.Connection
HikariPool-1 - Start completed.
```

 그리고 Hibernate가:

```
create table student_details
create table students
```

 를 수행했습니다.

 또한:

```
foreign key (student_id)
references students (student_id)
```

 까지 생성했습니다.

 따라서 현재:

```
Spring Boot
    ↓
HikariCP
    ↓
MariaDB
    ↓
Hibernate
    ↓
JPA
```

 연결이 정상입니다.

---

 # 16. 현재 구조의 핵심 Verification Matrix

 | 영역 | 검증 방법 | 확인 내용 |
| --- | --- | --- |
| Server | `/actuator/health` | 서버 UP |
| Controller | `/api/students` | HTTP API |
| GET | GET | 전체/단건 조회 |
| POST | POST | 생성 |
| PUT | PUT | 수정 |
| DELETE | DELETE | 삭제 |
| Validation | 잘못된 JSON | `@Valid` |
| Duplicate | 중복 학번 | BusinessException |
| Duplicate | 중복 이메일 | BusinessException |
| Duplicate | 중복 전화번호 | BusinessException |
| JPA | Hibernate SQL | Entity ↔ DB |
| Cascade | Student 생성 | StudentDetail 동시 저장 |
| Dirty Checking | Student 수정 | UPDATE 발생 |
| Repository | JPA Query | JOIN FETCH |
| DB | Hikari | MariaDB 연결 |
| Metrics | `/actuator/metrics` | JVM/HTTP 지표 |
| Actuator | `/actuator` | 운영 엔드포인트 |

---

 # 17. 지금 상태에서 가장 중요한 테스트 순서

 실제로는 전부 무작정 테스트하기보다는 다음 순서가 좋습니다.

```
# 1. 서버 Health
curl -i http://localhost:8081/actuator/health

# 2. 전체 조회
curl -i http://localhost:8081/api/students

# 3. 단건 조회
curl -i http://localhost:8081/api/students/1

# 4. 학생 생성
curl -i -X POST http://localhost:8081/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "테스트학생",
    "studentNumber": "TEST9999"
  }'

# 5. 생성된 학생 확인
curl -i http://localhost:8081/api/students

# 6. 수정
curl -i -X PUT http://localhost:8081/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "수정학생",
    "studentNumber": "TEST0001"
  }'

# 7. 삭제
curl -i -X DELETE http://localhost:8081/api/students/1

# 8. 삭제 확인
curl -i http://localhost:8081/api/students/1

# 9. Metrics
curl -i http://localhost:8081/actuator/metrics

# 10. HTTP Metrics
curl -i http://localhost:8081/actuator/metrics/http.server.requests
```

 다만 **PUT/DELETE에서 ID `1`은 실제 테스트 데이터가 맞는지 확인하고 사용**하는 것이 좋습니다.

---

 ## 현재 로그를 기준으로 한 최종 판정

 현재까지는:

```
[OK] Maven build
[OK] Java 17
[OK] Spring Boot 4.0.8
[OK] Tomcat 11
[OK] Port 8081
[OK] Spring Data JPA
[OK] Hibernate 7.2.24
[OK] HikariCP
[OK] MariaDB 10.11.18
[OK] Database connection
[OK] Table creation
[OK] FK creation
[OK] Repository scanning
[OK] 2 JPA repositories
[OK] EntityManagerFactory
[OK] Data initialization
[OK] 8 students inserted
[OK] Application started
```

 그리고 하나의 **주의 사항**만 있습니다.

```
spring.jpa.open-in-view is enabled by default
```

 이건 애플리케이션이 실패했다는 뜻이 아니라 **WARN**입니다. REST API 중심 애플리케이션이라면 추후:

```
spring.jpa.open-in-view=false
```

 로 명시하고, 그 상태에서도 DTO 변환과 `JOIN FETCH`가 정상 동작하는지 확인하는 것을 권장합니다.

 현재 구조에서는 이미:

```
findAllWithStudentDetail()
findByIdWithStudentDetail()
findByStudentNumber()
```

 에서 `LEFT JOIN FETCH`를 사용하고 있으므로 **OSIV를 끄고 JPA fetch 전략을 명확하게 관리하는 방향**으로 발전시키기 좋은 상태입니다.