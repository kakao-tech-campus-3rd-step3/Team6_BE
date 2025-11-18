# Game WebSocket API

## Client → Server

### 게임 시작

- **Destination**: `/app/room/{roomCode}/start-game`
- **PathVariable**: `roomCode` (String)
- **설명**: 현재 스테이지에 맞는 게임 시작을 요청합니다. 방장만 호출할 수 있습니다.

**Payload**: `GameContext`

| 필드        | 타입   | 설명                                                                   |
|:----------|:-------|:---------------------------------------------------------------------|
| roomCode  | String | 현재 방의 코드                                                           |
| type      | String | 시작할 게임 타입 (`MANITTO`, `RANDOM_ROULETTE`, `TOPIC_RECOMMEND`)          |
| topicName | String | `type`이 `TOPIC_RECOMMEND`일 경우, 특정 토픽에 대한 질문을 원할 때 사용하는 토픽 이름 (선택 사항) |

::: code-group

```json [MANITTO / RANDOM_ROULETTE]
{
  "roomCode": "a1b2c3d4",
  "type": "MANITTO"
}
```

```json [TOPIC_RECOMMEND]
{
  "roomCode": "a1b2c3d4",
  "type": "TOPIC_RECOMMEND",
  "topicName": "영화"
}
```

:::

### 게임 목록 요청

- **Destination**: `/app/room/{roomCode}/game-list`
- **PathVariable**: `roomCode` (String)
- **설명**: 선택 가능한 게임 목록을 요청합니다. 방장만 호출할 수 있습니다.

---

## Server → Client

### 게임 목록 구독

- **Topic**: `/topic/game-list/{roomCode}`
- **설명**: `game-list` 요청 시, 방장에게 선택 가능한 게임 목록을 전송합니다.

**Payload**: `GameCategory[]`

| 필드      | 타입       | 설명                  |
|:--------|:---------|:--------------------|
| status  | String   | 응답 상태 (`"SUCCESS"`) |
| data    | String[] | 게임 카테고리 목록          |
| message | String   | 응답 메시지              |

::: code-group

```json [Response Payload Example]
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

:::

### 게임 결과 구독

`start-game` 요청 시, 해당 게임의 결과를 수신합니다. 게임 종류에 따라 수신하는 Topic과 Payload가 다릅니다.

#### 마니또 (MANITTO) 결과

- **Topic**: `/user/queue/game-result` (Unicast)
- **설명**: 마니또 게임 결과로, 각 참여자는 **자신이 지켜줘야 할 마니또가 누구인지** 개인적으로 메시지를 받습니다.

**Payload**: `String` - 내가 지켜줘야 할 마니또의 이름

::: code-group

```json [Response Payload Example]
{
  "status": "SUCCESS",
  "data": "김철수",
  "message": "게임 결과를 정상적으로 전송했습니다."
}
```

:::

#### 랜덤 룰렛 (RANDOM_ROULETTE) 결과

- **Topic**: `/topic/game-result/{roomCode}` (Broadcast)
- **설명**: 랜덤으로 선택된 참여자 1명과, 그 참여자의 관심사 토픽에서 파생된 질문 1개를 모든 참여자에게 전송합니다.

**Payload**: `UserWithQuestion`

| 필드             | 타입     | 설명       |
|:---------------|:-------|:---------|
| data.userName  | String | 선택된 사용자 이름 |
| data.question  | Object | 질문 정보    |
| data.question.content | String | 질문 내용    |

::: code-group

```json [Response Payload Example]
{
  "status": "SUCCESS",
  "data": {
    "userName": "홍길동",
    "question": {
      "content": "가장 기억에 남는 여행지는 어디인가요?"
    }
  },
  "message": "게임 결과를 정상적으로 전송했습니다."
}
```

:::

#### 토픽 추천 (TOPIC_RECOMMEND) 결과

- **Topic**: `/topic/game-result/{roomCode}` (Broadcast)
- **설명**: 요청 시 `topicName`이 있었는지 여부에 따라 다른 정보를 모든 참여자에게 전송합니다.
  - **`topicName`이 없는 경우**: 방 참여자들의 관심사를 기반으로 집계된 주요 토픽들과, 가장 연관성이 높은 토픽에 대한 추천 질문 목록을 반환합니다.
  - **`topicName`이 있는 경우**: 해당 토픽과 관련된 추천 질문 목록을 반환합니다.

**Payload**: `TopicsWithQuestions` 또는 `Question[]`

| 필드                      | 타입         | 설명                        |
|:------------------------|:-----------|:--------------------------|
| data.topics             | Topic[]    | 추천 토픽 목록 (topicName 없을 시) |
| data.topics[].name      | String     | 토픽 이름                     |
| data.questions          | Question[] | 추천 질문 목록                  |
| data.questions[].content | String     | 질문 내용                     |

::: code-group

```json [topicName이 없는 경우]
{
  "status": "SUCCESS",
  "data": {
    "topics": [
      { "name": "여행" },
      { "name": "음악" }
    ],
    "questions": [
      { "content": "가장 기억에 남는 여행지는 어디인가요?" },
      { "content": "여행지에서 겪었던 재미있는 에피소드가 있나요?" }
    ]
  },
  "message": "게임 결과를 정상적으로 전송했습니다."
}
```

```json [topicName이 있는 경우]
{
  "status": "SUCCESS",
  "data": [
    { "content": "최근에 본 영화 중 가장 인상 깊었던 것은 무엇인가요?" },
    { "content": "인생 영화를 3편 꼽는다면?" }
  ],
  "message": "게임 결과를 정상적으로 전송했습니다."
}
```

:::