# User API

## GET /api/v1/users {#get-users}

등록된 모든 사용자 정보를 조회합니다.

- **Status**: 200 OK

::: code-group

```json [Response]
[
  {
    "id": 1,
    "name": "홍길동",
    "phone": "01012345678",
    "age": 25,
    "mbti": "INTJ",
    "introduction": "안녕하세요, 백엔드 개발자입니다.",
    "interests": [
      "음악",
      "영화"
    ]
  }
]
```
:::

### Response 필드

| 필드 | 타입 | 설명 |
|:---|:---|:---|
| id | Long | 사용자 ID |
| name | String | 사용자 이름 |
| phone | String | 전화번호 |
| age | Integer | 나이 |
| mbti | String | MBTI 타입 |
| introduction | String | 한줄 소개 |
| interests | Set\<String> | 관심사 목록 |

### 발생 가능한 에러

| ErrorCode | HTTP Status | 메시지 |
| --- | --- | --- |
| `INTERNAL_SERVER_ERROR` | 500 | 서버 내부 오류가 발생했습니다. |

## GET /api/v1/users/{id} {#get-user-by-id}

ID를 통해 특정 사용자 정보를 조회합니다.

- **Path Variable**: id (Long) - 조회할 사용자 ID
- **Status**: 200 OK

::: code-group

```json [Response]
{
  "id": 1,
  "name": "홍길동",
  "phone": "01012345678",
  "age": 25,
  "mbti": "INTJ",
  "introduction": "안녕하세요, 백엔드 개발자입니다.",
  "interests": [
    "음악",
    "영화"
  ]
}
```
:::

### Response 필드

| 필드 | 타입 | 설명 |
|:---|:---|:---|
| id | Long | 사용자 ID |
| name | String | 사용자 이름 |
| phone | String | 전화번호 |
| age | Integer | 나이 |
| mbti | String | MBTI 타입 |
| introduction | String | 한줄 소개 |
| interests | Set\<String> | 관심사 목록 |

### 발생 가능한 에러

| ErrorCode | HTTP Status | 메시지 |
| --- | --- | --- |
| `USER_NOT_FOUND` | 404 | 사용자를 찾을 수 없습니다. |
| `INTERNAL_SERVER_ERROR` | 500 | 서버 내부 오류가 발생했습니다. |

## POST /api/v1/users {#post-user}

새로운 사용자를 생성하며, 이미 존재하면 스킵하고 토큰을 발급합니다.

::: code-group

```json [Request]
{
  "name": "홍길동",
  "phone": "01012345678",
  "age": 25,
  "interests": [
    "음악",
    "영화"
  ],
  "mbtiValue": "INTJ",
  "introduction": "안녕하세요, 백엔드 개발자입니다."
}
```

```json [Response]
{
  "userId": 1,
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzE0Nj..."
}
```
:::

### Request 필드

| 필드 | 타입 | 설명 |
|:---|:---|:---|
| name | String | 사용자 이름 (max 20) |
| phone | String | 전화번호 (10-11자리 숫자) |
| age | Integer | 나이 (0-120) |
| interests | Set\<String> | 관심사 목록 |
| mbtiValue | String | MBTI 값 |
| introduction | String | 한줄 소개 (max 100) |

### Response 필드

| 필드 | 타입 | 설명 |
|:---|:---|:---|
| userId | Long | 생성 또는 조회된 사용자 ID |
| token | String | JWT 인증 토큰 |

### 발생 가능한 에러

| ErrorCode | HTTP Status | 메시지 |
| --- | --- | --- |
| `INVALID_REQUEST` | 400 | 유효하지 않은 요청입니다. |
| `INVALID_MBTI_TYPE` | 400 | 유효하지 않은 MBTI 타입입니다. |
| `INVALID_INTEREST_TYPE` | 400 | 유효하지 않은 관심사 타입입니다. |
| `INTERNAL_SERVER_ERROR` | 500 | 서버 내부 오류가 발생했습니다. |

## DELETE /api/v1/users/{id} {#delete-user}

ID를 통해 특정 사용자를 삭제합니다.

- **Path Variable**: id (Long) - 삭제할 사용자 ID
- **Status**: 200 OK

### 발생 가능한 에러

| ErrorCode | HTTP Status | 메시지 |
| --- | --- | --- |
| `USER_NOT_FOUND` | 404 | 사용자를 찾을 수 없습니다. |
| `INTERNAL_SERVER_ERROR` | 500 | 서버 내부 오류가 발생했습니다. |