

 # DTO 수정 내역

 ## 1\. DTO 구조 고정

 DTO는 다음 3개만 사용한다.

```
dto
├── BookDTO.java
├── BookDetailDTO.java
└── PublisherDTO.java
```

 추가적인 DTO는 생성하지 않는다.

---

 ## 2\. BookDetailDTO 수정

 도서 상세 정보를 하나의 DTO로 통합한다.

```
BookDetailDTO
├── description
├── language
├── pageCount
├── publisher
├── coverImageUrl
└── edition
```

 ### 수정 이유

 - `BookDetail` 엔티티의 상세 정보를 API에서 전달하기 위한 목적
- Request/Response를 별도 DTO로 나누지 않고 하나의 `BookDetailDTO`로 단순화
- DTO 개수가 불필요하게 증가하는 문제 방지

---

 ## 3\. BookDTO 수정

 Book과 관련된 DTO를 `BookDTO` 내부 클래스로 관리한다.

```
BookDTO
├── Request
│   ├── title
│   ├── author
│   ├── isbn
│   ├── price
│   ├── publishDate
│   ├── publisherId
│   └── detailRequest : BookDetailDTO
│
├── Response
│   ├── id
│   ├── title
│   ├── author
│   ├── isbn
│   ├── price
│   ├── publishDate
│   ├── publisher : PublisherDTO.SimpleResponse
│   └── detail : BookDetailResponse
│
├── SimpleResponse
└── BookDetailResponse
```

 ### 수정 이유

 기존처럼 Book 관련 DTO를 여러 파일로 분리하면 DTO가 지나치게 많아지고 관리가 복잡해질 수 있다.

 따라서 **Book이라는 하나의 기능 단위 안에서 Request, Response, SimpleResponse를 관리**하도록 수정한다.

 특히 목록 조회에서는 전체 Book 정보를 전달할 필요가 없으므로 `SimpleResponse`를 사용한다.

---

 ## 4\. PublisherDTO 수정

 Publisher 역시 하나의 DTO 안에서 Request/Response를 관리한다.

```
PublisherDTO
├── Request
│   ├── name
│   ├── establishedDate
│   └── address
│
├── Response
│   ├── id
│   ├── name
│   ├── establishedDate
│   ├── address
│   ├── bookCount
│   └── books : List<BookDTO.SimpleResponse>
│
└── SimpleResponse
    ├── id
    ├── name
    ├── establishedDate
    ├── address
    └── bookCount
```

 ### 수정 이유

 - Publisher 등록/수정 요청과 조회 응답의 역할을 구분
- Publisher 조회 시 연결된 Book 목록을 표현할 수 있도록 `books` 추가
- Publisher와 Book이 서로 전체 Response를 참조하면 순환 참조 문제가 발생할 수 있으므로 `BookDTO.SimpleResponse`를 사용

---

 ## 5\. 이번 수정의 핵심

```
기존
DTO가 기능별로 계속 증가
        ↓
관리 복잡도 증가
        ↓
Request / Response / SimpleResponse를 하나의 DTO 내부로 통합
        ↓
BookDTO
BookDetailDTO
PublisherDTO
```

 따라서 앞으로는 **위 3개의 DTO만 사용하며, 별도의 DTO를 추가하지 않는다.**

 현재 Repository 테스트는

```
Tests run: 15
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

 이므로 Repository 계층은 정상적으로 동작하는 상태이다. DTO 수정은 이 Repository 구조를 깨뜨리지 않는 범위에서 진행하면 된다.