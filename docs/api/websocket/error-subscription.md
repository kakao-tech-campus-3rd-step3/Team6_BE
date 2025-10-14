# WebSocket Error Subscription

WebSocket 통신 중 서버에서 오류가 발생하면, 서버는 해당 오류를 발생시킨 사용자에게 개인화된 에러 메시지를 전송합니다. 클라이언트는 이 메시지를 수신하기 위해 특정 큐를
구독해야 합니다.

## 에러 구독

- **Topic**: `/user/queue/errors`
- **설명**: STOMP 메시지 처리 중 발생하는 모든 `BusinessException` 및 기타 예외에 대한 정보를 수신합니다. `@SendToUser` 어노테이션에 의해
  각 사용자는 자신의 고유한 큐로 에러 응답을 받게 됩니다.

### Payload: `ErrorApiResponse`

| 필드            | 타입     | 설명                             |
|:--------------|:-------|:-------------------------------|
| status        | String | 응답 상태 (`"ERROR"`)              |
| data          | Object | `null`                         |
| error         | Object | 에러 정보                          |
| error.code    | String | 에러 코드 (e.g., `USER_NOT_FOUND`) |
| error.message | String | 에러 메시지                         |

::: code-group

```json [Response Example]
{
  "status": "ERROR",
  "data": null,
  "error": {
    "code": "USER_NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다."
  }
}
```

:::

### 발생 가능한 에러 코드

| ErrorCode                    | 내용                          | HTTP 코드 |
|------------------------------|-----------------------------|---------|
| INTERNAL_SERVER_ERROR        | 서버 내부 오류가 발생했습니다.           | 500     |
| USER_NOT_FOUND               | 사용자를 찾을 수 없습니다.             | 404     |
| INVALID_MBTI_TYPE            | 유효하지 않은 MBTI 타입입니다.         | 400     |
| USER_NOT_AUTHENTICATED       | 사용자가 인증되지 않았습니다.            | 401     |
| ROOM_NOT_FOUND               | 방을 찾을 수 없습니다.               | 404     |
| ROOM_ALREADY_EXISTS          | 방이 이미 존재합니다.                | 409     |
| WAITING_ROOM_NOT_FOUND       | 대기방을 찾을 수 없습니다.             | 404     |
| WAITING_ROOM_FULL            | 대기방이 가득 찼습니다.               | 400     |
| ALREADY_ROOM_JOIN            | 이미 방에 가입되어있습니다.             | 409     |
| ROOM_CAPACITY_EXCEEDED       | 방 정원을 초과했습니다.               | 400     |
| INVALID_STAGE_VALUE          | 유효하지 않은 스테이지 값입니다.          | 400     |
| USER_NOT_IN_ROOM             | 사용자가 방에 속해있지 않습니다.          | 400     |
| INVALID_STAGE_EVENT_VALUE    | 유효하지 않은 스테이지 이벤트 타입입니다.     | 400     |
| INVALID_STAGE_TRANSITION     | 유효하지 않은 스테이지 전환입니다.         | 400     |
| ROOM_STAGE_NOT_FOUND         | 방의 현재 스테이지를 찾을 수 없습니다.      | 404     |
| ROOM_OWNER_NOT_FOUND         | 방장이 존재하지 않습니다.              | 500     |
| ROOM_OWNER_MISMATCH          | 멤버에게는 허용되지 않은 요청입니다.        | 403     |
| INVALID_ROOM_STAGE           | 유효하지 않은 방 스테이지입니다.          | 400     |
| ROOM_STAGE_NOT_INITIALIZED   | 방 스테이지가 초기화되지 않았습니다.        | 400     |
| INIT_STAGE_EVENT_NOT_ALLOWED | 해당 요청에 INIT 이벤트는 허용되지 않습니다. | 400     |
| QUESTION_NOT_FOUND           | 질문을 찾을 수 없습니다.              | 404     |
| INVALID_QUESTION_TYPE        | 유효하지 않은 질문 타입입니다.           | 400     |
| INVALID_INTEREST_TYPE        | 유효하지 않은 관심사 타입입니다.          | 400     |
| INVALID_GAME_CATEGORY        | 유효하지 않은 게임 카테고리입니다.         | 400     |
| INVALID_JWT_PAYLOAD          | 유효하지 않은 JWT 페이로드입니다.        | 401     |
| EXPIRED_JWT_TOKEN            | 만료된 JWT 토큰입니다.              | 401     |
| INVALID_JWT_TOKEN            | 유효하지 않은 JWT 토큰입니다.          | 401     |
| INVALID_JWT_SIGNATURE        | 유효하지 않은 JWT 서명입니다.          | 401     |
| INVALID_REQUEST              | 유효하지 않은 요청입니다.              | 400     |

