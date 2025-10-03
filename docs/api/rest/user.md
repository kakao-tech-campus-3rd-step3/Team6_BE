
# User API

## `GET /api/v1/users`

등록된 모든 사용자 정보를 조회합니다.

::: code-group
```md [Response]
**Status**: `200 OK`

**Body**: `List<UserResponse>`

**Example**
```json
[
  {
    "id": 1,
    "name": "홍길동",
    "phone": "01012345678",
    "age": 25,
    "mbti": "INTJ",
    "introduction": "안녕하세요, 백엔드 개발자입니다.",
    "interests": ["음악", "영화"]
  }
]
```
```
:::

---

## `GET /api/v1/users/{id}`

ID를 통해 특정 사용자 정보를 조회합니다.

-   **Path-Variable**: `id` (Long) - 조회할 사용자의 ID

::: code-group
```md [Response]
**Status**: `200 OK`

**Body**: `UserResponse`

| 필드 | 타입 | 설명 |
| :--- | :--- | :--- |
| `id` | Long | 사용자 ID |
| `name` | String | 사용자 이름 |
| `phone` | String | 전화번호 |
| `age` | Integer | 나이 |
| `mbti` | String | MBTI 타입 |
| `introduction` | String | 한줄 소개 |
| `interests` | Set<String> | 관심사 목록 |

**Example**
```json
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
```
:::

---

## `POST /api/v1/users`

새로운 사용자를 생성하며, 이미 존재하면 스킵하고 토큰을 발급합니다.

::: code-group
```md [Request]
**Body**: `CreateUserCommand`

| 필드 | 타입 | 설명 |
| :--- | :--- | :--- |
| `name` | String | 사용자 이름 (max 20) |
| `phone` | String | 전화번호 (10-11자리 숫자) |
| `age` | Integer | 나이 (0-120) |
| `interests` | Set<String> | 관심사 목록 |
| `mbtiValue` | String | MBTI 값 |
| `introduction` | String | 한줄 소개 (max 100) |

**Example**
```json
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
```

```md [Response]
**Status**: `201 Created`

**Body**: `UserIdWithTokenResponse`

| 필드 | 타입 | 설명 |
| :--- | :--- | :--- |
| `userId` | Long | 생성 또는 조회된 사용자 ID |
| `token` | String | JWT 인증 토큰 |

**Example**
```json
{
  "userId": 1,
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzE0Nj..."
}
```
```
:::

---

## `DELETE /api/v1/users/{id}`

ID를 통해 특정 사용자를 삭제합니다.

-   **Path-Variable**: `id` (Long) - 삭제할 사용자의 ID
-   **Success Response**: `200 OK`
