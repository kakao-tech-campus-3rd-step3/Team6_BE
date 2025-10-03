
# Waiting Room WebSocket API

## 메시지 발행 (Client → Server)

### 대기방 생성

-   **Destination**: `/app/waiting-room/create`
-   **설명**: 새로운 대기방을 생성합니다. 성공 시 생성된 `roomId`를 `/user/queue/waiting-room`으로 전송받습니다.

::: code-group
```md [Payload]
**Body**: `CreateWaitingRoomCommand`

| 필드       | 타입    | 설명           | 예시 |
| :--------- | :------ | :------------- | :--- |
| `name`     | String  | 방 이름 (max 255) | `"카카오테크캠퍼스 아이디어톤"` |
| `capacity` | Integer | 최대 참여 인원 (2-20) | `7`  |

**Example**
```json
{
  "name": "카카오테크캠퍼스 아이디어톤",
  "capacity": 7
}
```
```

```md [Response (to /user/queue/waiting-room)]
**Body**: `SuccessApiResponse<WaitingRoomId>`

**Example**
```json
{
  "status": "SUCCESS",
  "data": {
    "roomId": "a1b2c3d4-e5f6-7890-1234-567890abcdef"
  },
  "message": "대기방이 정상적으로 생성되었습니다."
}
```
```
:::

### 대기방 참여

-   **Destination**: `/app/waiting-room/{roomId}/join`
-   **PathVariable**: `roomId` (String)
-   **설명**: 기존 대기방에 참여합니다. 참여 성공 시, 해당 대기방의 모든 참여자에게 `/topic/waiting-room/{roomId}`로 `PARTICIPANT_JOINED` 메시지가 전송됩니다.

---

## 메시지 구독 (Server → Client)

### 대기방 상태 구독

-   **Topic**: `/topic/waiting-room/{roomId}`
-   **설명**: 대기방의 상태 변경(참가자 입장, 게임 시작)을 수신합니다.

::: code-group
```md [참가자 입장 (PARTICIPANT_JOINED)]
**Type**: `PARTICIPANT_JOINED`

**Body**: `SuccessApiResponse<ParticipantJoinedPayload>`

**Example**
```json
{
  "status": "SUCCESS",
  "data": {
    "type": "PARTICIPANT_JOINED",
    "payload": {
      "status": "AVAILABLE",
      "room": {
        "roomId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
        "name": "카카오테크캠퍼스 아이디어톤",
        "capacity": 7,
        "hostId": 1
      },
      "participants": [
        {
          "id": 1,
          "name": "홍길동",
          "role": "HOST"
        },
        {
          "id": 2,
          "name": "김철수",
          "role": "MEMBER"
        }
      ]
    }
  },
  "message": "대기방에 참가자가 입장했습니다."
}
```
```

```md [게임 시작 (ROOM_STARTED)]
**Type**: `ROOM_STARTED`

**Body**: `SuccessApiResponse<RoomStartedPayload>`

**Example**
```json
{
  "status": "SUCCESS",
  "data": {
    "type": "ROOM_STARTED",
    "roomId": "a1b2c3d4-e5f6-7890-1234-567890abcdef"
  },
  "message": "대기방이 시작되었습니다."
}
```
```
:::
