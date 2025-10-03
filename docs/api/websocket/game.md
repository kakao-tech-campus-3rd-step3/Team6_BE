
# Game WebSocket API

## 메시지 발행 (Client → Server)

### 게임 시작

-   **Destination**: `/app/room/{roomCode}/start-game`
-   **PathVariable**: `roomCode` (String)
-   **설명**: 현재 스테이지에 맞는 게임 시작을 요청합니다. 방장만 호출할 수 있습니다.

### 게임 목록 요청

-   **Destination**: `/app/room/{roomCode}/game-list`
-   **PathVariable**: `roomCode` (String)
-   **설명**: 선택 가능한 게임 목록을 요청합니다. 방장만 호출할 수 있습니다.

---

## 메시지 구독 (Server → Client)

### 게임 목록 구독

-   **Topic**: `/topic/game-list/{roomCode}` (예상)
-   **설명**: `game-list` 요청 시, 방장에게 선택 가능한 게임 목록을 전송합니다.

::: code-group
```md [Payload]
**Body**: `SuccessApiResponse<List<GameCategory>>`

**Example**
```json
{
  "status": "SUCCESS",
  "data": [
    "MANITTO",
    "RANDOM_ROULETTE",
    "TOPIC_RECOMMEND"
  ],
  "message": "게임 리스트를 정상적으로 전송했습니다."
}
```
```
:::

### 게임 결과 구독

-   **Topic**: `/topic/game-result/{roomCode}` (예상)
-   **설명**: `start-game` 요청 시, 해당 게임의 결과를 모든 참여자에게 전송합니다. 현재 `GameResult`가 비어있어 구체적인 Payload는 미정입니다.
