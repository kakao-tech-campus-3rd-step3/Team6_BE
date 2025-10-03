# WebSocket Error Subscription

WebSocket 통신 중 서버에서 오류가 발생하면, 서버는 해당 오류를 발생시킨 사용자에게 개인화된 에러 메시지를 전송합니다.

클라이언트는 이 메시지를 수신하기 위해 특정 큐를 구독해야 합니다.

## 에러 구독

- **Topic**: `/user/queue/errors`
- **설명**: STOMP 메시지 처리 중 발생하는 모든 `BusinessException` 및 기타 예외에 대한 정보를 수신합니다. `@SendToUser` 어노테이션에 의해 각 사용자는 자신의 고유한 큐로 에러 응답을 받게 됩니다.
- **Payload**: `ErrorApiResponse`
  ```json
  {
    "status": "ERROR",
    "data": null,
    "error": {
      "code": "ERROR_CODE",
      "message": "Error message"
    }
  }
  ```
