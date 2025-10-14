package com.icebreaker.be.infra.persistence.redis.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.infra.messaging.waitingroom.WaitingRoomWebSocketNotifier;
import com.icebreaker.be.infra.persistence.redis.message.ParticipantJoinedMessage;
import com.icebreaker.be.infra.persistence.redis.message.PubSubMessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParticipantJoinedMessageHandler implements MessageHandler {

    private final WaitingRoomWebSocketNotifier waitingRoomWebSocketNotifier;
    private final ObjectMapper objectMapper;

    @Override
    public PubSubMessageType getMessageType() {
        return PubSubMessageType.PARTICIPANT_JOINED;
    }

    @Override
    public void handleAndSend(Object payload) {
        ParticipantJoinedMessage joinedPayload = objectMapper.convertValue(payload,
                ParticipantJoinedMessage.class);
        waitingRoomWebSocketNotifier.notifyParticipantJoined(
                joinedPayload.getRoomId(),
                joinedPayload.getWaitingRoomWithParticipants());
    }

}
