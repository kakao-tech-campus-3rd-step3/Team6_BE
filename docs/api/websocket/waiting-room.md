# Waiting Room WebSocket API

## Client → Server

### 대기방 생성

- **Destination**: `/app/waiting-room/create`
- **설명**: 새로운 대기방을 생성합니다. 성공 시 생성된 `roomId`를 `/user/queue/waiting-room`으로 전송받습니다.

#### Request Payload: `CreateWaitingRoomCommand`

| 필드       | 타입      | 설명              |
|:---------|:--------|:----------------|
| name     | String  | 방 이름 (max 255)  |
| capacity | Integer | 최대 참여 인원 (2-20) |

::: code-group

```json [Request Payload Example]
{
  "name": "카카오테크캠퍼스 아이디어톤",
  "capacity": 7
}
```

:::

#### Response (to `/user/queue/waiting-room`): `WaitingRoomId`

| 필드          | 타입     | 설명                  |
|:------------|:-------|:--------------------|
| status      | String | 응답 상태 (`"SUCCESS"`) |
| data        | Object | 응답 데이터              |
| data.roomId | String | 생성된 대기방 ID          |
| message     | String | 응답 메시지              |

::: code-group

```json [Response Payload Example]
{
  "status": "SUCCESS",
  "data": {
    "roomId": "a1b2c3d4-e5f6-7890-1234-567890abcdef"
  },
  "message": "대기방이 정상적으로 생성되었습니다."
}
```

:::

### 대기방 참여

- **Destination**: `/app/waiting-room/{roomId}/join`
- **PathVariable**: `roomId` (String)
- **설명**: 기존 대기방에 참여합니다. 참여 성공 시, 해당 대기방의 모든 참여자에게 `/topic/waiting-room/{roomId}`로
  `PARTICIPANT_JOINED` 메시지가 전송됩니다.

---

## Server → Client

### 대기방 상태 구독

- **Topic**: `/topic/waiting-room/{roomId}`
- **설명**: 대기방의 상태 변경(참가자 입장, 게임 시작)을 수신합니다. 메시지는 `type` 필드로 구분됩니다.

#### `PARTICIPANT_JOINED`

참가자가 대기방에 입장했을 때 전송됩니다.

**Payload**: `ParticipantJoinedPayload`

| 필드                               | 타입              | 설명                              |
|:---------------------------------|:----------------|:--------------------------------|
| status                           | String          | 응답 상태 (`"SUCCESS"`)             |
| data                             | Object          | 응답 데이터                          |
| data.type                        | String          | 메시지 타입 (`"PARTICIPANT_JOINED"`) |
| data.payload                     | Object          | 실제 페이로드                         |
| data.payload.status              | String          | 대기방 상태 (`AVAILABLE`, `FULL`)    |
| data.payload.room                | Object          | 대기방 정보                          |
| data.payload.room.roomId         | String          | 대기방 ID                          |
| data.payload.room.name           | String          | 대기방 이름                          |
| data.payload.room.capacity       | Integer         | 최대 참여 인원                        |
| data.payload.room.hostId         | Long            | 방장 ID                           |
| data.payload.participants        | Pariticipants[] | 참여자 목록                          |
| data.payload.participants[].id   | Long            | 참여자 ID                          |
| data.payload.participants[].name | String          | 참여자 이름                          |
| data.payload.participants[].role | String          | 참여자 역할 (`HOST`, `MEMBER`)       |
| message                          | String          | 응답 메시지                          |

**Payload**: `ParticipantPayload`

| 필드                               | 타입     | 설명                        |
|:---------------------------------|:-------|:--------------------------|
| data.payload.participants[].id   | Long   | 참여자 ID                    |
| data.payload.participants[].name | String | 참여자 이름                    |
| data.payload.participants[].role | String | 참여자 역할 (`HOST`, `MEMBER`) |

::: code-group

```json [Response Payload Example]
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

:::

#### `ROOM_STARTED`

대기방이 가득 차서 게임이 시작될 때 전송됩니다.

**Payload**: `RoomStartedPayload`

| 필드          | 타입     | 설명                        |
|:------------|:-------|:--------------------------|
| status      | String | 응답 상태 (`"SUCCESS"`)       |
| data        | Object | 응답 데이터                    |
| data.type   | String | 메시지 타입 (`"ROOM_STARTED"`) |
| data.roomId | String | 시작된 방(게임)의 ID             |
| message     | String | 응답 메시지                    |

::: code-group

```json [Response Payload Example]
{
  "status": "SUCCESS",
  "data": {
    "type": "ROOM_STARTED",
    "roomId": "a1b2c3d4-e5f6-7890-1234-567890abcdef"
  },
  "message": "대기방이 시작되었습니다."
}
```

:::