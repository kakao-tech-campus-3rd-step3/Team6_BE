package com.icebreaker.be.infra.messaging;

import com.icebreaker.be.global.common.response.ApiResponseFactory;
import com.icebreaker.be.global.common.response.SuccessApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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
        SuccessApiResponse<T> response = ApiResponseFactory.success(payload, message);
        messagingTemplate.convertAndSend(destination, response);
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

