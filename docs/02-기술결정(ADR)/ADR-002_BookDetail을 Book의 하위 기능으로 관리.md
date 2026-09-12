

# ADR-002 — BookDetail을 Book의 하위 기능으로 관리

 **Status:** Accepted\
 **Date:** 2026-09-13

 ## Context

 `BookDetail`은 Book과 1:1 관계이며, 독립적인 비즈니스 리소스라기보다 Book의 상세 정보를 표현한다.

 요구사항에서도 `BookDetailController`, `BookDetailService`를 별도로 두지 않도록 정의되어 있다.

 ## Decision

 `BookDetail`의 생성, 수정, 조회, 삭제와 관련된 로직은 `BookController`와 `BookService`에서 관리한다.

```
BookController
      │
      ▼
BookService
 ├── Book
 └── BookDetail
```

 별도의 다음 계층은 생성하지 않는다.

```
BookDetailController
BookDetailService
```

 ## Consequences

 - Book과 BookDetail을 하나의 기능 단위로 관리할 수 있다.
- API 구조가 단순해진다.
- BookService가 BookDetail 관련 책임도 담당한다.

