# 실행 및 빌드 환경

## Environment

- OS: Windows 11
- Terminal: PowerShell / WSL
- Java: 17.0.20
- Spring Boot: 4.0.8
- Hibernate: 7.2.24.Final
- Database: H2 2.4.240
- Build Tool: Maven Wrapper
- Repository: Git

## Build

```bash
./mvnw clean package
```

## Test
```bash
./mvnw clean test
```

## Expected:
```txt
Tests run: 15
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```
## Run
```bash
./mvnw spring-boot:run
```

## Verification

* Application context initialization
* BookRepositoryTest: 9 tests
* PublisherRepositoryTest: 5 tests
* ApplicationTest: 1 test
* Total: 15 tests


### 특히 좋은 점

실제 로그 정보.
```text
Java 17.0.20
Spring Boot 4.0.8
Hibernate 7.2.24.Final
H2 2.4.240
Maven

그리고:
```bash
./mvnw clean test
```

