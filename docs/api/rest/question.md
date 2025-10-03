
# Question API

## `GET /api/v1/questions`

등록된 모든 질문 정보를 조회합니다.

::: code-group
```md [Response]
**Status**: `200 OK`

**Body**: `List<QuestionResponse>`

**Example**
```json
[
  {
    "id": 1,
    "content": "어떤 음식을 좋아하시나요?",
    "type": "COMMON"
  },
  {
    "id": 2,
    "content": "최근에 본 영화는 무엇인가요?",
    "type": "PERSONAL"
  }
]
```
```
:::

---

## `GET /api/v1/questions/{id}`

ID를 통해 특정 질문 정보를 조회합니다.

-   **Path-Variable**: `id` (Long) - 조회할 질문의 ID

::: code-group
```md [Response]
**Status**: `200 OK`

**Body**: `QuestionResponse`

| 필드 | 타입 | 설명 |
| :--- | :--- | :--- |
| `id` | Long | 질문 ID |
| `content` | String | 질문 내용 |
| `type` | String | 질문 유형 (`COMMON`, `PERSONAL`) |

**Example**
```json
{
  "id": 1,
  "content": "어떤 음식을 좋아하시나요?",
  "type": "COMMON"
}
```
```
:::

---

## `POST /api/v1/questions`

새로운 질문을 생성합니다.

::: code-group
```md [Request]
**Body**: `CreateQuestionCommand`

| 필드 | 타입 | 설명 |
| :--- | :--- | :--- |
| `content` | String | 질문 내용 (max 255) |
| `questionType` | String | 질문 유형 (예: `"공통"`, `"개인"`) |

**Example**
```json
{
  "content": "어떤 음식을 좋아하시나요?",
  "questionType": "공통"
}
```
```

```md [Response]
**Status**: `201 Created`

(Response Body는 비어있습니다)
```
:::

---

## `DELETE /api/v1/questions/{id}`

ID를 통해 특정 질문을 삭제합니다.

-   **Path-Variable**: `id` (Long) - 삭제할 질문의 ID
-   **Success Response**: `200 OK`
