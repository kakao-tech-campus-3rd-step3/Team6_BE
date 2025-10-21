package com.icebreaker.be.infra.messaging;

import com.icebreaker.be.global.common.response.ApiResponseFactory;
import com.icebreaker.be.global.common.response.SuccessApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractStompNotifier {

    protected final SimpMessagingTemplate messagingTemplate;

    /**
     * Topic으로 메시지 전송
     *
     * @param destination Topic 주소
     * @param payload     전송할 실제 데이터
     * @param <T>         payload 타입
     */
    protected <T> void send(String destination, T payload, String message) {
        try {
            SuccessApiResponse<T> response = ApiResponseFactory.success(payload, message);
            messagingTemplate.convertAndSend(destination, response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 특정 사용자에게 개인 메시지 전송
     *
     * @param userId      Principal userId
     * @param destination User destination (예: /queue/match)
     * @param payload     전송할 실제 데이터
     * @param <T>         payload 타입
     */
    protected <T> void sendToUser(String userId, String destination, T payload, String message) {
        try {
            SuccessApiResponse<T> response = ApiResponseFactory.success(payload, message);
            messagingTemplate.convertAndSendToUser(userId, destination, response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Topic 주소 생성
     *
     * @param prefix Topic prefix
     * @param roomId Room ID
     * @return Topic 전체 주소
     */
    protected String buildTopic(String prefix, String roomId) {
        return prefix + roomId;
    }
}

