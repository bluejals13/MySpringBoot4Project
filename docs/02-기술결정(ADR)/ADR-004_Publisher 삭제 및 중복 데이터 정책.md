# ADR-004 — Publisher 삭제 및 중복 데이터 정책

 **Status:** Accepted\
 **Date:** 2026-09-13

 ## Context

 Publisher는 Book과 1:N 관계를 가진다.

 따라서 Book이 존재하는 Publisher를 삭제하면 Book의 Publisher 관계가 깨질 수 있다.

 또한 Publisher 이름과 Book ISBN은 각각 식별 및 검색에 사용되는 중요한 값이므로 중복을 허용하지 않는다.

 ## Decision

 다음 정책을 적용한다.

 ### Publisher 삭제

 Book이 하나라도 존재하는 Publisher는 삭제할 수 없다.

```
Publisher 삭제
     │
     ▼
소속 Book 존재?
   │       │
  Yes      No
   │       │
   ▼       ▼
삭제 거부   삭제
```

 ### Publisher 이름

 Publisher 이름은 중복을 허용하지 않는다.

 ### Book ISBN

 Book ISBN은 중복을 허용하지 않는다.

 생성 및 수정 시 중복 여부를 검증한다.

 ## Consequences

 - Publisher 삭제로 인한 Book의 관계 단절을 방지한다.
- Publisher 이름 기반 조회의 모호성을 방지한다.
- 동일 ISBN을 가진 중복 Book 생성을 방지한다.
- 생성뿐 아니라 수정 시에도 중복 검증이 필요하다.

---

 ## 최종 ADR 구성

```
docs/
└── adr/
    ├── ADR-001-publisher-book-relationship.md
    ├── ADR-002-book-detail-management.md
    ├── ADR-003-book-fetch-join.md
    └── ADR-004-publisher-delete-and-uniqueness.md
```

