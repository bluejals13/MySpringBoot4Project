

 # ADR-001 — Publisher와 Book의 1:N 관계 설계

 **Status:** Accepted\
 **Date:** 2026-09-13

 ## Context

 기존 시스템은 다음과 같은 관계를 가진다.

```
Book 1 ─── 1 BookDetail
```

 Publisher 기능을 추가하면서 하나의 Publisher가 여러 Book을 가질 수 있고, 하나의 Book은 하나의 Publisher에 소속되어야 한다.

```
Publisher 1 ─── N Book 1 ─── 1 BookDetail
```

 ## Decision

 `Publisher`와 `Book`을 **1:N 관계**로 설계한다.

 - Publisher는 여러 Book을 가진다.
- Book은 하나의 Publisher를 가진다.
- Book 생성/수정 시 `publisherId`를 통해 Publisher를 지정한다.
- 존재하지 않는 Publisher는 Book에 연결할 수 없다.
- 기존 `Book ↔ BookDetail` 1:1 관계는 유지한다.

 ## Consequences

 - Publisher 기준으로 Book 목록을 조회할 수 있다.
- Book에서 Publisher 정보를 조회할 수 있다.
- Book 생성/수정 시 Publisher 존재 여부 검증이 필요하다.
- Publisher 삭제 시 소속 Book에 대한 삭제 정책이 필요하다.

---

 