 # ADR-003 — Book 조회에 Fetch Join 적용

 **Status:** Accepted\
 **Date:** 2026-09-13

 ## Context

 Book 조회 시 다음과 같은 연관 데이터를 함께 제공해야 한다.

```
Book
 ├── Publisher
 └── BookDetail
```

 연관관계를 개별적으로 조회할 경우 불필요한 추가 쿼리가 발생할 수 있으며, 목록 조회에서는 N+1 문제가 발생할 가능성이 있다.

 ## Decision

 Book 조회 요구사항에 따라 Repository에서 Fetch Join을 사용한다.

```
Book
Book + BookDetail
Book + BookDetail + Publisher
```

 조회 목적에 맞게 Fetch Join 쿼리를 분리한다.

 예:

```
findAllWithBookDetail()
findByIsbnWithBookDetail()
findAllWithBookDetailAndPublisher()
findByPublisherId()
```

 ## Consequences

 - Book 조회에 필요한 연관 데이터를 효율적으로 조회할 수 있다.
- N+1 문제를 방지할 수 있다.
- 조회 목적에 따라 Repository 메서드가 구분된다.
- 모든 조회에서 무조건 Fetch Join하지 않고 필요한 경우에만 적용한다.

