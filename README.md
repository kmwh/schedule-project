# Schedule Project API Documentation

## API 목록

### 1. 일정 등록
POST `/api/schedules`
- 요청 바디
`{
  "todo": "할 일",
  "password": "1234",
  "authorName": "홍길동",
  "authorEmail": "hong@example.com"
}`
- 응답 예시
`{
  "id": 1,
  "todo": "할 일",
  "authorName": "홍길동",
  "authorEmail": "hong@example.com",
  "createdAt": "2025-05-25T12:34:56",
  "updatedAt": "2025-05-25T12:34:56"
}`

### 2. 일정 단건 조회
GET `/api/schedules/{id}`
- 응답 예시
`{
  "id": 1,
  "todo": "할 일",
  "authorName": "홍길동",
  "authorEmail": "hong@example.com",
  "createdAt": "2025-05-25T12:34:56",
  "updatedAt": "2025-05-25T12:34:56"
}`

### 3. 일정 목록 조회
GET `/api/schedules`
쿼리 파라미터 (선택사항):
`updatedAt: YYYY-MM-DD 형식의 수정일
authorId: 작성자 고유 인덱스
page: 페이지 번호 (기본값 0)
size: 페이지 크기 (기본값 20)`
- 응답 예시
`{
  "content": [
    {
      "id": 1,
      "todo": "할 일",
      "authorName": "홍길동",
      "authorEmail": "hong@example.com",
      "createdAt": "2025-05-25T12:34:56",
      "updatedAt": "2025-05-25T12:34:56"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "number": 0,
  "size": 20
}`

### 4. 일정 수정
PUT `/api/schedules/{id}`
- 요청 바디
`{
  "todo": "할 일",
  "password": "1234"
}`
- 응답 예시
`{
  "id": 1,
  "todo": "할 일",
  "authorName": "홍길동",
  "authorEmail": "hong@example.com",
  "createdAt": "2025-05-25T12:34:56",
  "updatedAt": "2025-05-25T13:00:00"
}`

### 5. 일정 삭제
DELETE `/api/schedules/{id}`
- 요청 바디
`{
  "password": "1234"
}`
- 응답 예시
`204 No Content`
