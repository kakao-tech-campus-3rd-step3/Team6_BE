# Room WebSocket API

## 메시지 발행 (Client → Server)

### 방 단계 변경

-   **Destination**: `/app/room/{roomCode}/change-stage`
-   **PathVariable**: `roomCode` (String)
-   **설명**: 방의 현재 단계를 변경합니다. 방장만 호출할 수 있습니다.
-   **Payload**: `ChangeRoomStageCommand`

| 필드        | 타입   | 설명                                                              | 예시                               |
| :---------- | :----- | :---------------------------------------------------------------- | :--------------------------------- |
| `eventType` | String | 이벤트 타입 (`NEXT`, `PREV`, `SELECT`)                            | `"SELECT"`                         |
| `stage`     | String | `eventType`이 `SELECT`일 경우, 이동할 스테이지 이름 (선택 사항) | `"MANITTO_STAGE"`                  |

<details>
<summary>Payload Example</summary>

```json
{
  "eventType": "SELECT",
  "stage": "MANITTO_STAGE"
}
```

</details>

### 방 참여자 목록 요청

-   **Destination**: `/app/room/{roomCode}/participants`
-   **PathVariable**: `roomCode` (String)
-   **설명**: 현재 방에 있는 모든 참여자 목록을 요청합니다. 방장만 호출할 수 있습니다.

---

## 메시지 구독 (Server → Client)

### 방 단계 변경 구독

-   **Topic**: `/topic/room-stage/{roomId}`
-   **PathVariable**: `roomId` (String)
-   **설명**: 방의 단계(Stage) 변경을 수신합니다.
-   **Payload**: `RoomStageChangedPayload`

<details>
<summary>Payload Example</summary>

```json
{
  "status": "SUCCESS",
  "data": {
    "stage": "MANITTO_STAGE"
  },
  "message": "룸 스테이지가 변경되었습니다."
}
```

</details>

### 방 참여자 목록 구독

-   **Topic**: `/topic/room-participant/{roomId}`
-   **PathVariable**: `roomId` (String)
-   **설명**: 방의 참여자 목록 변경을 수신합니다.
-   **Payload**: `List<RoomParticipantCommand>`

<details>
<summary>Payload Example</summary>

```json
{
  "status": "SUCCESS",
  "data": [
    {
      "id": 1,
      "name": "홍길동",
      "age": 25,
      "mbtiType": "INTJ",
      "interests": ["음악", "영화"]
    },
    {
      "id": 2,
      "name": "김철수",
      "age": 30,
      "mbtiType": "ENFP",
      "interests": ["여행", "스포츠"]
    }
  ],
  "message": "룸 참여자 목록을 정상적으로 전송했습니다."
}
```

</details>