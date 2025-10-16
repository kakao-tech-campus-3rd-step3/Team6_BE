package com.icebreaker.be.infra.persistence.redis.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.infra.messaging.waitingroom.WaitingRoomWebSocketNotifier;
import com.icebreaker.be.infra.persistence.redis.message.MessagePayload;
import com.icebreaker.be.infra.persistence.redis.message.PubSubMessageType;
import com.icebreaker.be.infra.persistence.redis.message.RoomStartedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomStartedMessageHandler implements MessageHandler {

    private final WaitingRoomWebSocketNotifier waitingRoomWebSocketNotifier;
    private final ObjectMapper objectMapper;

    @Override
    public PubSubMessageType getMessageType() {
        return PubSubMessageType.ROOM_STARTED;
    }

    @Override
    public void handleAndSend(MessagePayload payload) {
        RoomStartedMessage startedPayload = objectMapper.convertValue(payload,
                RoomStartedMessage.class);
        waitingRoomWebSocketNotifier.notifyRoomStarted(startedPayload.getRoomId());
    }
}
